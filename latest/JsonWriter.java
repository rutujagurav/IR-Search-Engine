import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonWriter {

	 JSONArray list;
	 public JsonWriter() {
		// TODO Auto-generated constructor stub
		 list=new JSONArray();
	}
	 
	 // create json object and add it to JsonArray
	void createJsonObj(String url,String imgsrc,String title,String size, String author, String authUrl, String description, String price, String views, String fav, String medium, String[] tags,String filename) {
		  JSONObject obj = new JSONObject();
		  JSONArray keywords = new JSONArray();
		  obj.put("url", url);
	        obj.put("title", title);
	        obj.put("imgsrc", imgsrc);
	        obj.put("price",price);
		    obj.put("artist", author);
	        obj.put("artistURL", authUrl);
	        obj.put("size",size);
	        obj.put("description",description);
	        obj.put("medium",medium);
	        obj.put("views",views);
	        obj.put("likes",fav);
	        obj.put("filename", filename);
	        
	        for(int i=0;i<tags.length;i++)
	        keywords.add(tags[i]);

	        obj.put("tags",keywords);
	        //add the object to array
	        addtoJsonArray(obj);
	
	        
	}
	
	//adds object to array
	void addtoJsonArray( JSONObject obj) {
		list.add(obj);
      //  System.out.println(list);
	}
	
	//writes json array to a json file
	void writeToFile(String filename) {
		 try (FileWriter file = new FileWriter(filename)) {

	            file.write(list.toJSONString());
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
