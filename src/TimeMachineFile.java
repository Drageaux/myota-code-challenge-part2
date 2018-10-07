import java.io.*;
import java.nio.file.Files;

public class TimeMachineFile extends File {

    private FileInputStream fis = null;

    public TimeMachineFile(String pathname) {
        super(pathname);
    }


    public byte[] readDataBufferAt(int offset, int n) throws IOException {
        try {
            this.fis = new FileInputStream(this);
            byte[] data = Files.readAllBytes(this.toPath());
            System.out.println(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }

        return null;
    }

    public void close() {
        try {
            // close all streams
            if (this.fis != null) {
                this.fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
