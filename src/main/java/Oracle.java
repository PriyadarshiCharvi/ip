import java.util.Scanner;

public class Oracle {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Oracle");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while(true) {
            String input = in.nextLine();

            if (input.equals("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________");
                break;
            }

            System.out.println("    ____________________________________________________________");
            System.out.println("    " + input);
            System.out.println("    ____________________________________________________________");

        }
        in.close();
    }
}
