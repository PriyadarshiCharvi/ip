import java.util.Scanner;

class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
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
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}


public class Oracle {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while(true) {
            String input = in.nextLine();

            if (input.equals("list")) {
                if (taskCount == 0) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    There are no tasks in your list yet.");
                    System.out.println("    ____________________________________________________________");
                } else {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("    " + (i + 1) + ". " + tasks[i]);
                    }
                    System.out.println("    ____________________________________________________________");
                }
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[taskCount] = new Todo(description);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this task to the list:");
                System.out.println("    " + tasks[taskCount]);
                System.out.println("    Now you have " + (taskCount + 1) + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
                taskCount++;
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split("/by");
                String description = parts[0].trim();
                String by = parts[1].trim();
                tasks[taskCount] = new Deadline(description, by);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this deadline to the list:");
                System.out.println("    " + tasks[taskCount]);
                System.out.println("    Now you have " + (taskCount + 1) + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
                taskCount++;
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split("/from|/to");
                String description = parts[0].trim();
                String from = parts[1].trim();
                String to = parts[2].trim();
                tasks[taskCount] = new Event(description, from, to);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this event to the list:");
                System.out.println("    " + tasks[taskCount]);
                System.out.println("    Now you have " + (taskCount + 1) + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
                taskCount++;
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markDone();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    Nice! I've marked this task as done:");
                    System.out.println("    " + tasks[index]);
                    System.out.println("    ____________________________________________________________");
                }
            } else if (input.startsWith("unmark")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markUndone();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    OK, I've marked this task as not done yet:");
                    System.out.println("    " + tasks[index]);
                    System.out.println("    ____________________________________________________________");
                }
            } else if (input.equals("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________");
                break;
            } else {
                tasks[taskCount] = new Task(input);
                System.out.println("    ____________________________________________________________");
                System.out.println("    added: " + input);
                System.out.println("    ____________________________________________________________");
                taskCount++;
            }
        }
        in.close();
    }
}
