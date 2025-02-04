import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task deleteTask(int index) throws OracleException {
        if (tasks.isEmpty()) {
            throw new OracleException("There are no tasks to delete.");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new OracleException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        return tasks.remove(index);
    }

    public Task getTask(int index) throws OracleException {
        if (index < 0 || index >= tasks.size()) {
            throw new OracleException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        return tasks.get(index);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
