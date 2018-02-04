import java.io.File;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FizdiCrawler extends Thread {

	QueueLinks Q3 = new QueueLinks();
	QueueLinks paintingLinksQ = new QueueLinks();
	DownloadImages downloadimg = new DownloadImages();
	QueueLinks hashsetQ=new QueueLinks(); //hashset q to check duplication
	public FizdiCrawler(QueueLinks q3, QueueLinks hs3) {
		// TODO Auto-generated constructor stub
		Q3 = q3;
		hashsetQ = hs3;
	}
	
	public void run() {
		try {
			
			JsonWriter jsonwrite = new JsonWriter();
			//declare all attributes to null Strings
			
			String paintingToCrawl=null, imgSrc = null, paintingTitle=null, artist=null, paintingDescription = null, price = null, paintingDimensions=null, artistUrl=null;
			String views = null, fav = null, medium = null;
			//paintingToCrawl = imgSrc = paintingTitle = artist = paintingDescription = price = paintingDimensions = authUrl = views = fav = medium = null;
			String tagLabels[] = new String[2];
			String tags[] = null;
			
			int crawlCounter=0;
			
			//file to keep track of last page crawled
			File file = new File("lastFizdipage.txt");
			
			
			while(!Q3.isEmpty()) {
				try {
				String linkToScrape = Q3.getItem(0);
				System.out.println(" link:"+linkToScrape);
				
				file.createNewFile();
				PrintWriter writer = new PrintWriter(file); 
				writer.write(linkToScrape);
				writer.close();
				
	    		Document document = Jsoup.connect(linkToScrape).get();
	    		
	    		
	    		//getting paintingURLs on this page of the website
	    		Elements containerDivs = document.getElementsByClass("CategoryContent");
	    		Elements outerImageDiv	= containerDivs.select(".ProductList");
	    		Elements imageDivs = outerImageDiv.select(".ProductImage");
	    		
	    		int i = 0;
	    		for(Element elem : imageDivs) {
	    			i++;
	    			String paintingURL = elem.select("a").attr("abs:href");
	    			if(!hashsetQ.crawledpages.contains(paintingURL)) {
	    				hashsetQ.crawledpages.add(paintingURL);
	    				paintingLinksQ.addString(paintingURL);
	    			//System.out.println("PaintingURL: "+paintingURL);
	    			}
	    			
	    		}
	    			
	    		
	    		//getting the next page of the website
	    		Elements pagination = document.getElementsByClass("CategoryPagination");
	    		String nextPageURL = pagination.select("a.nav-next").attr("abs:href");
	    		
	    		if(crawlCounter<20) {
	    			Q3.addString(nextPageURL);
	    			crawlCounter++;
	    		}
	    		//Thread.sleep(5*1000);
	    		//I am not giving any delay here
	    		
	    		while(!paintingLinksQ.isEmpty()) {
	    			try {
	    			//now crawling the painting page
	    			
	    			//getting painting URL
	    			paintingToCrawl = paintingLinksQ.getItem(0);
	    			
	    			Document paintingDoc = Jsoup.connect(paintingToCrawl).get();
	    			
	    			
	    			//getting image
	    			Elements imageSrcDiv = paintingDoc.select("div.ProductThumbImage");
	    			imgSrc = imageSrcDiv.select("img").attr("src"); 
	    			//System.out.println("Img Src: "+imgSrc);
	    			
	    			//getting tags
	    			String tagsLine = imageSrcDiv.select("img").attr("alt");
	    			//System.out.println("tagsLine: "+tagsLine);
	    			tags = tagsLine.split(",");
	    			//System.out.println("tags");
	    			//for(int v=0; v<tags.length; v++)
	    				//System.out.println(tags[v]);
	    			
	    			//getting Painting Title
	    			Elements paintingBlockContentDiv = paintingDoc.select("div.BlockContent");
	    			paintingTitle = paintingBlockContentDiv.select("div.left-content .ProductDetailsGrid.mobile .DetailRow h1").text();
	    		//	System.out.println("Painting Title: "+paintingTitle);
	    			
	    			//getting Painting Artist
	    			//Elements artistDiv = paintingTitleDiv.select("span.username-with-symbol.u");
	    			artist = paintingBlockContentDiv.select("div.left-content .ProductDetailsGrid.mobile .DetailRow .Value a").text();
	    			//System.out.println("Artist: "+artist);
	    			artistUrl = paintingBlockContentDiv.select("div.left-content .ProductDetailsGrid.mobile .DetailRow .Value a").attr("abs:href");
	    			//System.out.println("Artist URL: "+artistUrl);
	    			
	    			//getting Painting Price
	    			price = paintingBlockContentDiv.select("div.DetailRow.PriceRow.p-price .Value .ProductPrice.VariationProductPrice").first().text();
	    			//System.out.println("Price: "+price);
	    			
	    			//painting details
	    			String line = paintingDoc.select(".ProductWarrantyContainer").html();
	    			String tokens [] = line.split("<br>");
	    			
	    			//painting dimensions
	    			paintingDimensions = tokens[1].substring(7);
	    			//System.out.println("Dimensions: "+paintingDimensions);
	    			
	    			//painting medium
	    			medium = tokens[2].substring(10);
	    			//System.out.println("Medium: "+medium);
	    			
	    			//getting tags for the painting
	    			tagLabels[0] = tokens[0].substring(13);
	    			tagLabels[1] = tokens[3].substring(11);
	    			//System.out.println("Tags: "+tagLabels[0]+", "+tagLabels[1]);
	    			
	    			
	    			//get image name from title and append format
					String imageFileName=paintingTitle+"_image";
					//int temp = imgSrc.lastIndexOf(".");
			        //String format = imgSrc.substring(temp);
			        String format = imgSrc.substring(imgSrc.lastIndexOf("."), imgSrc.indexOf("?"));
			        imageFileName = imageFileName+format;
			        //System.out.println("imageFileName: "+imageFileName);
	    			
	    			//download image
			        String folder = "C:\\Users\\rutuj\\Desktop\\fizdiImg\\";
	    			downloadimg.download(imgSrc, imageFileName, folder);
	    			
	    			//writing to json
	    			jsonwrite.createJsonObj(paintingToCrawl, imgSrc, paintingTitle, paintingDimensions, artist, artistUrl, paintingDescription, price, views, fav, medium, tags,imageFileName);
	    			
	    			}catch (Exception insideLoop) {
	    				System.out.println("Exception innerLoop: "+insideLoop);
	    			}
	    			//remove crawled link
	    			paintingLinksQ.RemoveItem(0);
	    			
	    			Thread.sleep(5*1000);
	    		}
				}catch(Exception outerLoop) {
					System.out.println("Exception outerLoop: "+outerLoop);
				}
	    		Q3.RemoveItem(0);
	    		//break;
			}
			
			jsonwrite.writeToFile("imagesFizdi.json");
			System.out.println("f");
			Thread.sleep(5*1000);
			
		}
		catch(Exception e) {
			System.out.println("Overall exception: "+e);
			
		}	
	}
	
	
}
