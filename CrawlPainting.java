
import java.net.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;


public class CrawlPainting{
	public static int DELAY = 7;
	public static void main(String[] args) throws InterruptedException, IOException {
	
		
			 
				QueueLinks q1=new QueueLinks();
				QueueLinks q2=new QueueLinks();
				QueueLinks q3=new QueueLinks();
				q1.addString("https://www.saatchiart.com/paintings");
				q2.addString("https://shop.deviantart.com/wallart/?modView=none&qh=in%3Atraditional__-attribute%3Amature&order=9&offset=0");
				//q2.addString("https://shop.deviantart.com/wallart/?modView=none&qh=in%3Amanga__-attribute%3Amature&order=9&offset=0");
				q3.addString("https://www.fizdi.com/full-collection/?sort=bestselling&page=1");
				
				//SaatchiCrawler saatchiThread = new SaatchiCrawler(q1);
				//saatchiThread.start();
				
				FizdiCrawler fizdiThread = new FizdiCrawler(q3);
				fizdiThread.start();
				
				//DeviantCrawler deviantThread = new DeviantCrawler(q2);
				//deviantThread.start();
	}
}





