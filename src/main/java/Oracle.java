import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

enum TaskType {
    TODO, DEADLINE, EVENT;
}

class Task {
    private final String description;
    private final TaskType type;
    private boolean isDone;

    public Task(String description, TaskType type) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        String typeIcon = switch (type) {
            case TODO -> "[T]";
            case DEADLINE -> "[D]";
            case EVENT -> "[E]";
        };
        return typeIcon + "[" + getStatusIcon() + "] " + description;
    }
}

class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}

class Deadline extends Task {
    private final LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        if (by.isBlank()) {
            throw new IllegalArgumentException("Deadline date cannot be empty.");
        }
        try {
            this.by = LocalDateTime.parse(by, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date must be in format: d/M/yyyy HHmm (e.g., 2/12/2023 1800)");
        }
    }

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public LocalDateTime getDateTime() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }

    public String toStorageString() {
        return by.format(STORAGE_FORMATTER);
    }
}

class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("Event time cannot be empty.");
        }
        try {
            this.from = LocalDateTime.parse(from, INPUT_FORMATTER);
            this.to = LocalDateTime.parse(to, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Dates must be in format: d/M/yyyy HHmm (e.g., 2/12/2023 1800)");
        }
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getStartDateTime() {
        return from;
    }

    public LocalDateTime getEndDateTime() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from.format(OUTPUT_FORMATTER) + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }

    public String toStorageString() {
        return from.format(STORAGE_FORMATTER) + "|" + to.format(STORAGE_FORMATTER);
    }
}

class OracleException extends Exception {
    public OracleException(String message) {
        super(message);
    }
}

public class Oracle {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("oracle.txt");
    private final ArrayList<Task> tasks;

