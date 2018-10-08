import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TimeMachineSystem {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;

    private ChunkParser chunkParser;
    public MetadataParser metadataParser;
    public HashMap<String, Chunk> existingChunks;

    /**
     * Constructor with dependency injection to add helper methods to Time Machine
     *
     * @param chunkParser
     * @param metadataParser
     */
    public TimeMachineSystem(ChunkParser chunkParser,
                             MetadataParser metadataParser) {
        this.chunkParser = chunkParser;
        this.metadataParser = metadataParser;

        // retrieve existing chunks to prevent saving of duplicate chunks
        try {
            this.existingChunks = this.retrieveExistingChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public TimeMachineFile open(String name, String mode) {
        TimeMachineFile file = null;

        file = new TimeMachineFile("./file" + name);

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


    public boolean storeChunks(byte[] userInputBytes) {
        HashMap<String, Chunk> newChunksMap = this.createChunks(userInputBytes);

        Collection<Chunk> newChunks = newChunksMap.values();
        try {
            this.writeChunks(newChunks);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private HashMap<String, Chunk> retrieveExistingChunks() throws IOException {
        HashMap<String, Chunk> existingChunks = new HashMap<>();
        File folder = new File("./chunk");
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                TimeMachineFile tmFile = new TimeMachineFile(f.getAbsolutePath());
                String chunkString = this.readFile(tmFile);
                if (chunkString != null && !chunkString.isEmpty()) {
                    Chunk chunk = new Chunk(f.getName(), chunkString.getBytes());
                    existingChunks.put(chunkString, chunk);
                }
            }
        }

        return existingChunks;
    }


    private HashMap<String, Chunk> createChunks(byte[] data) {
        HashMap<String, Chunk> map = new HashMap<>();

        ArrayList<Byte> currBytes = new ArrayList<>();
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII
        for (byte b : data) {
            if (b == this.BOUNDARY_BYTE.byteValue()) { // found border
                if (currBytes.size() != 0) { // finish chunk and add to currBytes
                    this.makeNewChunk(currBytes, map);
                    currBytes.clear();
                }
            }
            if (currBytes.size() < this.MAX_FILE_SIZE) {
                currBytes.add(b);
            }
        }
        if (currBytes.size() != 0) { // read and store the rest until end of array
            System.out.println("End of file");
            this.makeNewChunk(currBytes, map);
            currBytes.clear();
        }
        System.out.println("Current bytes: " + currBytes);

        return map;
    }

    private void makeNewChunk(ArrayList<Byte> bytes, Map<String, Chunk> newMap) {
        byte[] byteArr = this.chunkParser.byteListToByteArray(bytes);
        if (!this.existingChunks.containsKey(new String(byteArr))) {
            Chunk newChunk = new Chunk(byteArr);
            newMap.put(new String(byteArr), newChunk);
        }
    }

    private void writeChunks(Collection<Chunk> chunks) throws IOException {
        FileOutputStream fos = null;

        for (Chunk c : chunks) {
            try {
                File chunkFile = new File(c.path);
                fos = new FileOutputStream(chunkFile);
                System.out.println(c.content);
                fos.write(c.content);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR: Failed to write to file");
            } finally {
                fos.close();
            }
        }
    }
}
