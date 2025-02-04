import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Deadline extends Task {
    private final LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mma");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Deadline(String description, String by) throws OracleException{
        super(description, TaskType.DEADLINE);
        if (by.isBlank()) {
            throw new IllegalArgumentException("Deadline date cannot be empty.");
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

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public LocalDateTime getDateTime() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }

    public String toStorageString() {
        return by.format(STORAGE_FORMATTER);
    }
}