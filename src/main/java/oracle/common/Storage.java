package oracle.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import oracle.task.Deadline;
import oracle.task.Event;
import oracle.task.Task;
import oracle.task.Todo;

/**
 * Handles loading and saving of tasks to a file for persistent storage.
 */
public class Storage {
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final Path filePath;

    /**
     * Constructs a Storage object to manage file operations.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file and returns them as a list.
     * If the file does not exist, it creates a new file and returns an empty task list.
     *
     * @return A list of tasks loaded from the file.
     * @throws OracleException If an error occurs while reading the file.
     */
    public ArrayList<Task> load() throws OracleException {
        try {
            ensureFileExists();
            return parseTasks(Files.readAllLines(filePath));
        } catch (IOException e) {
            throw new OracleException("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Ensures that the storage file and its parent directory exist.
     *
     * @throws IOException If an error occurs while creating directories or files.
     */
    private void ensureFileExists() throws IOException {
        Path parentDir = filePath.getParent();
        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Parses task entries from a list of strings.
     *
     * @param lines The list of task entries read from the storage file.
     * @return A list of parsed tasks.
     */
    private ArrayList<Task> parseTasks(List<String> lines) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            try {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            } catch (Exception e) {
                System.err.println("Skipping corrupted entry: " + line);
            }
        }
        return tasks;
    }

    /**
     * Parses a single task entry from a string.
     *
     * @param line A string representing a task in storage format.
     * @return The parsed Task object, or null if the entry is invalid.
     */
    private Task parseTask(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
        case "T" -> task = new Todo(description);
        case "D" -> task = (parts.length >= 4)
                ? new Deadline(description, parseDate(parts[3]))
                : null;
        case "E" -> task = (parts.length >= 5)
                ? new Event(description, parseDate(parts[3]), parseDate(parts[4]))
                : null;
        default -> task = null;
        }
        ;

        if (task != null && isDone) {
            task.markDone();
        }
        return task;
    }

    /**
     * Parses a date string into a LocalDateTime object.
     *
     * @param dateString The date string in the expected storage format.
     * @return A LocalDateTime object representing the parsed date.
     */
    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString.trim(), STORAGE_FORMATTER);
    }

    /**
     * Saves the given list of tasks to the file for persistent storage.
     *
     * @param tasks The list of tasks to be saved.
     * @throws OracleException If an error occurs while writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws OracleException {
        try {
            Path parentDir = filePath.getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                String isDone = task.getStatusIcon().equals("X") ? "1" : "0";

                String line;
                switch (task.getType()) {
                case TODO -> line = String.format("T | %s | %s", isDone,
                        task.toString().substring(task.toString().indexOf("] ") + 2));
                case DEADLINE -> {
                    Deadline d = (Deadline) task;
                    String desc = task.toString().substring(task.toString().indexOf("] ") + 2,
                            task.toString().indexOf(" (by:"));
                    line = String.format("D | %s | %s | %s", isDone, desc, d.toStorageString());
                }
                case EVENT -> {
                    Event e = (Event) task;
                    String desc = task.toString().substring(task.toString().indexOf("] ") + 2,
                            task.toString().indexOf(" (from:"));
                    line = String.format("E | %s | %s | %s", isDone, desc, e.toStorageString());
                }
                default -> line = " ";
                }
                ;
                lines.add(line);
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new OracleException("Error saving tasks: " + e.getMessage());
        }
    }
}
