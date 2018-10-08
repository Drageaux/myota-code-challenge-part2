import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TimeMachineSystem {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;

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


    public boolean storeChunks(byte[] data) {
        HashMap<String, Chunk> map = new HashMap<>();

        ArrayList<Byte> currBytes = new ArrayList<>();
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII
        for (byte b : data) {
            if (b == this.BOUNDARY_BYTE.byteValue()) { // found border
                if (currBytes.size() != 0) { // finish chunk and add to currBytes
                    Chunk newChunk = new Chunk(currBytes);
                    map.put(new String(String.valueOf(currBytes)), newChunk);
                    currBytes.clear();
                }
            }
            if (currBytes.size() < this.MAX_FILE_SIZE) {
                currBytes.add(b);
            }
        }
        if (currBytes.size() != 0) { // read and store the rest until end of array
            System.out.println("End of file");
            Chunk newChunk = new Chunk(currBytes);
            map.put(new String(String.valueOf(currBytes)), newChunk);
            currBytes.clear();
        }

        System.out.println("Current bytes: " + currBytes);

        return false;
    }
}
