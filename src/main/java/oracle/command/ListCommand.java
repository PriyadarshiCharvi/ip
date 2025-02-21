package oracle.command;

import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.TaskList;

/**
 * Represents a command to list all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Executes the command by displaying all tasks in the task list.
     *
     * @param tasks   The task list containing all tasks.
     * @param ui      The UI component to display the list of tasks.
     * @param storage The storage component (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.getTasks());
    }
}
