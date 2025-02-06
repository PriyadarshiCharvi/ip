package oracle.command;

import oracle.common.Ui;
import oracle.common.Storage;
import oracle.task.Task;
import oracle.task.TaskList;
import java.util.ArrayList;

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
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }
}
