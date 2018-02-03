import java.util.ArrayList;

public class QueueLinks{
	ArrayList<String> links;
	QueueLinks(){
		links = new ArrayList<String>();
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