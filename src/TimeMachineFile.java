import java.io.*;
import java.nio.file.Files;

public class TimeMachineFile extends File {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;

    public TimeMachineFile(String pathname) {
        super(pathname);
    }


    /**
     * @param data
     * @param offset
     * @return
     * @throws IOException
     */
    public int writeAt(byte[] data, int offset) throws IOException {
        int n = data.length;
        // prevent offset + length > max file size to simplify return value
        // otherwise, if offset was too high, we would need to manually count the actual length of bytes written
        if (n + offset > MAX_FILE_SIZE) {
            System.out.println("ERROR: Offset (" + offset + ") and length to write (" + n + ") is over the max file size allowed (32 bytes)");
            return -1;
        }

        try {
            this.fos = new FileOutputStream(this);
            fos.write(data, offset, n);
            return n;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to write to file");
            return -1;
        } finally {
            fos.close();
        }
    }


    /**
     * @param offset
     * @param n
     * @return
     * @throws IOException
     */
    public byte[] readAt(int offset, int n) throws IOException {
        if (offset + n > MAX_FILE_SIZE) {
            System.out.println("ERROR: Offset (" + offset + ") and length to read (" + n + ") is over the max file size allowed (32 bytes)");
            return null;
        }

        try {
            this.fis = new FileInputStream(this);
            byte[] data = new byte[32];
            // read into the byte array
            this.fis.read(data, offset, n);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            fis.close();
        }
    }


    /**
     *
     */
    public void close() {
        try {
            // close all streams
            if (this.fis != null) this.fis.close();
            if (this.fos != null) this.fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
