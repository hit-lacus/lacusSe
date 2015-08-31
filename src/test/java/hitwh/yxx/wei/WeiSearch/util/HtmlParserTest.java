package hitwh.yxx.wei.WeiSearch.util;

import hitwh.yxx.wei.WeiSearch.bean.Page;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class HtmlParserTest {

    private static Logger log = Logger.getLogger("HtmlParserTest.java");
    private String htmlDoc;

    @Before
    public void initialize() throws Exception {
        // System.out.println(System.getProperty("user.dir"));
        StringBuffer sb = new StringBuffer();
        byte[] temp = new byte[1024];
        int count;
        FileInputStream fis;
        /*
         * try { fis = new
         * FileInputStream(".\\src\\test\\resources\\sina.html");
         * 
         * while((count = fis.read(temp)) > 0){ sb.append(new
         * String(temp,0,count)); } fis.close(); htmlDoc = new
         * String(sb.toString().getBytes(),"UTF-8"); //htmlDoc = new
         * String(htmlDoc.g,"UTf-8"); //htmlDoc = URLEncoder.encode (htmlDoc,
         * "UTF-8" ); } catch (FileNotFoundException e) { e.printStackTrace(); }
         * catch (IOException e) { e.printStackTrace(); }
         */

        InputStreamReader isr = new InputStreamReader(new FileInputStream(".\\src\\test\\resources\\sinamail.html"),
                "GB2312");
        char[] cc = new char[32];

        while (isr.read(cc) > 0) {
            sb.append(cc);
        }
        htmlDoc = new String(sb.toString().getBytes(), "UTF-8");
        isr.close();
    }

    // @Test
    public void testParse1() {
        String text = HtmlParser.html2Text(htmlDoc);
        log.info(text);

    }

    // @Test
    public void testParse2() {
        ArrayList<String> list = HtmlParser.urlDetector(htmlDoc);
        for (String u : list) {
            log.info(u);
        }

    }

    //@Test
    public void testParse3() {
        Charset cs = HtmlParser.getCharset(htmlDoc);
        log.info(cs);

    }

    @Test
    public void testGetFullInformation() {

        HttpClient httpClient = new DefaultHttpClient();

        HttpUriRequest request = new HttpGet("http://www.sina.com");
        HttpResponse response;
        try {
            response = httpClient.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        HttpEntity he = response.getEntity();

        String content;
        try {
            content = HtmlParser.getHtmlDocWithoutEncoding(he.getContent());
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return;
        }
        Charset cs = HtmlParser.getCharset(content);
        
        

        try {
            String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);
            //log.info(utfDoc);
            Page page = new Page();
            page = HtmlParser.getFullInformation("", utfDoc, page); 
            log.info(page);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }
    
    //@Test
    public void testReplaceAll(){
        String s = "<title>1  ssss";
        log.info(s.replaceAll("<title>", ""));
        
    }
    
    

}
