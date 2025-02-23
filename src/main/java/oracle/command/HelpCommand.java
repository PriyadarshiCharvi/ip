package oracle.command;

import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.TaskList;

/**
 * Represents a command to display all available commands and their descriptions.
 */
public class HelpCommand extends Command {

    /**
     * Executes the help command in CLI mode by displaying all available commands.
     *
     * @param tasks   The task list (not used in this command).
     * @param ui      The UI component to display the help message.
     * @param storage The storage component (not used in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String helpMessage = getHelpMessage();
        ui.showHelpMessage(helpMessage);
    }

    /**
     * Executes the help command in GUI mode by returning a formatted help message.
     *
     * @param tasks   The task list (not used in this command).
     * @param ui      The UI component (not used in this command).
     * @param storage The storage component (not used in this command).
     * @return A formatted string containing the list of commands.
     */
    @Override
    public String executeForGui(TaskList tasks, Ui ui, Storage storage) {
        return getHelpMessage();
    }

    /**
     * Generates the help message containing all available commands.
     *
     * @return A formatted help message.
     */
    private String getHelpMessage() {
        return "🪐 Oracle Command Guide 🪐\n\n"
               + "1. list: Shows all tasks in your list\n"
               + "2. todo [task description]: Adds a To-Do task\n"
               + "3. deadline [task description] /by [date time]: Adds a Deadline task\n"
               + "4. event [task description] /from [date time] /to [date time]: Adds an Event task\n"
               + "5. delete [task number]: Deletes a task\n"
               + "6. mark [task number]: Marks a task as completed\n"
               + "7. unmark [task number]: Marks a task as not completed\n"
               + "8. find [keyword]: Finds tasks containing a specific keyword\n"
               + "9. snooze [task number] [new date time]: Reschedules a deadline or event\n"
               + "10. bye: Exits the application";
    }
}
