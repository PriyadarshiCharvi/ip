package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to mark a task as completed in the task list.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Constructs a MarkCommand with the specified task index.
     *
     * @param index The zero-based index of the task to be marked as done.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command by marking the specified task as done, saving the updated task list to storage,
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
                                      + "Please add a task first before marking it as complete.");
        }
        Task task = tasks.getTask(index);
        task.markDone();
        storage.save(tasks.getTasks());
        ui.showMarkedTask(task);
    }
    /**
     * Executes the mark-as-done operation for the GUI interface.
     * This method marks the task at the specified index as completed and persists the updated list to storage.
     *
     * @param tasks   The task list containing the task to be marked as done
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component to save the updated task list
     * @return A formatted string confirming the task has been marked as done, showing the updated task
     * @throws OracleException If there is an error during task update or storage operations
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("\uD83C\uDF0C The cosmos is empty... You have no tasks in your list yet! "
                                      + "Please add a task first before marking it as complete.");
        }
        Task task = tasks.getTask(index);
        task.markDone();
        storage.save(tasks.getTasks());
        return "✨Task accomplished! Another star ignites in your constellation:\n" + task;
    }

}
