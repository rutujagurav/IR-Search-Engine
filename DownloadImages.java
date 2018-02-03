

import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;

public class DownloadImages {

    private static final String folderPath = "C:\\Users\\rutuj\\Desktop\\images\\";

  public void download(String src,String name) throws IOException {

        String folder = null;
        System.out.println("in download src: "+src);
        
       //Open a URL Stream
        URL url = new URL(src);
        InputStream in = url.openStream();
        
        OutputStream out = new BufferedOutputStream(new FileOutputStream( folderPath + name));

        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }
        out.close();
        in.close();

    }
}