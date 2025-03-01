package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to unmark a task as incomplete in the task list.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Constructs an UnmarkCommand with the specified task index.
     *
     * @param index The zero-based index of the task to be marked as not done.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command by marking the specified task as not done, saving the updated task list to storage,
     * and displaying a confirmation message to the user.
     *
     * @param tasks   The task list where the task resides.
     * @param ui      The UI component to display feedback to the user.
     * @param storage The storage component responsible for saving task data.
     * @throws OracleException If an error occurs while retrieving the task or saving to storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("OOPS! There are no tasks in the list yet. "
                                      + "Please add a task first before marking it as incomplete.");
        }
        Task task = tasks.getTask(index);
        task.markUndone();
        storage.save(tasks.getTasks());
        ui.showUnmarkedTask(task);
    }
    /**
     * Executes the mark-as-undone operation for the GUI interface.
     * This method marks the task at the specified index as incomplete and persists the updated list to storage.
     *
     * @param tasks   The task list containing the task to be marked as undone
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component to save the updated task list
     * @return A formatted string confirming the task has been marked as undone, showing the updated task
     * @throws OracleException If there is an error during task update or storage operations
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("\uD83C\uDF0C The cosmos is empty... You have no tasks in your list yet! "
                                      + "Please add a task first before marking it as incomplete.");
        }
        Task task = tasks.getTask(index);
        task.markUndone();
        storage.save(tasks.getTasks());
        return "\uD83D\uDD04 The task is undone, drifting once more in the cosmic expanse:\n" + task;
    }

}
