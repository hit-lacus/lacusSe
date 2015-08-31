package hitwh.yxx.wei.WeiSearch.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import hitwh.yxx.wei.WeiSearch.bean.Page;
import hitwh.yxx.wei.WeiSearch.bean.UrlPool;

/**
 * 在独立的线程中爬取网页信息
 * 
 * @author lacus
 *
 */
@SuppressWarnings("deprecation")
public class Spy implements Runnable {
    private UrlPool pool;
    private HttpClient httpClient;
    private static Logger log = Logger.getLogger("Spy.java");
    private int searchDepth = 1;

    public Spy(UrlPool pool) {
        httpClient = new DefaultHttpClient();
        this.pool = pool;
    }

    /**
     * 请求遇到的不正常的情况：<br>
     * 1.找不到主机 <br>
     * 2.找到主机，但返回的状态码不是200<br>
     * 
     */
    @Override
    public void run() {
        log.info("在线程" + Thread.currentThread().getName() + ",   执行第" + searchDepth + "层搜索.");

        while (true) {
            HttpUriRequest request;
            HttpResponse response = null;
            String in = pool.getUrl();
            
            if(in == null){
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    log.error("被吵醒");
                    continue;
                }
            }
            
            Page page = new Page();
            page.setUrl(in);

            log.info(" Get a url:" + in);

            /**
             * 获得一个正确的请求对象
             */
            try {
                request = new HttpGet(in);
            } catch (IllegalArgumentException e) {
                log.error("URL地址出错:" + in + ",操作中断!");
                pool.getNewUrl().remove(in);
                continue;
            }

            /**
             * 发起Http Get请求，尝试获得响应，若无法获得正确的响应，跳过该链接
             */
            try {
                response = httpClient.execute(request);
            } catch (ClientProtocolException e) {
                try {
                    if (response != null)
                        response.getEntity().getContent();
                } catch (IllegalStateException e1) {
                    log.error("未知异常->" + e.getMessage());
                } catch (IOException e1) {
                    log.error("未知异常->" + e.getMessage());
                }
                log.error("主机无响应，请稍后再试 -> " + e.getMessage());
                pool.getNewUrl().remove(in);
                continue;
            } catch (IOException e) {
                try {
                    if (response != null)
                        response.getEntity().getContent();
                } catch (IllegalStateException e1) {
                    log.error("未知异常->" + e.getMessage());
                } catch (IOException e1) {
                    log.error("未知异常->" + e.getMessage());
                }
                log.error("网络故障，请稍后再试 -> " + e.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    log.error("线程被吵醒了 -> " + e.getMessage());
                }
                pool.getNewUrl().remove(in);
                continue;
            } catch (Exception e) {
                try {
                    if (response != null)
                        response.getEntity().getContent();
                } catch (IllegalStateException e1) {
                    log.error("未知异常->" + e.getMessage());
                } catch (IOException e1) {
                    log.error("未知异常->" + e.getMessage());
                }
                log.error("未知异常 -> " + e.getMessage());
                pool.getNewUrl().remove(in);
                continue;
            }

            HttpEntity he = response.getEntity();

            String content;
            int status = response.getStatusLine().getStatusCode();
            String contentType = he.getContentType().getValue();// 必须是text/html才能解析

            /**
             * 假如服务器的响应类型不是html类型的话，或者状态码不是200，我们就抛弃这条链接
             */
            if (status == 200 && contentType.contains("text/html")) {
                log.info("状态码  ：" + status + "-> OK.");
            } else {
                
                if(status == 200){
                    log.info("响应内容类型错误" + contentType + "-> ERROR.");
                }else{
                    log.info("响应状态码  错误" + status + "-> ERROR.");
                }
                
                try {
                    content = HtmlParser.getHtmlDocWithoutEncoding(response.getEntity().getContent());
                } catch (IllegalStateException e) {
                    log.error("未知异常->" + e.getMessage());
                } catch (IOException e) {
                    log.error("未知异常->" + e.getMessage());
                } finally {
                    pool.getNewUrl().remove(in);
                }
                continue;
            }

            /**
             * 为page对象写入日期
             */
            for (Header h : response.getAllHeaders()) {
                if (h.getName().equals("Date")) {
                    Date date = new Date(h.getValue());
                    page.setDate(date);
                }
            }

            /**
             * 获取目标链接的编码方式
             */
            try {
                content = HtmlParser.getHtmlDocWithoutEncoding(he.getContent());
            } catch (IllegalStateException e1) {
                log.error("未知错误 -> " + e1.getMessage());
                continue;
            } catch (IOException e1) {
                log.error("未知错误 -> " + e1.getMessage());
                continue;
            }
            Charset cs = HtmlParser.getCharset(content);
            if (cs == null) {
                log.error("Charset 获取异常！假设其为 UTF-8");
                cs = Charset.forName("UTF-8");
                continue;
            }

            /**
             * 为page设置md5
             */
            String md5 = MD5Util.string2MD5(content);
            page.setMd5(md5);

            /**
             * 无论原链接的文本采用何种编码，这里都采用UTF-8编码存储文本信息<br>
             * 
             */
            String utfDoc;
            String rawDoc;
            try {
                utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);
                rawDoc = HtmlParser.html2Text(utfDoc);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                continue;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            page.setUtfContent(rawDoc);
            
            page = HtmlParser.getFullInformation(in, utfDoc, page);

            /**
             * 从文本中获取新的链接，并存储到链接池中
             */
            List<String> list = HtmlParser.urlDetector(utfDoc);
            for (String url : list) {
                pool.addUrl(url);
            }
            
            

            /**
             * 收尾工作，准备下次爬取
             */
            searchDepth++;

            log.info("Count of REMAINED URL is " + pool.getNewUrl().size() + ".   Count of VISITED URL is "
                    + pool.getVisitedUrl().size()
                    + "，线程数 "
                    + Thread.activeCount());
            
            log.info(page);

            /**
             * 稍稍休息一下 ^_^
             */
        }

        /*
         * URL url; URLConnection conn ; try { url = new URL(pool.getUrl());
         * conn = url.openConnection();
         * 
         * InputStreamReader isr = new InputStreamReader(conn.getInputStream());
         * 
         * 
         * BufferedReader reader = new BufferedReader(new
         * InputStreamReader(conn.getInputStream())); String line = null;
         * while((line = reader.readLine()) != null) sb.append(line + "\n"); }
         * catch (IOException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         */

    }

}
