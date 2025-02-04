package oracle.command;

import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;
import oracle.task.Task;
import oracle.task.TaskList;

public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        Task task = tasks.getTask(index);
        task.markDone();
        storage.save(tasks.getTasks());
        ui.showMarkedTask(task);
    }
}