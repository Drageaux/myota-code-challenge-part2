import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MetadataParser {

    public MetadataParser() {
    }

    public void writeMetadataToFile(String fileName) {
        // file version vs metadata are separated by a space
        // individual chunks are comma-separated
        // versions are new-line separated; can store up to 3 lines only
    }

    private Chunk getChunkFromId(String id){

        return null;
    }

    private Metadata createMetadataObject(String fileName) {
        Metadata meta = null;

        String metadata = this.readMetadataFile(new TimeMachineFile("./meta/" + fileName));

        if (metadata != null && !metadata.isEmpty()) {
            // file version vs metadata are separated by a space
            // individual chunks are comma-separated
            // versions are new-line separated; can store up to 3 lines only
            System.out.println(metadata);
        }
        return meta;
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


    /**
     * Generate version-specific metadata.
     *
     * @param data
     * @param existingMap
     * @param version
     * @return
     */
    private String generateVersionMetadata(String data, HashMap<String, Chunk> existingMap, int version) {
        String results = Integer.toString(version) + " ";
        // metadata should create a file in path "meta/{name}", where {name} is same as "file/{name}"

        // efficient solution: re-array all data of new file
        // could use regex but it's a little complicated for the scope
        ArrayList<String> chunkString = new ArrayList<>();
        String currStr = "";
        for (String s : data.split("")) {
            if (s.equals("0")) {
                chunkString.add(currStr);
                currStr = "";
            }
            if (currStr.length() < 32) {
                currStr.concat(s);
            }
        }
        if (currStr.length() != 0) { // read the rest of the string
            chunkString.add(currStr);
            currStr = "";
        }

        // then loop through each chunk in that array
        // to find location in existingMap
        // --> we don't have to loop through the entire existingMap
        for (String s : chunkString) {
            Chunk c = existingMap.get(s);
            results += c.content + ",";
        }
        results = results.substring(0, results.length() - 1);
        System.out.println(results);

        return results;
    }

}
