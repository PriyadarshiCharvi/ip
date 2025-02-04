import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Event(String description, String from, String to) throws OracleException {
        super(description, TaskType.EVENT);
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("Event time cannot be empty.");
        }
        try {
            this.from = LocalDateTime.parse(from, INPUT_FORMATTER);
            this.to = LocalDateTime.parse(to, INPUT_FORMATTER);
            if (this.to.isBefore(this.from)) {
                throw new OracleException("Event end time cannot be before start time.");
            }
        } catch (DateTimeParseException e) {
            throw new OracleException(
                    "Please enter dates in the format: d/M/yyyy HHmm\n" +
                            "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500\n" +
                            "    Note: Time should be in 24-hour format."
            );
        }
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getStartDateTime() {
        return from;
    }

    public LocalDateTime getEndDateTime() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from.format(OUTPUT_FORMATTER) + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }

    public String toStorageString() {
        return from.format(STORAGE_FORMATTER) + "|" + to.format(STORAGE_FORMATTER);
    }
}