    public Oracle() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    private void loadTasks() {
        try {
            if (!Files.exists(DATA_DIRECTORY)) {
                Files.createDirectories(DATA_DIRECTORY);
            }

            if (!Files.exists(DATA_FILE)) {
                Files.createFile(DATA_FILE);
                return;
            }

            for (String line : Files.readAllLines(DATA_FILE)) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 3) continue;

                    String type = parts[0].trim();
                    boolean isDone = parts[1].trim().equals("1");
                    String description = parts[2].trim();
                    Task task;

                    switch (type) {
                        case "T":
                            task = new Todo(description);
                            break;
                        case "D":
                            if (parts.length < 4) continue;
                            LocalDateTime deadline = LocalDateTime.parse(parts[3].trim(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            task = new Deadline(description, deadline);
                            break;
                        case "E":
                            if (parts.length < 5) continue;
                            LocalDateTime eventStart = LocalDateTime.parse(parts[3].trim(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            LocalDateTime eventEnd = LocalDateTime.parse(parts[4].trim(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            task = new Event(description, eventStart, eventEnd);
                            break;
                        default:
                            continue;
                    }

                    if (isDone) {
                        task.markDone();
                    }
                    tasks.add(task);
                } catch (Exception e) {
                    System.err.println("Skipping corrupted entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            if (!Files.exists(DATA_DIRECTORY)) {
                Files.createDirectories(DATA_DIRECTORY);
            }

            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                String isDone = task.getStatusIcon().equals("X") ? "1" : "0";

                String line = switch (task.getType()) {
                    case TODO -> String.format("T | %s | %s", isDone,
                            task.toString().substring(task.toString().indexOf("] ") + 2));
                    case DEADLINE -> {
                        Deadline d = (Deadline) task;
                        String desc = task.toString().substring(task.toString().indexOf("] ") + 2,
                                task.toString().indexOf(" (by:"));
                        yield String.format("D | %s | %s | %s", isDone, desc, d.toStorageString());
                    }
                    case EVENT -> {
                        Event e = (Event) task;
                        String desc = task.toString().substring(task.toString().indexOf("] ") + 2,
                                task.toString().indexOf(" (from:"));
                        yield String.format("E | %s | %s | %s", isDone, desc, e.toStorageString());
                    }
                };
                lines.add(line);
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Oracle oracle = new Oracle();
        Scanner in = new Scanner(System.in);

        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while(true) {
            try {
                String input = in.nextLine();

                if (input.equals("list")) {
                    if (oracle.tasks.isEmpty()) {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    There are no tasks in your list yet.");
                        System.out.println("    ____________________________________________________________");
                    } else {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Here are the tasks in your list:");
                        for (int i = 0; i < oracle.tasks.size(); i++) {
                            System.out.println("    " + (i + 1) + ". " + oracle.tasks.get(i));
                        }
                        System.out.println("    ____________________________________________________________");
                    }
                } else if (input.startsWith("todo")) {
                    if (input.length() <= 5) {
                        throw new OracleException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    String description = input.substring(5);
                    oracle.tasks.add(new Todo(description));
                    oracle.saveTasks();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this task to the list:");
                    System.out.println("    " + oracle.tasks.get(oracle.tasks.size() - 1));
                    System.out.println("    Now you have " + oracle.tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("deadline")) {
                    String[] parts = input.substring(8).split("/by", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new OracleException("OOPS!!! The correct format for deadline is: deadline [task] /by [date/time]");
                    }
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    oracle.tasks.add(new Deadline(description, by));
                    oracle.saveTasks();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this deadline to the list:");
                    System.out.println("    " + oracle.tasks.get(oracle.tasks.size() - 1));
                    System.out.println("    Now you have " + oracle.tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("event")) {
                    String[] parts = input.substring(5).split("/from|/to", 3);
                    if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                        throw new OracleException("OOPS!!! The correct format for event is: event [task] /from [start] /to [end]");
                    }
                    String description = parts[0].trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();
                    oracle.tasks.add(new Event(description, from, to));
                    oracle.saveTasks();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this event to the list:");
                    System.out.println("    " + oracle.tasks.get(oracle.tasks.size() - 1));
                    System.out.println("    Now you have " + oracle.tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("delete")) {
                    if (oracle.tasks.isEmpty()) {
                        throw new OracleException("There are no tasks to delete.");
                    }
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index < 0 || index >= oracle.tasks.size()) {
                        throw new OracleException("Invalid task number. Please enter a number between 1 and " + oracle.tasks.size());
                    }
                    Task removedTask = oracle.tasks.remove(index);
                    oracle.saveTasks();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Noted. I've removed this task:");
                    System.out.println("      " + removedTask);
                    System.out.println("    Now you have " + oracle.tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("mark")) {
                    if (oracle.tasks.isEmpty()) {
                        throw new OracleException("Looks like your task list is empty! Add your first task before marking anything as done.");
                    }
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    if (index < 0 || index >= oracle.tasks.size()) {
                        throw new OracleException("OOPS!!! Task number is invalid. Please enter a number between 1 and " + oracle.tasks.size());
                    }
                        oracle.tasks.get(index).markDone();
                        oracle.saveTasks();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Great! I've marked this task as done:");
                        System.out.println("    " + oracle.tasks.get(index));
                        System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("unmark")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index < 0 || index > oracle.tasks.size()) {
                        throw new OracleException("OOPS!!! Task number is invalid. Please enter a number between 1 and " + oracle.tasks.size());
                    }
                        oracle.tasks.get(index).markUndone();
                        oracle.saveTasks();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Alright, I've marked this task as not done yet:");
                        System.out.println("    " + oracle.tasks.get(index));
                        System.out.println("    ____________________________________________________________");
                } else if (input.equals("bye")) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Bye. Hope to see you again soon!");
                    System.out.println("    ____________________________________________________________");
                    break;
                } else {
                    throw new OracleException("OOPS!!! I'm sorry, but I don't know what that means :-(. Try something like 'todo assignment'.");
                }
            } catch (OracleException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Please enter a valid task number.");
                System.out.println("    ____________________________________________________________");
            } catch (Exception e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    OOPS!!! " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            }

        }
        in.close();
    }
}
