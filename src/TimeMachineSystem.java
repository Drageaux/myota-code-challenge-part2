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
    private final static Byte EOF_BYTE = new Byte("0");

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
        // or EOF, or 0 decimal, or 'EOF' ASCII
        for (byte b : data) {
            if (b == BOUNDARY_BYTE.byteValue()) { // found border
                if (currBytes.size() == 0) { // if empty, add to currBytes
                    System.out.println("First chunk");
                } else { // else, finish chunk and add to currBytes
                    Chunk newChunk = new Chunk(currBytes);
                    map.put(new String(String.valueOf(currBytes)), newChunk);

                    currBytes.clear();
                }
            } else if (b == EOF_BYTE.byteValue()) { // found end of file
                System.out.println("End of file");
                Chunk newChunk = new Chunk(currBytes);
                map.put(new String(String.valueOf(currBytes)), newChunk);

                currBytes.clear();
                break;
            }

            if (currBytes.size() <= 32) {
                currBytes.add(b);
            }
        }
        System.out.println("Current bytes: " + currBytes);

        return false;
    }
}
