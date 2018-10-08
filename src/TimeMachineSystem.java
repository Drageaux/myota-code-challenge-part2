import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TimeMachineSystem {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;

    public MetadataParser metadataParser;

    /**
     * Constructor with dependency injection to add helper methods to Time Machine
     *
     * @param metadataParser
     */
    public TimeMachineSystem(MetadataParser metadataParser) {
        this.metadataParser = metadataParser;
    }


    /**
     * Return a file. Print file if file exists.
     *
     * @param name
     * @param mode
     * @return
     */
    public static TimeMachineFile open(String name, String mode) {
        TimeMachineFile file = null;

        file = new TimeMachineFile("./file/" + name);

        if (file.exists()) {
            try {
                System.out.println(Helper.readFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
