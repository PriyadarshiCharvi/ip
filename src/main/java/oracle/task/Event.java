package oracle.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import oracle.common.OracleException;



/**
 * Represents an event task with a start and end date/time.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs an Event task with a description, start time, and end time as strings.
     *
     * @param description The description of the event.
     * @param from The start date/time in "d/M/yyyy HHmm" format.
     * @param to The end date/time in "d/M/yyyy HHmm" format.
     * @throws OracleException If the date format is incorrect or the end time is before the start time.
     */
    public Event(String description, String from, String to) throws OracleException {
        super(description, TaskType.EVENT);
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("oracle.task.Event time cannot be empty.");
        }
        try {
            this.from = LocalDateTime.parse(from, INPUT_FORMATTER);
            this.to = LocalDateTime.parse(to, INPUT_FORMATTER);
            if (this.to.isBefore(this.from)) {
                throw new OracleException("oracle.task.Event end time cannot be before start time.");
            }
        } catch (DateTimeParseException e) {
            throw new OracleException(
                    "Please enter dates in the format: d/M/yyyy HHmm\n"
                            + "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500\n"
                            + "    Note: Time should be in 24-hour format."
            );
        }
    }

    /**
     * Constructs an Event task with a description, start time, and end time as LocalDateTime objects.
     *
     * @param description The description of the event.
     * @param from The start date/time as a LocalDateTime object.
     * @param to The end date/time as a LocalDateTime object.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Retrieves the start date/time of the event.
     *
     * @return The start date/time as a LocalDateTime object.
     */
    public LocalDateTime getStartDateTime() {
        return from;
    }

    /**
     * Retrieves the end date/time of the event.
     *
     * @return The end date/time as a LocalDateTime object.
     */
    public LocalDateTime getEndDateTime() {
        return to;
    }

    /**
     * Returns a string representation of the Event task for user display.
     *
     * @return A formatted string containing the task description, start time, and end time.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from.format(OUTPUT_FORMATTER)
                + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }

    /**
     * Converts the event's start and end times to a string format suitable for file storage.
     *
     * @return A formatted string containing the start and end times in "yyyy-MM-dd HHmm" format.
     */
    public String toStorageString() {
        return from.format(STORAGE_FORMATTER) + "|" + to.format(STORAGE_FORMATTER);
    }

    /**
     * Reschedules the event to a new start time while preserving its duration.
     *
     * @param newFrom The new start date and time.
     * @param newTo The new end date and time.
     * @throws OracleException If the new end time is before the new start time.
     */
    public void reschedule(LocalDateTime newFrom, LocalDateTime newTo) throws OracleException {
        if (newTo.isBefore(newFrom)) {
            throw new OracleException("Event end time cannot be before start time.");
        }
        this.from = newFrom;
        this.to = newTo;
    }


}
