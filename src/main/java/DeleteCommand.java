class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        Task removedTask = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
