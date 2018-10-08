import java.util.List;

public class ChunkParser {


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
}
