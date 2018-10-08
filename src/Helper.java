import java.io.FileReader;
import java.io.IOException;

public class Helper {

    public static String readFile(TimeMachineFile file) throws IOException {
        String result = "";

        FileReader reader = null;
        reader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        result = new String(chars);

        return result;
    }
}
