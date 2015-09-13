package hitwh.yxx.lacusSe.web.action;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 
 * @author Administrator
 *
 */
public class HtmlParser {
    private static Logger log = Logger.getLogger("HtmlParser.java");

    public HtmlParser() {
    }

    /**
     * 获取网页中的纯文本信息
     * @param inputString
     * @return
     */
    public static String html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script, p_style, p_html, p_comment, p_meta;
        Matcher m_script, m_style, m_html, m_comment, m_meta;

        try {
            // 定义script正则式{或<script[^>]*?>[\s\S]*?<\/script> }
            String regEx_script = "<script[^>]*?>[\\s\\S]*?</script>";
            // 定义style正则式{或<style[^>]*?>[\s\S]*?<\/style> }
            String regEx_style = "<style[^>]*?>[\\s\\S]*?</style>";

            String regEx_comment = "<!--[^>]*?-->";
            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";

            String regEx_meta = "&[\\w]*?;";

            p_meta = Pattern.compile(regEx_meta, Pattern.CASE_INSENSITIVE);
            m_meta = p_meta.matcher(htmlStr);
            htmlStr = m_meta.replaceAll(""); // 过滤HTML实体

            p_comment = Pattern.compile(regEx_comment, Pattern.CASE_INSENSITIVE);
            m_comment = p_comment.matcher(htmlStr);
            htmlStr = m_comment.replaceAll(""); // 过滤html注释

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            // 去除所有空格
            textStr = htmlStr.replaceAll("\\s*", "");

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }


    /**
     * 从文本提取出URL
     * @param htmlDoc
     * @return
     */
    public static ArrayList<String> urlDetector(String htmlDoc) {
        String temp;
        final String patternString = "<[A|a] [^>]*?>";
        final String patternString2 = "href=\"[^\"]+?\"";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile(patternString2, Pattern.CASE_INSENSITIVE);

        ArrayList<String> allURLs = new ArrayList<>();

        Matcher matcher = pattern.matcher(htmlDoc);
        String tempURL;
        while (matcher.find()) {
            try {
                tempURL = matcher.group();
                Matcher m = pattern2.matcher(tempURL);
                while(m.find()){
                    temp = m.group();
                    temp = temp.substring(6,temp.length() - 1).trim();
                    if(temp.startsWith("http")){
                        allURLs.add(temp);
                    }
                }      
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allURLs;
    }

    /**
     * <meta charset="utf-8"> 
     * <meta http-equiv="Content-Type"content="text/html; charset=utf-8"/>
     * <meta http-equiv="content-type" content="text/html;charset=GB2312">
     * <meta http-equiv="content-language" content="utf-8" />  
     * 获取网页编码
     * @param htmlDoc HTML文件原件
     * @return 该文本的编码方式
     */
    public static Charset getCharset(String htmlDoc) {
        Charset cs;
        String charset = "";
        String regEx_Charset1 = "<meta charset=\"\\S+?\"";
        String regEx_Charset2 = "content=\"text/html;\\s*?charset=\\S+?\"";
        boolean isFound = false;

        String regEx_Inner1 = "\"\\S+?\"";
        String regEx_Inner2 = "set=\\S+?\"";

        Pattern p_Charset1 = Pattern.compile(regEx_Charset1);
        Matcher m_Charset1 = p_Charset1.matcher(htmlDoc);

        Pattern p_Charset2 = Pattern.compile(regEx_Charset2);
        Matcher m_Charset2 = p_Charset2.matcher(htmlDoc);

        Pattern p_Inner1 = Pattern.compile(regEx_Inner1);
        Pattern p_Inner2 = Pattern.compile(regEx_Inner2);

        while (m_Charset1.find()) {
            charset = m_Charset1.group();
            Matcher m_Inner1 = p_Inner1.matcher(charset);
            //log.info(charset);
            while (m_Inner1.find()) {
                charset = m_Inner1.group();
                charset = charset.substring(1, charset.length() - 1);
                isFound = true;
                break;
            }
            break;
            //log.info(charset);
        }

        if (!isFound) {
            while (m_Charset2.find()) {
                charset = m_Charset2.group();
                Matcher m_Inner2 = p_Inner2.matcher(charset);
                //log.info(charset);
                while (m_Inner2.find()) {
                    charset = m_Inner2.group();
                    charset = charset.substring(4, charset.length() - 1);
                    isFound = true;
                }
                //log.info(charset);
            }

        }
        try {
            cs = Charset.forName(charset);
        } catch (Exception e) {
            log.error("获取编码错误，原因：" + e.getMessage() + " -> "+charset);
            //log.info(htmlDoc);
            return null;
        }

        return cs;
    }
    
    /**
     * 从输入流获得未经恰当编码的文本
     * @param is
     * @return
     */

    public static String getHtmlDocWithoutEncoding(InputStream is) {
        StringBuffer sb = new StringBuffer();
        
        try {
        	log.info("正在获取数据");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is)); 
            String line = null; 
            while((line = reader.readLine()) != null) 
                sb.append(line + "\n"); 
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        
        return sb.toString();
    }
    
    /**
     * 从输入流获得恰当的，编码为UTF-8的文本
     * @param is
     * @param cs
     * @return
     * @throws IOException
     */
    
    public static String getHtmlDocWithUtf8(InputStream is,Charset cs) throws IOException {
        
        StringBuffer sb = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is, cs);
        String htmlDoc;   
        char[] cc = new char[32] ;
        
        while( isr.read(cc) > 0){
              sb.append(cc);
        }
        
        htmlDoc = new String(sb.toString().getBytes(),"UTF-8");
        
        log.info(htmlDoc.substring(0, 300));
        
        is.close();
        isr.close(); 
        return htmlDoc;
    }
    
    
    /**
     * 去除一些无用的符号
     */
    public static String removeUselessSymbol(String utfDoc){
        
        return null;
    }
    
    

}
