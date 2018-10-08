import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TimeMachineSystem {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;

    private HashMap<String, Chunk> existingChunks = new HashMap<>();

    public TimeMachineSystem() {
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


    public boolean storeChunks(byte[] bytes) {
        HashMap<String, Chunk> newChunks = this.createChunks(bytes);
        return false;
    }


    private HashMap<String, Chunk> createChunks(byte[] data) {
        HashMap<String, Chunk> map = new HashMap<>();

        ArrayList<Byte> currBytes = new ArrayList<>();
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII
        for (byte b : data) {
            if (b == this.BOUNDARY_BYTE.byteValue()) { // found border
                if (currBytes.size() != 0) { // finish chunk and add to currBytes
                    this.storeNewChunk(currBytes, map);
                    currBytes.clear();
                }
            }
            if (currBytes.size() < this.MAX_FILE_SIZE) {
                currBytes.add(b);
            }
        }
        if (currBytes.size() != 0) { // read and store the rest until end of array
            System.out.println("End of file");
            this.storeNewChunk(currBytes, map);
            currBytes.clear();
        }
        System.out.println("Current bytes: " + currBytes);

        return map;
    }

    private void storeNewChunk(ArrayList<Byte> bytes, Map<String, Chunk> newMap) {
        if (!this.existingChunks.containsKey(bytes)) {
            Chunk newChunk = new Chunk(bytes);
            newMap.put(new String(String.valueOf(bytes)), newChunk);
        }
    }
}
