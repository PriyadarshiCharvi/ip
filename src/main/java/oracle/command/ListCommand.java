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
    /**
     * Executes the list operation for the GUI interface.
     * This method displays all tasks currently in the task list in a numbered format.
     * If the task list is empty, it returns an appropriate message.
     *
     * @param tasks   The task list to display
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component (not used in this implementation)
     * @return A formatted string containing numbered list of all tasks, or a message if list is empty
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            return "\uD83C\uDF0C The cosmos is empty... You have no tasks in your list yet!";
        }
        StringBuilder response = new StringBuilder("\uD83D\uDCDD Here are the tasks in your cosmic logs:\n");
        for (int i = 0; i < tasks.size(); i++) {
            response.append((i + 1)).append(". ").append(tasks.getTasks().get(i)).append("\n");
        }
        return response.toString();
    }

}
