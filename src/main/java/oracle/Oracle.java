package oracle;

import oracle.command.Command;

import oracle.common.OracleException;
import oracle.common.Parser;
import oracle.common.Storage;
import oracle.common.Ui;

import oracle.task.TaskList;


public class Oracle {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

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

    public static void main(String[] args) {
        new Oracle("data/oracle.txt").run();
    }
}
