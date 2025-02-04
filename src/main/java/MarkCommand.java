class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        Task task = tasks.getTask(index);
        task.markDone();
        storage.save(tasks.getTasks());
        ui.showMarkedTask(task);
    }
}