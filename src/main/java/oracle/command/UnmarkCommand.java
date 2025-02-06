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
        Task task = tasks.getTask(index);
        task.markUndone();
        storage.save(tasks.getTasks());
        ui.showUnmarkedTask(task);
    }
}