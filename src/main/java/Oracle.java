import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description;
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        if (by.isBlank()) {
            throw new IllegalArgumentException("Deadline date cannot be empty.");
        }
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("Event time cannot be empty.");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

class OracleException extends Exception {
    public OracleException(String message) {
        super(message);
    }
}

public class Oracle {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while(true) {
            try {
                String input = in.nextLine();

                if (input.equals("list")) {
                    if (tasks.isEmpty()) {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    There are no tasks in your list yet.");
                        System.out.println("    ____________________________________________________________");
                    } else {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("    " + (i + 1) + ". " + tasks.get(i));
                        }
                        System.out.println("    ____________________________________________________________");
                    }
                } else if (input.startsWith("todo")) {
                    if (input.length() <= 5) {
                        throw new OracleException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    String description = input.substring(5);
                    tasks.add(new Todo(description));
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this task to the list:");
                    System.out.println("    " + tasks.get(tasks.size() - 1));
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("deadline")) {
                    String[] parts = input.substring(8).split("/by", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new OracleException("OOPS!!! The correct format for deadline is: deadline [task] /by [date]");
                    }
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    tasks.add(new Deadline(description, by));
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this deadline to the list:");
                    System.out.println("    " + tasks.get(tasks.size() - 1));
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("event")) {
                    String[] parts = input.substring(5).split("/from|/to", 3);
                    if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                        throw new OracleException("OOPS!!! The correct format for event is: event [task] /from [start] /to [end]");
                    }
                    String description = parts[0].trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();
                    tasks.add(new Event(description, from, to));
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Got it. I've added this event to the list:");
                    System.out.println("    " + tasks.get(tasks.size() - 1));
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("delete")) {
                    if (tasks.isEmpty()) {
                        throw new OracleException("There are no tasks to delete.");
                    }
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new OracleException("Invalid task number. Please enter a number between 1 and " + tasks.size());
                    }
                    Task removedTask = tasks.remove(index);
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Noted. I've removed this task:");
                    System.out.println("      " + removedTask);
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("mark")) {
                    if (tasks.isEmpty()) {
                        throw new OracleException("Looks like your task list is empty! Add your first task before marking anything as done.");
                    }
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new OracleException("OOPS!!! Task number is invalid. Please enter a number between 1 and " + tasks.size());
                    }
                        tasks.get(index).markDone();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Great! I've marked this task as done:");
                        System.out.println("    " + tasks.get(index));
                        System.out.println("    ____________________________________________________________");
                } else if (input.startsWith("unmark")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index < 0 || index > tasks.size()) {
                        throw new OracleException("OOPS!!! Task number is invalid. Please enter a number between 1 and " + tasks.size());
                    }
                        tasks.get(index).markUndone();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("    Alright, I've marked this task as not done yet:");
                        System.out.println("    " + tasks.get(index));
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
