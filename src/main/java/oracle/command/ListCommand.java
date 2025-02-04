package oracle.command;

import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.TaskList;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.getTasks());
    }
}