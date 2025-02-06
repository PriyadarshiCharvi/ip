package oracle.task;

/**
 * Represents a generic task with a description, completion status, and type.
 * This class is extended by specific task types such as {@code Todo}, {@code Deadline}, and {@code Event}.
 */
public class Task {
    private final String description;
    private final TaskType type;
    private boolean isDone;

    /**
     * Constructs a new Task with a description and type.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     * @param type The type of the task (TODO, DEADLINE, or EVENT).
     * @throws IllegalArgumentException If the description is empty or blank.
     */
    public Task(String description, TaskType type) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("oracle.task.Task description cannot be empty.");
        }
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Retrieves the status icon of the task.
     * Returns "X" if the task is completed, otherwise returns a space " ".
     *
     * @return A string representing the status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as completed.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Retrieves the type of the task.
     *
     * @return The {@code TaskType} of the task.
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns a string representation of the task, including its type, status, and description.
     *
     * @return A formatted string representing the task.
     */
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