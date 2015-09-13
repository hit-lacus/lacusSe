package hitwh.yxx.wei.lacusSe.seeker.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import hitwh.yxx.wei.lacusSe.seeker.bean.Page;
import hitwh.yxx.wei.lacusSe.seeker.bean.UrlPool;
import hitwh.yxx.wei.lacusSe.seeker.constant.Constant;

/**
 * 在独立的线程中爬取网页信息
 * 
 * @author lacus
 */
@SuppressWarnings("deprecation")
public class Spy implements Runnable {
	private UrlPool pool;
	private HttpClient httpClient;
	private static Logger log = Logger.getLogger("Spy.java");
	private int searchDepth = 1;
	public final static boolean URL_COLLECTOR = false;//表示这是一个url收集器
	public final static boolean HTML_PARSER = true;//表示这是一个html解析器
	private boolean kind ;
	private boolean isFull = false;
	public Spy(UrlPool pool,boolean kind) {
		this.kind = kind;
		httpClient = new DefaultHttpClient();
		
		this.pool = pool;
	}

	/**
	 * <pre>
	 * 请求遇到的不正常的情况：
	 * 1.找不到主机
	 * 2.找到主机，但返回的状态码不是200
	 * 3.返回代码是200，但内容类型不是 text/html
	 * 4.目标服务器应答超时
	 * </pre>
	 */
	@Override
	public void run() {
		log.info("在线程" + Thread.currentThread().getName() + ",   执行第" + searchDepth + "层搜索.");
		
		
		while (true) {
			/**
			 * URL收集器达到阈值就会休眠
			 * HTML解析器达到阈值同样会休眠
			 */
			if( kind == URL_COLLECTOR){
				/*
				while(pool.getNewUrl().size() > Constant.MAX_NEW_URL * 0.5){
					try {
						System.out.print("。");
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						log.error("未知错误");
					}
				}*/
			}else{
				/*
				while(pool.getNewUrl().size() < Constant.MIN_NEW_URL){
					try {
						System.out.print(". ");
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						log.error("未知错误");
					}
				}*/
			}
			long start = System.currentTimeMillis();
			
			HttpUriRequest request;
			HttpResponse response = null;
			HttpEntity he;
			Page page = new Page();
			String in = pool.getUrl();//从链接池获取链接
			String content;
			Charset cs;
			String utfDoc;
			String rawDoc;
			
			

			/**
			 * 假如不能获取链接，则等待
			 */
			if (in == null) {
				leave(null);
				continue;
			}

			
			page.setUrl(in);
			log.info(" Get a url:" + in);

			/**
			 * 获得一个正确的请求对象
			 */
			try {
				request = new HttpGet(in);
			} catch (IllegalArgumentException e) {
				log.error("URL地址出错:" + in + ",操作中断!");
				continue;
			}

			/**
			 * 发起Http Get请求，尝试获得响应，若无法获得正确的响应，跳过该链接
			 */
			try {
				response = httpClient.execute(request);
			} catch (Exception e) {
				e.printStackTrace();
				leave(response);
				continue;
			}

			he = response.getEntity();

			
			int status = response.getStatusLine().getStatusCode();
			String contentType = he.getContentType().getValue();// 必须是text/html才能解析

			/**
			 * 假如服务器的响应类型不是html类型的话，或者状态码不是200，我们就抛弃这条链接
			 */
			if (status == 200 && contentType.contains("text/html")) {
				log.info("状态码 ：" + status + "-> OK.");
			} else {
				if (status == 200) {
					log.info("响应内容类型错误" + contentType + "-> ERROR.");
				} else {
					log.info("响应状态码  错误" + status + "-> ERROR.");
				}
				leave(response);
				continue;
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
			log.info("获取了未经编码的文本，长度为" +  content.length());
			
			cs = HtmlParser.getCharset(content);
			if (cs == null) {
				log.error("Charset 获取异常！假设其为 UTF-8");
				cs = Charset.forName("UTF-8");
			}

			log.info("编码为" + cs.name());

			/**
			 * 无论原链接的文本采用何种编码，这里都采用UTF-8编码存储文本信息<br>
			 */
			try {
				utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);
				rawDoc = HtmlParser.html2Text(utfDoc);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("未知错误");
				continue;
			}
			log.info("已经获取文本,长度为" + utfDoc.length());
			page.setUtfContent(rawDoc);
			
			
			
			/**
			 * 为page对象写入日期
			 */
			page.setDate(new Date());
			
			page = HtmlParser.getFullInformation(in, utfDoc, page);

			/**
			 * 为page设置md5,根据url获取签名
			 */
			String md5 = MD5Util.string2MD5(page.getTitle());
			page.setMd5(md5);

			
			/**
			 * 对isFull置位
			 */
			if(pool.getNewUrl().size() >= Constant.LIMIT_NEW_URL){
				isFull = true;
			}else{
				isFull = false;	
			}
			
			/**
			 * 从文本中获取新的链接，并存储到链接池中
			 */
			int count = 0;
			if(isFull){
				log.error("系统进入饱和状态，将放弃本次获取的url直至链接池的新链接"
						+ "数量达到一个合适的比例，但保存本次的页面");
			}else{
				List<String> list = HtmlParser.urlDetector(utfDoc);
				
				log.info("将链接保存入page池");
				for (String url : list) {
					pool.addUrl(url);
					count ++;
				}
			}
			
			/**
			 * 收尾工作，准备下次爬取
			 */
			searchDepth++;
			pool.addPage(page);

			if(System.currentTimeMillis() - start > 30000){
				
				log.error("警告，本轮用时过长，严重异常");
			}else{
				
			}
			
			log.info("REPORT AS FOLLOW:\n+  Count of REMAINED URL is " + pool.getNewUrl().size() + ".   Count of VISITED URL is "
					+ pool.getVisitedUrl().size() + "，线程数 " + Thread.activeCount() +" ， 获取页面数为 " 
					+ pool.getPages().size() +" ，本轮计时 " + (System.currentTimeMillis() - start) 
					+"毫秒， 本轮添加了 " + count + "个链接。");

			log.info(page);
			
			

		}

	}
	
	public void leave(HttpResponse response){
		try {
			Thread.sleep(3000);
			if( response != null ){
				EntityUtils.consumeQuietly(response.getEntity());
			}
				
			log.info("休眠中~");
		} catch (Exception e) {
			log.error("未知错误" + e);
		}
	}

}
