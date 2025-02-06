package oracle.common;

import oracle.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interactions by displaying messages and reading user input.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a Ui object with a scanner to read user input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays a welcome message when the program starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        showLine();
    }

    /**
     * Displays a horizontal line for formatting purposes.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Reads a command input from the user.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showLine();
        System.out.println("    " + message);
        showLine();
    }

    /**
     * Displays an error message when tasks cannot be loaded from the storage file.
     */
    public void showLoadingError() {
        showError("Error loading tasks from file.");
    }

    /**
     * Displays the list of tasks currently stored.
     *
     * @param tasks The list of tasks to display.
     */
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

    /**
     * Displays a message when a task is successfully added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding the new one.
     */
    public void showAddedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("    Got it. I've added this task to the list:");
        System.out.println("    " + task);
        System.out.println("    Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message when a task is successfully deleted.
     *
     * @param task The task that was deleted.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showDeletedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message when a task is successfully marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showMarkedTask(Task task) {
        showLine();
        System.out.println("    Great! I've marked this task as done:");
        System.out.println("    " + task);
        showLine();
    }

    /**
     * Displays a message when a task is successfully unmarked (set to not done).
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        showLine();
        System.out.println("    Alright, I've marked this task as not done yet:");
        System.out.println("    " + task);
        showLine();
    }

    /**
     * Displays the tasks that match the given search keyword.
     *
     * @param tasks The list of matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println("    No matching tasks found.");
        } else {
            System.out.println("    Here are the matching tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + tasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Displays a goodbye message when the program exits.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("    Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Closes the scanner used for user input.
     */
    public void close() {
        scanner.close();
    }
}
