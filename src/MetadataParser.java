import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MetadataParser {


    private String generateMetadata(String data, HashMap<String, Chunk> existingMap) {
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
        String results = "";
        for (String s : chunkString) {
            Chunk c = existingMap.get(s);
            results += c.content + ",";
        }
        results = results.substring(0, results.length() - 1);
        System.out.println(results);

        return results;
    }

}
