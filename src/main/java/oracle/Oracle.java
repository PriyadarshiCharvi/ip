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
     * Runs the chatbot, handles user commands in a loop until an exit command is issued.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            isExit = processCommand(input);
        }
        ui.close();
    }

    private boolean processCommand(String input) {
        try {
            Command command = Parser.parse(input);
            command.execute(tasks, ui, storage);
            return command.isExit();
        } catch (OracleException e) {
            ui.showError(e.getMessage());
            return false;
        }
    }

    public String getResponse(String input) {
        try {
            return Parser.parse(input).executeForGui(tasks, ui, storage);
        } catch (OracleException e) {
            return "Error: " + e.getMessage();
        }
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
