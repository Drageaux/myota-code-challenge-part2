import java.util.ArrayList;
import java.util.HashMap;

public class Metadata {

    HashMap<Integer, ArrayList<Chunk>> versionChunksPair;

    public Metadata(HashMap<Integer, ArrayList<Chunk>> metaMapping) {
        this.versionChunksPair = metaMapping;
    }
}
