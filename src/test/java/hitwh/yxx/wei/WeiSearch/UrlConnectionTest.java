package hitwh.yxx.wei.WeiSearch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.junit.Test;

public class UrlConnectionTest {
    
    private static Logger log = Logger.getLogger("UrlConnectionTest.java");
    
    @Test
    public void testGetEncoding(){
        try {
            URL url1 = new URL("http://www.mail.sina.com.cn");
            URL url2 = new URL("http://www.sina.com.cn");
            URL url3 = new URL("http://www.baidu.com.cn");
            
            URLConnection con1 = url1.openConnection();
            URLConnection con2 = url2.openConnection();
            URLConnection con3 = url3.openConnection();
            
            String enc1 = con1.getContentEncoding();
            String enc2 = con2.getContentEncoding();
            String enc3 = con3.getContentEncoding();
            
            log.info( url1 + " -> " + enc1);
            log.info( url2 + " -> " + enc2); 
            log.info( url3 + " -> " + enc3);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    

}
