import java.util.List;
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

    public Chunk(List<Byte> content) {
        this.uid = UUID.randomUUID().toString();
        this.content = this.byteListToByteArray(content);
        this.path = "./chunk/" + this.uid;
    }

    public Chunk(Byte[] content) {
        this.uid = UUID.randomUUID().toString();
        this.content = this.byteClassListToPrimitiveByteArray(content);
        this.path = "./chunk/" + this.uid;
    }


    @Override
    public String toString() {
        return new String(this.content);
    }

    private byte[] byteListToByteArray(List<Byte> list) {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) byteArray[i] = list.get(i);
        return byteArray;
    }

    private byte[] byteClassListToPrimitiveByteArray(Byte[] list) {
        byte[] byteArray = new byte[list.length];
        for (int i = 0; i < list.length; i++) byteArray[i] = list[i];
        return byteArray;
    }
}
