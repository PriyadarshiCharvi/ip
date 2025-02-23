package oracle.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Deadline;
import oracle.task.Event;
import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to snooze (postpone) a task.
 */
public class SnoozeCommand extends Command {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private final int index;
    private final String newDateTime;

    /**
     * Constructs a SnoozeCommand with the given task index and new date/time.
     *
     * @param index        The index of the task to be rescheduled.
     * @param newDateTime  The new due date or event time in d/M/yyyy HHmm format.
     */
    public SnoozeCommand(int index, String newDateTime) {
        this.index = index;
        this.newDateTime = newDateTime;
    }

    /**
     * Executes the command by postponing the deadline or event task.
     *
     * @param tasks   The task list where the task is stored.
     * @param ui      The UI component to display results.
     * @param storage The storage component responsible for saving task data.
     * @throws OracleException If the task is invalid or the format is incorrect.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("OOPS! There are no tasks in the list yet. "
                                      + "Please add a deadline or event before trying to snooze.");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new OracleException("That task isn’t in our star system! Please enter a number between 1 and "
                                      + tasks.size());
        }
        Task task = tasks.getTask(index);
        LocalDateTime newDateTime = parseNewDateTime();

        if (task instanceof Deadline deadline) {
            deadline.reschedule(newDateTime);
            ui.showSnoozedTask(task);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            LocalDateTime oldFrom = event.getStartDateTime();
            LocalDateTime oldTo = event.getEndDateTime();

            long durationMinutes = java.time.Duration.between(oldFrom, oldTo).toMinutes();

            LocalDateTime newTo = newDateTime.plusMinutes(durationMinutes);
            event.reschedule(newDateTime, newTo);
            ui.showSnoozedTask(event);
        } else {
            throw new OracleException("Only deadlines and events can be snoozed.");
        }
        storage.save(tasks.getTasks());
    }


    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("\uD83C\uDF0C The cosmos is empty... You have no tasks in your list yet! "
                                      + "Please add a deadline or event before trying to snooze.");
        }
        execute(tasks, ui, storage);
        return "\uD83D\uDEF0\uFE0F Adjusting orbital trajectory… Your task has been rescheduled:\n"
               + tasks.getTask(index);
    }

    private LocalDateTime parseNewDateTime() throws OracleException {
        try {
            return LocalDateTime.parse(newDateTime, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new OracleException("Invalid date format. Use d/M/yyyy HHmm.");
        }
    }
}
