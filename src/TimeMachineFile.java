import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class TimeMachineFile extends File {

    private final static Byte BOUNDARY_BYTE = new Byte("48");
    private final static int MAX_FILE_SIZE = 32;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;

    private int version = 0;

    /**
     * Constructor for File related to Time Machine System.
     * FIXME: should only be whatever is in the /file folder
     *
     * @param pathname
     */
    public TimeMachineFile(String pathname) {
        super(pathname);
    }


    /**
     * Write to this file.
     *
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
     * Return a byte array read from this file.
     *
     * @param offset
     * @param n
     * @return
     * @throws IOException
     */
    public byte[] readAt(int offset, int n) {
        if (offset + n > MAX_FILE_SIZE) {
            System.out.println("ERROR: Offset (" + offset + ") and length to read (" + n + ") is over the max file size allowed (32 bytes)");
            return null;
        }

        try {
            try {
                this.fis = new FileInputStream(this);
            } catch (FileNotFoundException e) {
                throw e;
            }

            byte[] data = new byte[32];
            // read into the byte array
            this.fis.read(data, offset, n);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Command to store Chunks from user's input and generate corresponding Metadata.
     * FIXME: if called from Parsers, may create infinite cycle
     *
     * @param userInputBytes - argument to help interaction with the program
     * @return
     */
    public void flush(byte[] userInputBytes) {
        // flushes data in input as Chunks to chunk path
        ChunkParser chunkParser = new ChunkParser();
        try {
            chunkParser.writeChunksToFiles(userInputBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // also stores corresponding Metadata to metadata path
        MetadataParser metadataParser = new MetadataParser();
        metadataParser.writeMetadataToFile(this.getName());
    }


    /**
     * Close all streams.
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


    /**********
     * HELPER *
     **********/

}
