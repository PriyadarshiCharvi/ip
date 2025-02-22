package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to add a new task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Constructs an AddCommand with the specified task.
     *
     * @param task The task to be added.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Executes the command by adding the task to the task list, saving the updated list to storage,
     * and displaying a confirmation message to the user.
     *
     * @param tasks   The task list where the new task will be added.
     * @param ui      The UI component to display feedback to the user.
     * @param storage The storage component responsible for saving task data.
     * @throws OracleException If an error occurs while saving the task to storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }

    /**
     * Executes the add task operation for the GUI interface.
     * This method adds a new task to the task list and persists the updated list to storage.
     *
     * @param tasks   The task list to add the new task to
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component to save the updated task list
     * @return A formatted string confirming the task addition, showing the added task and updated task count
     * @throws OracleException If there is an error during task addition or storage operations
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        return "Added task:\n" + task + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

}
