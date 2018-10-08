import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TimeMachineSystem {

    private final static Byte BOUNDARY_BYTE = new Byte("0");
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
        HashSet<Chunk> set = new HashSet<Chunk>();

        byte[] currBytes = new byte[0];
        int currSize = 0;
        // read up until boundary 0x30, or 48 decimal, or '0' ASCII
        for (byte b : data) {
            // found border, if not empty, add to set
            if (new Byte("48").byteValue() == b) {
                if (currBytes.length > 0) {
                    System.out.println(currBytes.length);
                }
            }
        }

        return false;
    }

    private byte[] byteListToByteArray(List<Byte> list) {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) byteArray[i] = list.get(i);
        return byteArray;
    }
}
