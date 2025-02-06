package oracle.task;

import oracle.common.OracleException;

import java.util.ArrayList;

/**
 * Represents a list of tasks. Provides methods to add, remove, retrieve, and check tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks The list of tasks to initialize the TaskList with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the task list by index.
     *
     * @param index The zero-based index of the task to be deleted.
     * @return The task that was removed.
     * @throws OracleException If the task list is empty or the index is invalid.
     */
    public Task deleteTask(int index) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("There are no tasks to delete.");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new OracleException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the task list by index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index.
     * @throws OracleException If the index is out of bounds.
     */
    public Task getTask(int index) throws OracleException {
        if (index < 0 || index >= tasks.size()) {
            throw new OracleException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        return tasks.get(index);
    }

    /**
     * Retrieves all tasks in the task list.
     *
     * @return A new ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Retrieves the number of tasks in the task list.
     *
     * @return The total number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return {@code true} if the task list is empty, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Finds tasks that contain the specified keyword in their description.
     *
     * @param keyword The keyword to search for.
     * @return A list of tasks that contain the keyword.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
