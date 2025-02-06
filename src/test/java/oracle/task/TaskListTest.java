package oracle.task;

import oracle.common.OracleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {
    @Test
    public void deleteTask_validIndex_success() throws OracleException {
        TaskList taskList = new TaskList();
        Task todo = new Todo("Test Task");
        taskList.addTask(todo);
        assertEquals(1, taskList.size());

        Task removedTask = taskList.deleteTask(0);
        assertEquals("[T][ ] Test Task", removedTask.toString());
        assertEquals(0, taskList.size());
    }

    @Test
    public void deleteTask_invalidIndex_exceptionThrown() {
        TaskList taskList = new TaskList();
        OracleException exception = assertThrows(OracleException.class, () -> {
            taskList.deleteTask(0);
        });
        assertEquals("There are no tasks to delete.", exception.getMessage());
    }

    @Test
    public void addTask_listSizeIncreases() throws OracleException {
        TaskList taskList = new TaskList();
        Task task = new Todo("Buy groceries");
        taskList.addTask(task);

        assertEquals(1, taskList.size());
        assertEquals("[T][ ] Buy groceries", taskList.getTask(0).toString());
    }
}
