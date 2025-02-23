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
        if (tasks.isEmpty()) {
            throw new OracleException("OOPS! There are no tasks in the list yet. "
                                      + "Please add a task first before attempting to delete one.");
        }
        Task removedTask = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.showDeletedTask(removedTask, tasks.size());
    }
    /**
     * Executes the remove task operation for the GUI interface.
     * This method removes a task at the specified index from the task list and persists the updated list to storage.
     *
     * @param tasks   The task list to remove the task from
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component to save the updated task list
     * @return A formatted string confirming the task removal, showing the removed task and updated task count
     * @throws OracleException If there is an error during task removal or storage operations
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("\uD83C\uDF0C The cosmos is empty... You have no tasks in your list yet! "
                                      + "Please add a task first before attempting to delete one.");
        }
        Task removedTask = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        return "☄\uFE0F The task has been obliterated into the void. Farewell, "
               + removedTask + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

}
