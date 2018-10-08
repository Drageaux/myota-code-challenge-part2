import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TimeMachineSystemTest {

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }


    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static Byte EOF_BYTE = new Byte("0");

    @Test
    void testCreateChunks() {
        TimeMachineFile file = null;
        file = new TimeMachineFile("./file/" + "test");

        HashMap<String, Chunk> map = new HashMap<>();

        byte[] data = new byte[32];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fis.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Byte> currBytes = new ArrayList<>();
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII
        for (byte b : data) {
            if (b == this.BOUNDARY_BYTE.byteValue()) { // found border
                if (currBytes.size() != 0) { // finish chunk and add to currBytes
                    Chunk newChunk = new Chunk(ChunkParser.byteListToByteArray(currBytes));
                    map.put(new String(String.valueOf(currBytes)), newChunk);
                    currBytes.clear();
                }
            }
            if (currBytes.size() < 32) {
                currBytes.add(b);
            }
        }
        if (currBytes.size() != 0) { // read and store the rest until end of array
            System.out.println("End of file");
            Chunk newChunk = new Chunk(ChunkParser.byteListToByteArray(currBytes));
            map.put(new String(String.valueOf(currBytes)), newChunk);
            currBytes.clear();
        }
        System.out.println("Current bytes: " + currBytes);
    }

}