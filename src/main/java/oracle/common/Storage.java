package oracle.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
     * Loads tasks from the file and returns them as a list.
     * If the file does not exist, it creates a new file and returns an empty task list.
     *
     * @return A list of tasks loaded from the file.
     * @throws OracleException If an error occurs while reading the file.
     */
    public ArrayList<Task> load() throws OracleException {
        assert filePath != null : "File path should not be null";
        try {
            Path parentDir = filePath.getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return new ArrayList<>();
            }

            ArrayList<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 3) {
                        continue;
                    }

                    String type = parts[0].trim();
                    boolean isDone = parts[1].trim().equals("1");
                    String description = parts[2].trim();
                    Task task;

                    switch (type) {
                    case "T":
                        task = new Todo(description);
                        break;
                    case "D":
                        if (parts.length < 4) {
                            continue;
                        }
                        LocalDateTime deadline = LocalDateTime.parse(parts[3].trim(), STORAGE_FORMATTER);
                        task = new Deadline(description, deadline);
                        break;
                    case "E":
                        if (parts.length < 5) {
                            continue;
                        }
                        LocalDateTime eventStart = LocalDateTime.parse(parts[3].trim(), STORAGE_FORMATTER);
                        LocalDateTime eventEnd = LocalDateTime.parse(parts[4].trim(), STORAGE_FORMATTER);
                        task = new Event(description, eventStart, eventEnd);
                        break;
                    default:
                        continue;
                    }

                    if (isDone) {
                        task.markDone();
                    }
                    tasks.add(task);
                } catch (Exception e) {
                    System.err.println("Skipping corrupted entry: " + line);
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new OracleException("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Saves the given list of tasks to the file for persistent storage.
     *
     * @param tasks The list of tasks to be saved.
     * @throws OracleException If an error occurs while writing to the file.
     */
    @SuppressWarnings("checkstyle:Indentation")
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
