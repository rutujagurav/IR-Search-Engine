import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class SaatchiCrawler extends Thread
{
	QueueLinks Q1 = new QueueLinks();
	QueueLinks imageLinksQ = new QueueLinks();
	public SaatchiCrawler(QueueLinks q1) {
		// TODO Auto-generated constructor stub 
		
		 Q1= q1;
		
	}
	
    public void run()
    {
    	
        try
        { 
        	
        	while(!Q1.isEmpty()) {
        		String linkToScrape = Q1.getItem(0);
        		Document document = Jsoup.connect(linkToScrape).get();
        		
        		System.out.println(" link:"+linkToScrape);
        	
        		Elements outerUL=document.getElementsByAttributeValue("class","item-list masonry");
        		Elements firstDiv= outerUL.select("div.list-art-image");
        		int i=0;
		
        		Elements imglinks= firstDiv.select("div.list-art-image-wrapper a");
        	
				
        		for (Element link : imglinks) {
        			i++;
				
        			 String absHref = link.attr("abs:href");
				
        			imageLinksQ.addString(absHref);
        			
        		}
        		
        		

        		Elements pagination=document.getElementsByAttributeValue("class","so-pagination");
        					// get next page link and add to Q1
        		Elements pages=pagination.select("ul.pull-right.pages");
        		
        		Element nextpage=pages.select("li").last().previousElementSibling().select("a").first();
        				
        		//Element nextAnchor
        		String nextLink=nextpage.attr("abs:href");
        		
        		Q1.addString(nextLink);
        				
        		
        		// Element tableData = document.select("table").get(5); 
        		
        		Thread.sleep(8*1000);
        		 while(!imageLinksQ.isEmpty()) {
        			 //do crawlin thing
        			 
        			 
        			 String imgLinkToCrawl=imageLinksQ.getItem(0);
        			 //small-12 medium-6 large-12 columns art-meta
        			 System.out.println("img link:"+imgLinkToCrawl);
        			 
        			 Document imgDocument = Jsoup.connect(imgLinkToCrawl).get();
        			 Elements imgDiv=imgDocument.select("div.small-12.medium-6.large-12.columns.art-meta");
        			 Elements getTitle= imgDiv.select("h3.title");
        			 String imgTitle=getTitle.text();
        			 
        			 Element authorDetails=imgDiv.select("p a").first();
        			 String author=authorDetails.text();
        			 
        			 Element authUrl=authorDetails.select("a").first();
        			 String authorLink = authUrl.attr("abs:href");
        			 String size="";
        			 Elements sizeDetails=imgDiv.select("p.category-size span");
        			 for (Element span: sizeDetails) {
        				 size=size+span.text()+"X";
        			 }
        			 System.out.println(imgTitle);
        			 System.out.println(author);
        			 System.out.println(size);
        			 Thread.sleep(1*1000);
        			 System.out.println("inside image link crawler");
        			 imageLinksQ.RemoveItem(0);
        			 
        			 Elements priceDiv=imgDocument.select("div.art-detail-ribbon");
        			Elements getPrice=priceDiv.select("div.small-6 large-6 columns");
        			 
        			 
        			 
        			 Thread.sleep(9*1000);
        		 }
        		 
        		 Q1.RemoveItem(0);
        		
        		
        	}
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println (e);
        }
    }
}

	

