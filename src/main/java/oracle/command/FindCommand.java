package oracle.command;

import java.util.List;

import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Task;
import oracle.task.TaskList;

/**
 * Represents a command to find tasks containing a specific keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the given keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the command by filtering tasks that contain the keyword
     * and displaying the matching results to the user.
     *
     * @param tasks   The task list where tasks are searched.
     * @param ui      The UI component to display results.
     * @param storage The storage component (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }
    /**
     * Executes the find operation for the GUI interface.
     * This method searches through the task list for tasks containing the specified keyword
     * and returns a formatted list of all matching tasks.
     *
     * @param tasks   The task list to search through
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component (not used in this implementation)
     * @return A formatted string containing numbered list of all matching tasks, or a message if no matches found
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
        }
        return response.toString();
    }

}
