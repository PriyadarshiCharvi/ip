package oracle.task;

public class Task {
    private final String description;
    private final TaskType type;
    private boolean isDone;

    public Task(String description, TaskType type) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("oracle.task.Task description cannot be empty.");
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