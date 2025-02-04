import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println("    " + message);
        showLine();
    }

    public void showLoadingError() {
        showError("Error loading tasks from file.");
    }

    public void showTasks(List<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println("    There are no tasks in your list yet.");
        } else {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + tasks.get(i));
            }
        }
        showLine();
    }

    public void showAddedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("    Got it. I've added this task to the list:");
        System.out.println("    " + task);
        System.out.println("    Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    public void showDeletedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    public void showMarkedTask(Task task) {
        showLine();
        System.out.println("    Great! I've marked this task as done:");
        System.out.println("    " + task);
        showLine();
    }

    public void showUnmarkedTask(Task task) {
        showLine();
        System.out.println("    Alright, I've marked this task as not done yet:");
        System.out.println("    " + task);
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println("    Bye. Hope to see you again soon!");
        showLine();
    }

    public void close() {
        scanner.close();
    }
}
