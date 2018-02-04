import java.util.ArrayList;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class QueueLinks{
	ArrayList<String> links;
	HashSet<String> crawledpages;
	PrintWriter writer;
	File file;
	QueueLinks(){
		links = new ArrayList<String>();
	}
	
	void HashSetCreate() throws IOException{
		crawledpages = new HashSet<String>();
		file = new File("hash_file.txt");
		
		file.createNewFile();
	//	return crawledpages;
	}
	
	//Write hashset to file
	void writeHashToFile() throws FileNotFoundException {
		writer = new PrintWriter(file);
		File file = new File("hash_file.txt");
		String nl=System.getProperty("line.separator");
		//	file.createNewFile();
			//PrintWriter writer = new PrintWriter(file);
			Iterator<String> i = crawledpages.iterator();
			while(i.hasNext()) {
				writer.write(i.next());
				writer.write((nl));
			}
			writer.close();
	}

	void addString(String url) {
		
		links.add(url);
	}
	
	void displayQ() {
		for(int i=0;i<links.size();i++)
		System.out.println(" i="+i+" "+links.get(i));
	}
	
	int getSize() {
		return links.size();
	}
	
	String getItem(int index) {
		return links.get(index);
	}
	
	void RemoveItem(int index){
		links.remove(index);
	}
	
	boolean isEmpty() {
		return links.isEmpty();
	}
	
}