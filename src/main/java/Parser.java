public class Parser {
    public static Command parse(String input) throws OracleException {
        String trimmedInput = input.trim();
        if (trimmedInput.equals("list")) {
            return new ListCommand();
        } else if (trimmedInput.equals("bye")) {
            return new ExitCommand();
        } else if (trimmedInput.startsWith("todo")) {
            return parseTodoCommand(trimmedInput);
        } else if (trimmedInput.startsWith("deadline")) {
            return parseDeadlineCommand(trimmedInput);
        } else if (trimmedInput.startsWith("event")) {
            return parseEventCommand(trimmedInput);
        } else if (trimmedInput.startsWith("delete")) {
            return parseDeleteCommand(trimmedInput);
        } else if (trimmedInput.startsWith("mark")) {
            return parseMarkCommand(trimmedInput);
        } else if (trimmedInput.startsWith("unmark")) {
            return parseUnmarkCommand(trimmedInput);
        } else {
            throw new OracleException("OOPS!!! I'm sorry, but I don't know what that means :-(. Try something like 'todo assignment'.");
        }
    }

    private static Command parseTodoCommand(String input) throws OracleException {
        if (input.length() <= 5) {
            throw new OracleException("OOPS!!! The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        return new AddCommand(new Todo(description));
    }

    private static Command parseDeadlineCommand(String input) throws OracleException {
        String[] parts = input.substring(8).split("/by", 2);
        if (parts.length < 2) {
            throw new OracleException(
                    "The correct format for deadline is: deadline [description] /by [date time]\n" +
                            "    For example: deadline assignment /by 2/12/2023 2359"
            );
        }
        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new OracleException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new OracleException(
                    "The deadline time must be provided.\n" +
                            "    Format: deadline [description] /by [date time]\n" +
                            "    For example: deadline assignment /by 2/12/2023 2359"
            );
        }

        return new AddCommand(new Deadline(description, by));
    }

    private static Command parseEventCommand(String input) throws OracleException {
        String[] parts = input.substring(5).split("/from|/to", 3);
        if (parts.length < 3) {
            throw new OracleException(
                    "The correct format for event is: event [description] /from [date time] /to [date time]\n" +
                            "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500"
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
                    "Both start and end times must be provided.\n" +
                            "    Format: event [description] /from [date time] /to [date time]\n" +
                            "    For example: event meeting /from 2/12/2023 1400 /to 2/12/2023 1500"
            );
        }

        return new AddCommand(new Event(description, from, to));
    }

    private static Command parseDeleteCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }

    private static Command parseMarkCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(5).trim()) - 1;
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }

    private static Command parseUnmarkCommand(String input) throws OracleException {
        try {
            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            throw new OracleException("Please enter a valid task number.");
        }
    }
}
