package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;

import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Constructs a DeleteCommand with the specified task index.
     *
     * @param index The zero-based index of the task to be deleted from the task list.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command by removing the specified task from the task list,
     * saving the updated list to storage, and displaying a confirmation message to the user.
     *
     * @param tasks   The task list from which the task will be removed.
     * @param ui      The UI component to display feedback to the user.
     * @param storage The storage component responsible for saving task data.
     * @throws OracleException If an error occurs while deleting the task or saving to storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        Task removedTask = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
