package oracle.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void markDone_taskIsMarkedCorrectly() {
        Task todo = new Todo("Read book");
        todo.markDone();
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void markUndone_taskIsUnmarkedCorrectly() {
        Task todo = new Todo("Read book");
        todo.markDone();
        todo.markUndone();
        assertEquals(" ", todo.getStatusIcon());
    }
}
