import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TimeMachineSystemTest {

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testStoreChunks() {
        TimeMachineFile file = null;
        file = new TimeMachineFile("./file/" + "test");

        HashSet<Chunk> set = new HashSet<Chunk>();

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
        int currSize = 0;
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII

        for (byte b : data) {
            // found border
            if (new Byte("48").byteValue() == b) {
                if (currBytes.size() == 0) { // if empty, add to currBytes
                    System.out.println("is empty");
                } else { // else, finish chunk and add to currBytes

                    Chunk newChunk = new Chunk(this.byteListToByteArray(currBytes));
                    System.out.println("new chunk: " + newChunk);
                    currBytes.clear();
                }
            }
            if (currBytes.size() <= 32) {
                currBytes.add(b);
            }
        }
        System.out.println(currBytes);
    }


    byte[] byteListToByteArray(List<Byte> list) {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) byteArray[i] = list.get(i);
        return byteArray;
    }
}