class ExitCommand extends Command {
    @Override
    void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    boolean isExit() {
        return true;
    }
}