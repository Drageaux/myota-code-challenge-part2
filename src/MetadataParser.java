import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class MetadataParser {

    public MetadataParser() {
    }


    public void writeMetadataToFile(String fileName) {
        TimeMachineFile tmf = new TimeMachineFile("./meta/" + fileName);
        String metadata = "";
        try {
            metadata = Helper.readFile(tmf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sanitzedMeta = metadata.replace("\r\n", "\n"); // new line is \r\n for some reason
        String[] versionsStrArr = sanitzedMeta.split("\n");
        versionsStrArr = Arrays.copyOf(versionsStrArr, versionsStrArr.length - 1); // remove oldest ver

        ArrayList<String> finalList = new ArrayList<>();
        finalList.add(this.compileNewVersionMetadata(fileName));
        Collections.addAll(finalList, versionsStrArr);
        String output = "";
        for (String s : finalList) {
            output += s + "\n";
        }
        output = output.substring(0, output.length() - 1);

        // write to file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("./meta/" + fileName);
            fos.write(output.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String compileNewVersionMetadata(String fileName) {
        Metadata metadata = this.createMetadataObject(fileName);
        HashMap<Integer, ArrayList<Chunk>> newMetaMapping = new HashMap<>();

        Iterator<Integer> iterator = metadata.versionChunksPair.keySet().iterator();
        Integer currVer = null;
        Integer lastVer = null;
        Integer oldestVer = null;
        // find oldest version
        oldestVer = iterator.next();
        if (iterator.hasNext()) {
            lastVer = iterator.next();
        }
        if (iterator.hasNext()) {
            currVer = iterator.next();
        }

        Integer newVer = null;
        metadata.versionChunksPair.remove(oldestVer);
        if (currVer != null) {
            newVer = currVer + 1;
        } else newVer = 1;

        // new mapping
        ChunkParser cp = new ChunkParser();
        // keep as String
        return newVer + " " + this.generateVersionMetadata(fileName, cp.existingChunks);
    }


    public void restoreFromVersion(String fileName, Integer currentVersion, String version) {
        // CURR, LAST, OLDEST
        // get version, version minus 1 or 2, exit if key/value doesn't exist
    }


    private Metadata createMetadataObject(String fileName) {
        Metadata meta = null;

        HashMap<Integer, ArrayList<Chunk>> metaMap = new HashMap<>();

        String metadata = readMetadataFile(new TimeMachineFile("./meta/" + fileName));
        System.out.println(metadata);

        if (metadata != null && !metadata.isEmpty()) {
            // versions are new-line separated; can store up to 3 lines only
            String sanitzedMeta = metadata.replace("\r\n", "\n"); // new line is \r\n for some reason
            String[] versionsStrArr = sanitzedMeta.split("\n");

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

        meta = new Metadata(metaMap);
        return meta;
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


    /**
     * Generate version-specific metadata.
     *
     * @param fileName
     * @param existingMap
     * @return
     */
    private String generateVersionMetadata(String fileName, HashMap<String, Chunk> existingMap) {
        String results = "";
        // metadata should create a file in path "meta/{name}", where {name} is same as "file/{name}"
        TimeMachineFile tmf = new TimeMachineFile("./file/" + fileName);
        String data = "";
        try {
            data = Helper.readFile(tmf);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                currStr += s;
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
            results += c.uid + ",";
        }
        results = results.substring(0, results.length() - 1);

        return results;
    }

}
