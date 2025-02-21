package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.TaskList;

/**
 * Represents an abstract command that can be executed within the chatbot.
 * All specific commands must extend this class and implement the execute method.
 */
public abstract class Command {
    /**
     * Executes the command by performing an operation on the task list,
     * updating the UI, and optionally modifying storage.
     *
     * @param tasks   The task list to be modified.
     * @param ui      The UI component for displaying messages.
     * @param storage The storage component for saving task data.
     * @throws OracleException If an error occurs during execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException;

    /**
     * Determines if this command signals the program to exit.
     * By default, returns {@code false}. Commands like {@code ExitCommand} will override this.
     *
     * @return {@code true} if the command is an exit command, otherwise {@code false}.
     */
    public boolean isExit() {
        return false;
    }
}
