package hitwh.yxx.lacusSe.web.action;

import hitwh.yxx.lacusSe.web.pojo.ResultItem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller("search")
@Scope("prototype")
public class SearchAction extends ActionSupport  implements ServletRequestAware,ServletResponseAware{
	/***/
	private static String temp  = "http://127.0.0.1:8983/solr/collection1/"
			+ "select?wt=json&indent=true&rows=10&hl.fl=title&q=";
	private static String keyword = "杨幂";
	private static HttpClient httpClient = new DefaultHttpClient();
	private static Logger log = Logger.getLogger("SearchTest.java");
	private HttpServletRequest request1;
	private HttpServletResponse response1; 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String search(){
		HttpUriRequest request;
		HttpResponse response = null;
		HttpEntity he = null;
		String url = null;
		
		/*
		Map<String,Object> params =  ActionContext.getContext();
		System.out.println(params);
		for(Entry<String, Object> o : params.entrySet()){
			System.out.println(o.getKey() + " " + o.getValue());
		}*/
		
		
		
		keyword = request1.getParameter("searchWord");
		
		request1.getRequestURI();
		
		System.out.println(request1.getRequestURI());
		if( keyword == null)  keyword= "小时代";
		try {
			url = temp + URLEncoder.encode(keyword,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		log.info(url);
		
		/**
		 * 获得一个正确的响应对象
		 */
		try {
			request = new HttpGet(url);
			response = httpClient.execute(request);
			he = response.getEntity();
		} catch (IllegalArgumentException e) {
			log.error("URL地址出错:" + url + ",操作中断!");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int status = response.getStatusLine().getStatusCode();
		String contentType = he.getContentType().getValue();// 必须是text/html才能解析
		log.info("状态码：" + status + "，响应类型：" + contentType);
		
		String content = null;
		try {
			content = HtmlParser.getHtmlDocWithoutEncoding(he.getContent());
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//log.info(content);
		
		ObjectMapper mapper = new ObjectMapper();  
        JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
        JsonNode header = rootNode.path("responseHeader");
        JsonNode body = rootNode.path("response");
        JsonNode docs = body.path("docs");
        JsonNode hl = body.path("highlighting");
        
        String qStatus = header.get("status").asText();
        String qTime   = header.get("QTime").asText() + "ms ";
        String docsCount = body.path("numFound").asText();
        
		log.info("状态码：" + qStatus + " 用时：" + qTime + " 结果数：" + docsCount);
		
		List<ResultItem> resultList = new ArrayList<ResultItem>();
		
		
		for(JsonNode n : docs){
			String t = n.path("title").asText();
			String u = n.path("url").asText();
			ResultItem ri = new ResultItem();
			ri.setTitle(t);
			ri.setUrl(u);
			resultList.add(ri);
		}
		
		int i = 0;
		for(JsonNode n : hl){
			String high = n.path("title").asText();
			log.info(hl);
			ResultItem ri = resultList.get(i);
			ri.setHighLight(high);
			i ++;
		}	
		log.info(resultList);	
		
		request1.setAttribute("results", resultList);
		//addActionMessage("  Message of 131110526 \n" + resultList);
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request1 = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response1 = arg0;	
	}

}
