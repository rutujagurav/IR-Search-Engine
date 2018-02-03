import java.io.File;
import java.io.PrintWriter;

import javax.print.attribute.IntegerSyntax;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DeviantCrawler extends Thread {

	QueueLinks Q2 = new QueueLinks();
	QueueLinks paintingLinksQ = new QueueLinks();
	DownloadImages downloadimg= new DownloadImages();
	
	public DeviantCrawler(QueueLinks q2) {
		// TODO Auto-generated constructor stub
		Q2 = q2;
	}
	
	public void run() {
		try {
			
			JsonWriter jsonwrite =new JsonWriter();
			//declare all attributes to null Strings
			
			String paintingToCrawl, imgSrc, paintingTitle, artist, paintingDescription, price, paintingDimensions, artistUrl;
			String views = null, fav = null, medium = null;
			//paintingToCrawl = imgSrc = paintingTitle = artist = paintingDescription = price = paintingDimensions = authUrl = views = fav = medium = null;
			String tagLabels[];
			File file = new File("lastDeviantpage.txt");
			while(!Q2.isEmpty()) {
				try {
				String linkToScrape = Q2.getItem(0);
				System.out.println(" link:"+linkToScrape);
				
				file.createNewFile();
				PrintWriter writer = new PrintWriter(file); 
				writer.write(linkToScrape);
				writer.close();
				
				
	    		Document document = Jsoup.connect(linkToScrape).get();
	    		
	    		
	    		
	    		//getting paintingURLs on this page of the website
	    		Elements containerDivs = document.getElementsByClass("stream stream-fh");
	    		Elements imageDivs	= containerDivs.select("div.tt-a.shop-tt.tt-fh");
	    		int i = 0;
	    		for(Element elem : imageDivs) {
	    			i++;
	    			String paintingURL = elem.select("span.tt-w span.details a.t.tt-fh-pr").attr("abs:href");
	    			paintingLinksQ.addString(paintingURL);
	    			//System.out.println(paintingURL);
	    		}
	    		
	    		//getting painting thumbnail
	    		//Elements imgSrcDiv = containerDivs.select("span.shadow");
	    		//imgSrc = imgSrcDiv.select("a img").attr("src");
	    		//System.out.println("Image Src: "+imgSrc);
	    		
	    		
	    		//getting the next page of the website
	    		Elements pagination = document.getElementsByClass("pagination");
	    		String nextPageURL = pagination.select("ul.pages li.next a.away").attr("abs:href");
	    		
	    		Q2.addString(nextPageURL);
	    		
	    		//Thread.sleep(5*1000);
	    		
	    		while(!paintingLinksQ.isEmpty()) {
	    			try {
	    			//now crawling the painting page
	    			
	    			//getting painting URL
	    			paintingToCrawl = paintingLinksQ.getItem(0);
	    			
	    			Document paintingDoc = Jsoup.connect(paintingToCrawl).get();
	    			
	    			//getting image
	    			Element imageDiv = paintingDoc.select(".dev-view-deviation img").get(0);
	    			imgSrc = imageDiv.attr("abs:src");
	    			System.out.println("iiiiiiiiiiiii");
	    			System.out.println(imgSrc);
	    			
	    			//getting tags for the painting
	    			Elements tagsDivs = paintingDoc.select("div.dev-about-cat-cc");
	    			Elements tags = tagsDivs.select("a.h");
	    			int numOfTags = tags.size();
	    			tagLabels = new String[numOfTags];
	    			int j = 0;
	    			for(Element tagsIn : tags){
	    				tagLabels[j++] = tagsIn.select("span").text();
	    			}
	    			
	    			//System.out.print("Painting Tags: ");
	    			//for(int k = 0; k<numOfTags; k++)
	    			//{
	    				//System.out.print(tagLabels[k]+" ");
	    			//}
	    			//System.out.println();
	    			
	    			//getting Painting Title
	    			Elements paintingTitleDiv = paintingDoc.select("div.dev-title-container");
	    			paintingTitle = paintingTitleDiv.select("a").text();
	    			//System.out.println("Painting Title: "+paintingTitle);
	    			
	    			//getting Painting Artist
	    			Elements artistDiv = paintingTitleDiv.select("span.username-with-symbol.u");
	    			artist = artistDiv.select("a").first().text();
	    			//System.out.println("Artist: "+artist);
	    			artistUrl = artistDiv.select("a").attr("abs:href");
	    			//System.out.println("Artist URL: "+artistUrl);
	    			
	    			//getting Painting Description
	    			Elements descDiv = paintingDoc.select("div.dev-description");
	    			paintingDescription = descDiv.select("div.text.block").text();
	    			//System.out.println("Description: "+paintingDescription);
	    			
	    			//getting Painting Price
	    			Elements priceDiv = paintingDoc.select("div.dev-view-meta");
	    			Elements priceSpan = priceDiv.select("div.price span div").get(1).select("span");
	    			price="";
	    			int l=0;
	    			for(Element x : priceSpan) {
	    				//System.out.println("Price: "+x.html());
	    				price = price + x.text();
	    				l++;
	    				if(l>2) {
	    					break;
	    				}
	    			}
	    			
	    			//System.out.println("Price: "+price);
	    			
	    			//painting dimensions
	    			paintingDimensions = "12x18";
	    			//System.out.println("Dimensions: "+paintingDimensions);
	    			
	    			//get image name from title and append format
					String imageFileName=paintingTitle+"_image";
					int temp = imgSrc.lastIndexOf(".");
			        String format = imgSrc.substring(temp);
			        imageFileName = imageFileName+format;
					
			        //download image 
					downloadimg.download(imgSrc,imageFileName);
	    			
	    			//write to json
	    			jsonwrite.createJsonObj(paintingToCrawl, imgSrc, paintingTitle, paintingDimensions, artist, artistUrl, paintingDescription, price, views, fav, medium, tagLabels, imageFileName);
	    			}catch(Exception insideLoop) {
	    				
	    			}
	    			paintingLinksQ.RemoveItem(0);
	    			
	    			Thread.sleep(5*1000);
	    		}
				}catch(Exception outsideLoop) {
					
				}
	    		Q2.RemoveItem(0);
	    		break;
			}
			jsonwrite.writeToFile("imagesDeviant.json");
			System.out.println("hhhhhhhhhhhhhhhhhhhhllllllllllll");
			Thread.sleep(5*1000);
			
		}
		catch(Exception e) {
			System.out.println(e);
			
		}	
	}
}
