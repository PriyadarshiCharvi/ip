package oracle.common;

import oracle.command.AddCommand;
import oracle.command.Command;
import oracle.command.DeleteCommand;
import oracle.command.ExitCommand;
import oracle.command.FindCommand;
import oracle.command.HelpCommand;
import oracle.command.ListCommand;
import oracle.command.MarkCommand;
import oracle.command.SnoozeCommand;
import oracle.command.UnmarkCommand;
import oracle.task.Deadline;
import oracle.task.Event;
import oracle.task.Todo;

/**
 * Parses user input and converts it into executable commands.
 */
public class Parser {
    /**
     * Parses the user input string and returns the corresponding command.
     *
     * @param input The raw user input string.
     * @return The appropriate {@code Command} based on the input.
     * @throws OracleException If the input is invalid or unrecognized.
     */
    public static Command parse(String input) throws OracleException {
        assert input != null : "Input command should not be null";
        assert !input.trim().isEmpty() : "Input command should not be empty";
        String trimmedInput = input.trim();
        switch (trimmedInput.split(" ")[0]) {
        case "list":
            return new ListCommand();
        case "bye":
            return new ExitCommand();
        case "todo":
            return parseTodoCommand(trimmedInput);
        case "deadline":
            return parseDeadlineCommand(trimmedInput);
        case "event":
            return parseEventCommand(trimmedInput);
        case "delete":
            return parseDeleteCommand(trimmedInput);
        case "mark":
            return parseMarkCommand(trimmedInput);
        case "unmark":
            return parseUnmarkCommand(trimmedInput);
        case "find":
            return new FindCommand(trimmedInput.substring(5).trim());
        case "snooze":
            return parseSnoozeCommand(trimmedInput);
        case "help":
            return new HelpCommand();
        default:
            throw new OracleException(
                    "OOPS!!! I'm sorry, but I don't know what that means :-(. Type 'help' to see the list of commands."
            );
        }
    }

    /**
     * Parses a todo command and returns an {@code AddCommand}.
     *
     * @param input The user input string.
     * @return An {@code AddCommand} that adds a todo task.
     * @throws OracleException If the description is missing.
     */
    private static Command parseTodoCommand(String input) throws OracleException {
        if (input.length() <= 5) {
            throw new OracleException("OOPS!!! The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        return new AddCommand(new Todo(description));
    }

    /**
     * Parses a deadline command and returns an {@code AddCommand}.
     *
     * @param input The user input string.
     * @return An {@code AddCommand} that adds a deadline task.
     * @throws OracleException If the format is incorrect or the description is missing.
     */
    private static Command parseDeadlineCommand(String input) throws OracleException {
        String trimmedInput = input.trim();
        String commandWithoutPrefix = trimmedInput.substring("deadline".length()).trim();
        String[] parts = commandWithoutPrefix.split("/by", 2);

        if (parts.length < 2) {
            throw new OracleException(
                    "The correct format for deadline is: deadline [description] /by [date time]\n"
                    + "    For example: deadline assignment /by 2/12/2023 2359"
            );
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new OracleException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new OracleException(
                    "The deadline time must be provided.\n"
                    + "    Format: deadline [description] /by [date time]\n"
                    + "    For example: deadline assignment /by 2/12/2023 2359"
            );
        }

        return new AddCommand(new Deadline(description, by));
    }

    /**
     * Parses an event command and returns an {@code AddCommand}.
     *
     * @param input The user input string.
     * @return An {@code AddCommand} that adds an event task.
     * @throws OracleException If the format is incorrect or required fields are missing.
     */
    private static Command parseEventCommand(String input) throws OracleException {
        String[] parts = input.substring(5).split("/from|/to", 3);
        if (parts.length < 3) {
            throw new OracleException(
                    "The correct format for event is: event [description] /from [date time] /to [date time]\n"
                            + "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500"
            );
        }
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();

        if (description.isEmpty()) {
            throw new OracleException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new OracleException(
                    "Both start and end times must be provided.\n"
                            + "    Format: event [description] /from [date time] /to [date time]\n"
                            + "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500"
            );
        }

        return new AddCommand(new Event(description, from, to));
    }

    /**
     * Parses a delete command and returns a {@code DeleteCommand}.
     *
     * @param input The user input string.
     * @return A {@code DeleteCommand} that removes a task.
     * @throws OracleException If the task index is invalid.
     */
    private static Command parseDeleteCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }

    /**
     * Parses a mark command and returns a {@code MarkCommand}.
     *
     * @param input The user input string.
     * @return A {@code MarkCommand} that marks a task as done.
     * @throws OracleException If the task index is invalid.
     */
    private static Command parseMarkCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(5).trim()) - 1;
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }

    /**
     * Parses an unmark command and returns an {@code UnmarkCommand}.
     *
     * @param input The user input string.
     * @return An {@code UnmarkCommand} that marks a task as not done.
     * @throws OracleException If the task index is invalid.
     */
    private static Command parseUnmarkCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }

    /**
     * Parses a snooze command and returns a {@code SnoozeCommand}.
     *
     * @param input The user input string.
     * @return A {@code SnoozeCommand} to postpone the task.
     * @throws OracleException If the format is incorrect or the index is invalid.
     */
    private static Command parseSnoozeCommand(String input) throws OracleException {
        String[] parts = input.split(" ", 3);
        if (parts.length < 3) {
            throw new OracleException("The correct format for snoozing a task is:\n"
                    + "snooze [task number] [new date time]\n"
                                      + "Example: snooze 2 5/12/2023 1800");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            String newDateTime = parts[2].trim();
            return new SnoozeCommand(index, newDateTime);
        } catch (NumberFormatException e) {
            throw new OracleException("Invalid task number. Use a valid integer.");
        }
    }
}
