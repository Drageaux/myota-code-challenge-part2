import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TimeMachineSystem tms = new TimeMachineSystem(new MetadataParser());
        TimeMachineFile currentFile = null;
        String readWriteMode = "";
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
                        System.out.print(" (current file: \"" + currentFile.getName() + "\"");
                        if (readWriteMode == "read") {
                            System.out.println(", read-only mode)");
                            System.out.println("3. Read all file data (offset 0, length 32)");
                            System.out.println("4. Restore file version");
                            System.out.println("5. Close file");
                            System.out.println("0. Exit program");
                        } else if (readWriteMode == "write") {
                            System.out.println(", writable mode)");
                            System.out.println("2. Write file (flush chunks and metadata if written successful)");
                            System.out.println("3. Read all file data (offset 0, length 32");
                            System.out.println("4. Restore file version");
                            System.out.println("5. Close file");
                            System.out.println("0. Exit program");
                        } else {
                            System.out.println(")");
                        }
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
                            while (readFile == null || readFile.isEmpty()) {
                                System.out.print("Enter file name: ");
                                readFile = sc.nextLine();
                                if (readFile == null || readFile.isEmpty()) {
                                    System.out.println("Invalid input (please enter a file name)");
                                }
                            }
                            int mode = -1;
                            while (mode < 0 || mode > 2) {
                                System.out.print("Read or write mode? (1.read  2.write  0.cancel): ");
                                try {
                                    mode = Integer.parseInt(sc.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input");
                                }

                            }
                            if (mode == 0) break;
                            else if (mode == 1) readWriteMode = "read";
                            else if (mode == 2) readWriteMode = "write";

                            currentFile = tms.open(readFile, readWriteMode);
                            break;
                    }
                } else { // if system is currently reading a file
                    switch (choice) {
                        default:
                            break;
                        case (1):
                            System.out.println("ERROR: File already opened. Close file first to open another one");
                            break;
                        case (2):
                            if (readWriteMode == "write") {
                                // write by data and not just text
                                String output = null;
                                while (output == null || output.isEmpty()) {
                                    System.out.print("Enter your content in regular text: ");
                                    output = sc.nextLine();
                                }
                                byte[] bytes = output.getBytes();
                                try {
                                    int lengthDataWritten = currentFile.writeAt(bytes, 0);
                                    // only store chunks and generate metadata if write process successful
                                    if (lengthDataWritten != -1) {
                                        currentFile.flush(bytes);
                                        System.out.println(lengthDataWritten + " bytes written");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("ERROR: You don't have write access");
                            }
                            break;
                        case (3):

                            byte[] bytes = currentFile.readAt(0, 32);
                            if (bytes == null) break;
                            System.out.print("In hex: ");
                            for (byte b : bytes) {
                                System.out.printf("%02x ", b);
                            }
                            System.out.println("");

                            System.out.print("In ASCII: ");
                            for (byte b : bytes) {
                                System.out.print(b + " ");
                            }
                            System.out.println("");
                            break;
                        case (4):
                            int version = -1;
                            while (version < 0 || version > 3) {
                                System.out.println("1. Keep current version");
                                System.out.println("2. Restore last version");
                                System.out.println("3. Restore oldest version");
                                version = Integer.parseInt(sc.nextLine());
                            }
                            if (version == 0) {
                                break;
                            }
                            switch (version) {
                                default:
                                    break;
                                case (1):
                                    currentFile.restoreFromVersion("CURR");
                                    break;
                                case (2):
                                    currentFile.restoreFromVersion("LAST");
                                    break;
                                case (3):
                                    currentFile.restoreFromVersion("OLDEST");
                                    break;
                            }
                            break;
                        case (5):
                            currentFile.close();
                            currentFile = null;
                            readWriteMode = "";
                            break;
                    }
                }
            }

//            System.out.print("Press Enter/Return to continue...");
//            String input = sc.nextLine();
            System.out.println("===================================================\n");
        }
    }
}
