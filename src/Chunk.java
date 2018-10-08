import java.util.UUID;

public class Chunk {
    String uid; // UUID.randomUUID().toString();
    byte[] content;
    String path;

    public Chunk(byte[] content) {
        this.uid = UUID.randomUUID().toString();
        this.content = content;
        this.path = "./chunk/" + this.uid;
    }

    @Override
    public String toString() {
        return new String(this.content);
    }

    public boolean equals(Chunk o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        return false;
    }
}
