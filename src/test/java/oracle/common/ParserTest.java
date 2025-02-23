package oracle.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.command.AddCommand;
import oracle.command.Command;

public class ParserTest {

    @Test
    public void parse_validTodoCommand_success() throws OracleException {
        Command command = Parser.parse("todo homework");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_validDeadlineCommand_success() throws OracleException {
        Command command = Parser.parse("deadline project /by 2/12/2023 2359");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_validEventCommand_success() throws OracleException {
        Command command = Parser.parse("event meeting /from 3/12/2023 1400 /to 3/12/2023 1600");
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_unknownCommand_throwsException() {
        OracleException exception = assertThrows(OracleException.class, () -> {
            Parser.parse("unknown command");
        });
        assertEquals(
                "OOPS!!! I'm sorry, but I don't know what that means :-(. Type 'help' to see the list of commands.",
                exception.getMessage());
    }
}
