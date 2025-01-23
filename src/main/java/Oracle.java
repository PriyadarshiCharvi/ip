import java.util.Scanner;

public class Oracle {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;

        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while(true) {
            String input = in.nextLine();

            if (input.equals("list")) {
                if (taskCount == 0) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("    There are no tasks in your list yet.");
                    System.out.println("    ____________________________________________________________");
                } else {
                    System.out.println("    ____________________________________________________________");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("    " + (i + 1) + ". " + tasks[i]);
                    }
                    System.out.println("    ____________________________________________________________");
                }
            } else if (input.equals("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________");
                break;
            } else {
                tasks[taskCount] = input;
                System.out.println("    ____________________________________________________________");
                System.out.println("    added: " + input);
                System.out.println("    ____________________________________________________________");
                taskCount++;
            }
        }
        in.close();
    }
}
