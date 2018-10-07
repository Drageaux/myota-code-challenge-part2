import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TimeMachineSystem tms = new TimeMachineSystem();
        TimeMachineFile currentFile = null;
        boolean exit = false;

        System.out.println("Welcome to the Time Machine System!\n");

        Scanner sc = new Scanner(System.in);
        while (!exit) {
            int choice = -1;

            while (choice < 0 || choice > 5) {
                try {

                    if (currentFile == null) {
                        System.out.println("\nMAIN MENU");
                        System.out.println("1. Open file");
                        System.out.println("0. Exit program");
                    } else {
                        System.out.print("\nMAIN MENU");
                        System.out.println(" (current file: " + currentFile.getName() + ")");

                        System.out.println("1. Open file");
                        System.out.println("2. Create file");
                        System.out.println("3. Read file data at");
                        System.out.println("4. Restore file version");
                        System.out.println("5. Close file");
                        System.out.println("0. Exit program");
                    }
                    System.out.print("Enter your command: ");
                    choice = Integer.parseInt(sc.nextLine()); // already throws
                } catch (NumberFormatException e) {
                    System.out.println("Invalid command. Please try again (0-3).\n");
                }
                if (choice == 0) { // exit
                    System.out.println("Thank you for using the Multi-Parent Directory System!");
                    System.out.println("See you again!");
                    exit = true;
                }

                if (currentFile == null) {
                    switch (choice) {
                        default:
                            break;
                        case (1):
                            String readFile = null;
                            while (readFile == null || readFile..isEmpty()) {
                                System.out.print("Enter file name: ");
                                readFile = sc.nextLine();
                                System.out.println("Invalid input (please enter a file name)");
                            }
                            currentFile = tms.open(readFile, "read");
                            break;
                    }
                } else {
                    switch (choice) {

                        case (2):

                            break;
                        case (3):
                            break;
                        case (4):
                            break;
                        case (5):
                            currentFile.close();
                            currentFile = null;
                            break;
                    }
                }
            }

            System.out.print("Press Enter/Return to continue...");
            String input = sc.nextLine();
            System.out.println("===================================================\n");
        }
    }
}
