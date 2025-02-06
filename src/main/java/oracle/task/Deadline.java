package oracle.task;

import oracle.common.OracleException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline, containing a description and a due date and time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs a Deadline task with a description and a due date in string format.
     *
     * @param description The description of the task.
     * @param by The due date and time in "d/M/yyyy HHmm" format.
     * @throws OracleException If the date format is incorrect.
     */
    public Deadline(String description, String by) throws OracleException {
        super(description, TaskType.DEADLINE);
        if (by.isBlank()) {
            throw new IllegalArgumentException("oracle.task.Deadline date cannot be empty.");
        }
        try {
            this.by = LocalDateTime.parse(by, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new OracleException(
                    "Please enter deadline in the format: d/M/yyyy HHmm\n" +
                            "    For example: deadline assignment /by 2/12/2023 2359\n" +
                            "    Note: Time should be in 24-hour format."
            );
        }
    }

    /**
     * Constructs a Deadline task with a description and a LocalDateTime due date.
     *
     * @param description The description of the task.
     * @param by The due date and time as a LocalDateTime object.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Retrieves the due date of the deadline task.
     *
     * @return The due date and time as a LocalDateTime object.
     */
    public LocalDateTime getDateTime() {
        return by;
    }

    /**
     * Returns a string representation of the Deadline task for user display.
     *
     * @return A formatted string containing the task description and due date.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }

    /**
     * Converts the deadline's due date to a string format suitable for file storage.
     *
     * @return The due date formatted as "yyyy-MM-dd HHmm".
     */
    public String toStorageString() {
        return by.format(STORAGE_FORMATTER);
    }
}