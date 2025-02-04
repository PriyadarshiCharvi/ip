class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    void execute(TaskList tasks, Ui ui, Storage storage) throws OracleException {
        Task task = tasks.getTask(index);
        task.markUndone();
        storage.save(tasks.getTasks());
        ui.showUnmarkedTask(task);
    }
}