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
     * Indicates that this command signals the program to exit.
     *
     * @return {@code true}, indicating that the program should terminate.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}