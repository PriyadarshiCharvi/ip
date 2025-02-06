package oracle;

import oracle.command.Command;

import oracle.common.OracleException;
import oracle.common.Parser;
import oracle.common.Storage;
import oracle.common.Ui;

import oracle.task.TaskList;

/**
 * The main class for the Oracle application.
 * Handles initialization, user interactions, and execution of commands.
 */
public class Oracle {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs an Oracle chatbot instance with a specified storage file.
     *
     * @param filePath The file path where task data is stored.
     */
    public Oracle(String filePath) {
        TaskList tasks1;
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks1 = new TaskList(storage.load());
        } catch (OracleException e) {
            ui.showLoadingError();
            tasks1 = new TaskList();
        }
        tasks = tasks1;
    }

    /**
     * Runs the chatbot, handling user commands in a loop until an exit command is issued.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (OracleException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * The entry point of the Oracle chatbot application.
     * Initializes and starts the chatbot with a predefined storage location.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Oracle("data/oracle.txt").run();
    }
}
