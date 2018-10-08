import java.util.List;
import java.util.UUID;

public class Chunk {
    String uid; // UUID.randomUUID().toString();
    byte[] content;
    String path;


    public Chunk(String filename, byte[] content) {
        this.uid = filename;
        this.content = content;
        this.path = "./chunk/" + this.uid;
    }

    public Chunk(byte[] content) {
        this.uid = UUID.randomUUID().toString();
        this.content = content;
        this.path = "./chunk/" + this.uid;
    }


    @Override
    public String toString() {
        return new String(this.content);
    }
}
