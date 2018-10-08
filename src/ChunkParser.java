import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ChunkParser {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;

    public HashMap<String, Chunk> existingChunks;

    public ChunkParser() {
        // retrieve existing chunks to prevent saving of duplicate chunks
        try {
            this.existingChunks = this.retrieveExistingChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**********
     * HELPER *
     **********/
    public static byte[] byteListToByteArray(List<Byte> list) {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) byteArray[i] = list.get(i);
        return byteArray;
    }

    public static byte[] byteClassListToPrimitiveByteArray(Byte[] list) {
        byte[] byteArray = new byte[list.length];
        for (int i = 0; i < list.length; i++) byteArray[i] = list[i];
        return byteArray;
    }


    /********
     * MAIN *
     ********/

    /**
     * Read from /chunk folder and pre-populate a HashMap all existing chunks.
     *
     * @return HashMap
     * @throws IOException
     */
    public HashMap<String, Chunk> retrieveExistingChunks() throws IOException {
        HashMap<String, Chunk> existingChunks = new HashMap<>();
        File folder = new File("./chunk");
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                TimeMachineFile tmFile = new TimeMachineFile(f.getAbsolutePath());
                String chunkString = Helper.readFile(tmFile);
                if (chunkString != null && !chunkString.isEmpty()) {
                    Chunk chunk = new Chunk(f.getName(), chunkString.getBytes());
                    existingChunks.put(chunkString, chunk);
                }
            }
        }

        return existingChunks;
    }


    /**
     * Make Collection of Chunks, then for each Chunk in a Collection,
     * write a chunk file into the /chunk folder.
     * NOTE: not checking duplicates
     * TODO: retrieve existingChunks and check against duplicates
     *
     * @param userInputBytes
     * @throws IOException
     */
    public void writeChunksToFiles(byte[] userInputBytes) throws IOException {
        HashMap<String, Chunk> newChunksMap = this.createNewChunksMap(userInputBytes);
        Collection<Chunk> newChunks = newChunksMap.values();
        FileOutputStream fos = null;

        for (Chunk c : newChunks) {
            try {
                File chunkFile = new File(c.path);
                fos = new FileOutputStream(chunkFile);
                fos.write(c.content);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR: Failed to write to file");
            } finally {
                fos.close();
            }
        }
    }


    /**
     * Parse a byte array and populate a HashMap with new Chunks only.
     *
     * @param data - an array of bytes, likely from a plain text file
     * @return
     */
    private HashMap<String, Chunk> createNewChunksMap(byte[] data) {
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
//            System.out.println("End of file");
            this.makeNewChunk(currBytes, map);
            currBytes.clear();
        }
        System.out.println("Read all bytes!");

        return map;
    }


    /**
     * Add bytes to new map if bytes pattern is not already stored in a Chunk.
     *
     * @param bytes  - bytes to add
     * @param newMap - map to add if bytes pattern is not already stored in existingMap
     */
    private void makeNewChunk(ArrayList<Byte> bytes, Map<String, Chunk> newMap) {
        byte[] byteArr = this.byteListToByteArray(bytes);
        if (!this.existingChunks.containsKey(new String(byteArr))) {
            Chunk newChunk = new Chunk(byteArr);
            newMap.put(new String(byteArr), newChunk);
        }
    }

}
