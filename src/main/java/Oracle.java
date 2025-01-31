import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

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
    private final String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        if (by.isBlank()) {
            throw new IllegalArgumentException("Deadline date cannot be empty.");
        }
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}

class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("Event time cannot be empty.");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
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
                            task = new Deadline(description, parts[3].trim());
                            break;
                        case "E":
                            if (parts.length < 5) continue;
                            task = new Event(description, parts[3].trim(), parts[4].trim());
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

                String taskStr = task.toString();
                String description = taskStr.substring(taskStr.indexOf("] ") + 2);

                String line = switch (task.getType()) {
                    case TODO ->
                            String.format("T | %s | %s", isDone, description);
                    case DEADLINE -> {
                        String desc = description.substring(0, description.indexOf(" (by:"));
                        String by = description.substring(description.indexOf("by: ") + 4, description.length() - 1);
                        yield String.format("D | %s | %s | %s", isDone, desc, by);
                    }
                    case EVENT -> {
                        String desc = description.substring(0, description.indexOf(" (from:"));
                        String timeStr = description.substring(description.indexOf("from: "));
                        String from = timeStr.substring(6, timeStr.indexOf(" to:"));
                        String to = timeStr.substring(timeStr.indexOf("to: ") + 4, timeStr.length() - 1);
                        yield String.format("E | %s | %s | %s | %s", isDone, desc, from, to);
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
                        throw new OracleException("OOPS!!! The correct format for deadline is: deadline [task] /by [date]");
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
                System.out.println("    OOPS!!! Something went wrong: " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            }

        }
        in.close();
    }
}
