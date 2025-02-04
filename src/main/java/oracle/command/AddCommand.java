package oracle.command;

import oracle.task.Task;
import oracle.common.OracleException;
import oracle.common.Storage;
import oracle.common.Ui;

import oracle.task.TaskList;

public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }
}
