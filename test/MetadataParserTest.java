import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MetadataParserTest {

    @Test
    void createMetadataObjectTest() {

        HashMap<Integer, ArrayList<Chunk>> metaMap = new HashMap<>();


        String metadata = readMetadataFile(new TimeMachineFile("./meta/" + "test"));
        System.out.println(metadata);

        if (metadata != null && !metadata.isEmpty()) {
            // versions are new-line separated; can store up to 3 lines only
            String sanitzedMeta = metadata.replace("\r\n", "\n"); // new line is \r\n for some reason
            String[] versionsStrArr = sanitzedMeta.split("\n");
            System.out.println(versionsStrArr);

            for (String version : versionsStrArr) {
                // file version vs metadata are separated by a space
                String[] verVsMeta = version.split(" ");
                Integer verInt = Integer.parseInt(verVsMeta[0]);
                String chunkIdsStr = verVsMeta[1];
                // individual chunks are comma-separated
                String[] chunkIdsStrArr = chunkIdsStr.split(",");

                // for each ID, retrieve Chunk and populate into Array
                ArrayList<Chunk> chunksArr = new ArrayList<>();
                for (String id : chunkIdsStrArr) {
                    Chunk chunk = this.getChunkFromId(id);
                    chunksArr.add(chunk);
                }

                metaMap.put(verInt, chunksArr);
            }
        }
    }

    private Chunk getChunkFromId(String id) {
        TimeMachineFile f = new TimeMachineFile("./chunk/" + id);
        try {
            String chunkStr = Helper.readFile(f);
            return new Chunk(id, chunkStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readMetadataFile(TimeMachineFile metadataFile) {
        String metadataString = null;
        try {
            metadataString = Helper.readFile(metadataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadataString;
    }
}
