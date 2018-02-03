import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class parseJSONFile {

	public JSONArray parseJSONFile(String jsonFilePath){
        //Get the JSON file, in this case is in ~/resources/test.json
        InputStream jsonFile =  getClass().getResourceAsStream(jsonFilePath);
        Reader readerJson = new InputStreamReader(jsonFile);
        //Parse the json file using simple-json library
        Object fileObjects= JSONValue.parse(readerJson);
        JSONArray arrayObjects=(JSONArray)fileObjects;
        return arrayObjects;
    }
}
