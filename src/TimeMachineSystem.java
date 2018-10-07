import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TimeMachineSystem {

    private TimeMachineFile exampleFile;


    public TimeMachineSystem() {
        this.exampleFile = this.open("test", "write");
    }

    public TimeMachineFile open(String name, String mode) {
        TimeMachineFile file = null;

        file = new TimeMachineFile("./file/" + name);

        if (mode == "read" && file.exists()) {
            try {
                System.out.println(this.readFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    private String readFile(TimeMachineFile file) throws IOException {
        String result = "";

        FileReader reader = null;
        reader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        result = new String(chars);

        return result;
    }

}
