package oracle.task;

/**
 * Represents the different types of tasks that can be created.
 */
public enum TaskType {
    /** A simple task without a deadline or event duration. */
    TODO,

    /** A task that has a specific deadline. */
    DEADLINE,

    /** A task that occurs within a specific time frame. */
    EVENT
}

