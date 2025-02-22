package oracle.command;

import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.TaskList;

/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {
    /**
     * Executes the command by displaying a goodbye message to the user.
     *
     * @param tasks   The task list (not modified in this command).
     * @param ui      The UI component to display the goodbye message.
     * @param storage The storage component (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Executes the exit operation for the GUI interface.
     * This method returns a farewell message to be displayed before the application closes.
     *
     * @param tasks   The task list (not used in this implementation)
     * @param ui      The UI component (not used in this implementation)
     * @param storage The storage component (not used in this implementation)
     * @return A goodbye message to the user
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) {
        return "Goodbye! Hope to see you again soon!";
    }


    /**
     * Indicates that this command signals the program to exit.
     *
     * @return {@code true}, indicating that the program should terminate.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
