package oracle.task;

/**
 * Represents a Todo task, which is a basic task without a deadline or event duration.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with a description.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
