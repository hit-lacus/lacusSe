package hitwh.yxx.wei.WeiSearch;

import hitwh.yxx.wei.WeiSearch.util.HtmlParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 学习使用Apache的HttpClient<br>
 * 参考链接：http://www.cnblogs.com/loveyakamoz/archive/2011/07/21/2113252.html
 * 
 * @author lacus
 *
 */
@SuppressWarnings("deprecation")
public class HttpClientTest {

	private static Logger log = Logger.getLogger("HttpClientTest.java");

	// @Test
	public void test1() {
		HttpClient httpClient = new DefaultHttpClient();

		// HTTP请求
		HttpUriRequest request = new HttpGet("http://www.baidu.com");

		// 打印请求信息
		System.out.println(request.getRequestLine());
		try {
			// 发送请求，返回响应
			HttpResponse response = httpClient.execute(request);

			// 打印响应信息
			System.out.println(response.getStatusLine());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试如何获得http的应答类型
	 */
	// @Test
	public void test2() {
		HttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("http://www.sina.com");
		try {
			HttpResponse response = httpClient.execute(request);
			String rp = response.getStatusLine().getReasonPhrase();
			int code = response.getStatusLine().getStatusCode();

			HttpEntity he = response.getEntity();

			for (HeaderElement ele : he.getContentType().getElements()) {
				log.info(ele);
			}

			log.info("Content Type = " + he.getContentType().getValue());
			// http://images.cnitblog.com/i/116165/201407/121800390202646.png
			// http://central.maven.org/maven2/com/github/spullara/mustache/java/compiler/0.9.0/compiler-0.9.0.jar
			// http://pan.baidu.com/share/link?shareid=56256858&uk=2636269852
			// http://my.9ku.com/lrcshow.asp?id=27322&m=%C9%CF%BA%A3%CC%B2
			/*
			 * 
			 * String content =
			 * HtmlParser.getHtmlDocWithoutEncoding(he.getContent()); Charset cs
			 * = HtmlParser.getCharset(content);
			 * 
			 * System.out.println(rp + "->" + code);
			 * System.out.println("Paras->" +
			 * response.getParams().getParameter("encoding"));
			 * System.out.println("ContenLength->" + he.getContentLength());
			 * System.out.println("Encode      ->" + he.getContentEncoding());
			 * System.out.println("ContentType ->" + he.getContentType());
			 * System.out.println("Encoding->" + cs);
			 * 
			 * System.out.println("Content after proper encoding->" +
			 * HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).
			 * getEntity().getContent(), cs));
			 * 
			 * 
			 */

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 测试HtmlParser 是否可以解析网页
	 */
	// @Test
	@SuppressWarnings("deprecation")
	public void test() {
		HttpClient httpClient = new DefaultHttpClient();
		// httpClient.

		// http://www.sina.com/sss.html -> 404
		// http://www.sina111.com
		HttpUriRequest request = new HttpGet("http://www.sina111.com");

		try {
			HttpResponse response = httpClient.execute(request);

			// response.
			log.info(response.getStatusLine().getStatusCode());

			HttpEntity he = response.getEntity();

			String content = HtmlParser.getHtmlDocWithoutEncoding(he.getContent());
			Charset cs = HtmlParser.getCharset(content);

			String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);

			// System.out.println("Content after proper encoding->" +
			// HtmlParser.html2Text(utfDoc));
			List<String> list = HtmlParser.urlDetector(utfDoc);
			for (String url : list) {
				log.info(url);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testCharset() {
		Charset cs = Charset.forName("UTF-8");
		log.info(cs);
	}

	/**
	 * 测试如何获得HTTP响应返回时间
	 */
	// @Test
	public void testGetDateOfResponse() {
		HttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("http://www.sina.com");

		try {
			HttpResponse response = httpClient.execute(request);

			for (Header h : response.getAllHeaders()) {
				if (h.getName().equals("Date")) {
					@SuppressWarnings("deprecation")
					Date date = new Date(h.getValue());
					log.info(date);

				}

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Server -> nginx
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Date -> Thu, 27 Aug 2015
		 * 06:06:54 GMT
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Content-Type -> text/html
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Last-Modified -> Thu, 27 Aug
		 * 2015 06:06:40 GMT
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Vary -> Accept-Encoding
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Expires -> Thu, 27 Aug 2015
		 * 06:07:54 GMT
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Cache-Control -> max-age=60
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- X-Powered-By -> shci_v1.03
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Age -> 28
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- Content-Length -> 624864
		 * 
		 * WEI [ INFO ] 08-27 14:06:07
		 * hitwh.yxx.wei.WeiSearch.HttpClientTest.testGetDateOfResponse(
		 * HttpClientTest.java:168) from main -- X-Cache -> HIT from
		 * cnc.jn.18e4.149.spool.sina.com.cn
		 * 
		 * 
		 */

	}

	/**
	 * 测试需要账号登陆的网站，比如
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	// @Test
	//@Test
	public void testWebSiteCookieNeeded() throws Exception {

		HttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("http://enewstree.com/discuz/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&username=HttpClient&password=HttpClient");

		HttpResponse response = httpClient.execute(request);

		String content = HtmlParser.getHtmlDocWithoutEncoding(response.getEntity().getContent());

		Charset cs = HtmlParser.getCharset(content);
		String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);
		for (Header h : response.getAllHeaders()) {
			for (HeaderElement ele : h.getElements()) {
				// log.info(ele.getName() + " -> " + ele.getValue());
			}
		}

		log.info(response.getFirstHeader("Location").getValue());

		// log.info(utfDoc);

	}

	/**
	 * 尝试登录人人网 Email ： hitwh_yxx@163.com Password： 9698198559
	 * 
	 * <pre>
	 *  <form method="post" id="loginForm" class="login-form" action="http://www.renren.com/PLogin.do">
	    <dl class="top clearfix">
	    <dd>
	    <input type="text" name="email" class="input-text" id="email" tabindex="1" value="" />
	    </dd>
	    </dl>
	    <dl class="pwd clearfix">
	    <dd>
	    <input type="password" id="password" name="password" error="请输入密码" class="input-text" tabindex="2"/>
	    <label class="pwdtip" id="pwdTip" for="password">请输入密码</label>
	    <a class="forgetPwd" id="forgetPwd" href="http://safe.renren.com/findPass.do" stats="home_findpassword">忘记密码？</a>
	    </dd>
	    </dl>
	    <div class="caps-lock-tips" id="capsLockMessage" style="display:none"></div>
	    <dl class="savepassword clearfix">
	    <dt>
	    <label title="为了确保您的信息安全，请不要在网吧或者公共机房勾选此项！" for="autoLogin" class="labelCheckbox">
	    <input type="checkbox" name="autoLogin" id="autoLogin" value="true" tabindex="4" />下次自动登录
	    </label>
	    </dt>
	    <dd>
	    <span class="getpassword" id="getpassword"><a href="http://safe.renren.com/findPass.do" stats="home_findpassword">忘记密码？</a></span>
	    </dd>
	    </dl>
	    <dl id="code" class="code clearfix">
	    <dt><label for="code">验证码:</label></dt>
	    <dd>
	    <input id="icode" type="text" name="icode" class="input-text" tabindex="3" autocomplete="off" />
	    <label class="codetip" id="codeTip" for="icode">请输入验证码</label>
	    </dd>
	    </dl>
	    <dl id="codeimg" class="codeimg clearfix">
	    <dt></dt>
	    <dd><img id="verifyPic_login" src="http://icode.renren.com/getcode.do?t=web_login&rnd=Math.random()"/>
	    </dd>
	    <a class="changeone" href="javascript:refreshCode_login();" >换一个</a>
	    </dl>
	    <dl class="bottom">
	    <input type="hidden" name="origURL" value="http://www.renren.com/home" />
	    <input type="hidden" name="domain" value="renren.com" />
	    <input type="hidden" name="key_id" value="1" />
	    <input type="hidden" name="captcha_type" id="captcha_type" value="web_login" />
	    <input type="submit" id="login" class="input-submit login-btn" stats="loginPage_login_button" value="登录" tabindex="5"/>
	    </dl>
	    </form>
	 * </pre>
	 * 
	 * =========================================================================
	 * ================================================
	 * =========================================================================
	 * ================================================
	 * =========================================================================
	 * ================================================
	 * 
	 * <form id="addBlog_editorForm" isedit="new" ugcId="961060381" blogId="0"
	 * action="http://blog.renren.com/NewEntry.do" draftaction=
	 * "http://blog.renren.com/blog/882983487/961060381/autoSave" editaction=
	 * "http://blog.renren.com/blog/882983487/961060381/editBlog" method="post"
	 * onsubmit="return beforeSubmitWithPassWordCheck(this);"
	 * > <div id="addBlog_sideLeft" class="addBlog-box"> <div id=
	 * "addBlog_editorWrap"> <div id="addBlog_blogTitle_box"> <span id=
	 * "addBlog_blogTitle_number"><span id="addBlog_blogTitle_realNumber">90
	 * </span>/100</span>
	 * <textarea placeholder="输入日志标题" autocomplete="off" name="title" id=
	 * "addBlog_blogTitle"></textarea> </div> <textarea name="body"
	 * id="addBlog_blogContent" style="font-size:16px;"
	 * data-privacy='{"control":99,"createTime":"1440776402866","privacyGroupId"
	 * :"0","owner":882983487,"ugcGID":"3602879702857457181","public":true}'>
	 * </textarea> </div>
	 * <div id="addBlog_editorButtons"> <input type="button" id=
	 * "addBlog_cancelBtn" class="ui-button" value="取消"/> <input type="button"
	 * id="addBlog_publishBtn" class=
	 * "ui-button ui-button-blue" value="发布日志" tabindex="4"/> <input type=
	 * "button" id="addBlog_saveAsDraft" class="ui-button" value="保存为草稿"/>
	 * </div> </div>
	 * <div id="addBlog_sideRight"> <div id="addBlog_otherTabs" class=
	 * "addBlog-box"> <div class="addBlog-btn" id="addBlog_filmReview"> <i></i>
	 * <span>影评日志</span> </div>
	 * <div class="addBlog-btn" id="addBlog_draftBox"> <i></i>
	 * <span>草稿箱</span> </div>
	 * <div class="addBlog-btn" id="addBlog_importBlog"> <i></i>
	 * <span>导入日志</span> </div> </div>
	 * <input type="hidden" name="relative_optype" id="relative_optype"/><div
	 * style="display: none;"> <span class=
	 * "privacy-trigger privacy-trigger-small privacy-trigger-input"
	 * id="privacy_new" data-toggle="privacy" data-option='{"ugcType": "blog",
	 * "sourceControl": 99, "extend": "PASSWORD", "alignType": "2-3"}' >
	 * <i class="privacy-icon picon-public"></i> <span>公开</span> <input
	 * type="hidden" value='{"sourceControl": 99}' name="privacyParams">
	 * <i class="privacy-icon picon-arrow-down"></i> </span></div>
	 * <input type="hidden" name="isVip" id="isVip" value="" /> <input type=
	 * "hidden" name="jf_vip_em" id="jf_vip_em" value="true" /> <input type=
	 * "hidden" name="bfrom" value="010203044" /> <input type="hidden" value="0"
	 * id="newLetterId" name="newLetterId" /> <input type="hidden" name=
	 * "blog_pic_id" id="blog_pic_id" value="" /> <input type="hidden" name=
	 * "pic_path" id="pic_path" value="" /> <input type="hidden" name="activity"
	 * id="activity" value="" /> <input type="hidden" name="id" id="id" value=
	 * "961060381" /> <input type="hidden" name="postFormId" id="postFormId"
	 * value="1732263266" /> <div id="addBlog_ownerTabs" class="addBlog-box">
	 * <div id="addBlog_ownerInfo"> <a href=
	 * "http://www.renren.com/profile.do?id=882983487"> <img id=
	 * "addBlog_ownerPortrait" src=
	 * "http://head.xiaonei.com/photos/0/0/men_tiny.gif" /></a>
	 * <div id="addBlog_ownerTitle"> <strong>俞霄翔</strong> </div> </div>
	 * <div id="addBlog_privacySet"> </div>
	 * <div id="addBlog_categorySet" defaultCate="0"> </div>
	 * <div id="addBlog_top"> <div style=
	 * "float: left;height: auto;overflow: hidden;"
	 * > <div id="isTop" class="noSelected" ></div>
	 * <div id="top_text">置顶</div> </div> </div>
	 * <div class="clearfix" style="height:2px;">&nbsp;</div> </div> </div>
	 * </form>
	 * 
	 * 
	 * =========================================================================
	 * ============================================
	 * =========================================================================
	 * ============================================
	 * =========================================================================
	 * ============================================
	 * 
	 * <form class="status-global-publisher" action=
	 * "http://shell.renren.com/882983487/status" method="post"> <div class=
	 * "status-inputer"> <!-- <div class="tl-title"><img src=
	 * "http://a.xnimg.cn/modules/global-publisher/res/tl-title.png"/></div> -->
	 * <dl class="global-publisher-selector clearfix">
	 * <dd class="global-publisher-selector-status"><a class=
	 * "global-publisher-status-trigger" href="javascript:;" title="状态" onfocus=
	 * "this.blur()">状态</a></dd>
	 * <dd class="global-publisher-selector-photo"><a class=
	 * "global-publisher-photo-trigger" href="javascript:;" title="那些照片" onfocus
	 * ="this.blur()">那些照片</a></dd>
	 * <dd class="global-publisher-selector-lifeevent last"
	 * ><a class="global-publisher-lifeevent-trigger" href="javascript:;" title=
	 * "我的故事">我的故事</a></dd>
	 * </dl>
	 * <div class="content-container" style="display:none;"><menu>
	 * <li><a class="close" style="display:none;" href="#" onclick=
	 * "return false;">关闭</a></li> </menu>
	 * <div class="status-textarea"> <textarea name="content" class=
	 * "status-content" defaultpos="" defaultmodule="" placeholder="你正在干嘛？">
	 * </textarea>
	 * <div class="publisher-toolbar" style="display:none"> <a class="at-button"
	 * href="#at" title="点名"></a>
	 * <a class="emotion-button" href="#emotion" title="表情"></a>
	 * <input class="speech-button" style="display:none;" type="text"
	 * x-webkit-speech="speech" lang="zh_CN" title="语音输入" /> <div class=
	 * "chars-info" style="display:none"><span class="chars-remain">240</span>
	 * </div> </div> </div> <div class="global-publisher-modules-box"> </div>
	 * <div class=
	 * "global-publisher-footer clearfix" style="display:none"> <div class=
	 * "global-publisher-date"> <span>发生于</span>
	 * <select class="year-select"><option>2012</option></select>
	 * <select class="month-select"><option>1</option></select>
	 * <select class="day-select"><option>1</option></select>
	 * <a href="javascript:;" class="add-month">添加月份</a>
	 * <a href="javascript:;" class="add-day">添加日期</a>
	 * <input type="hidden" id="tlTime" value="" /> </div>
	 * <div class="global-publisher-actions"> <input type="hidden" name=
	 * "channel" value="renren" /> <a href="#nogo" class="privacy-trigger"
	 * id="privacy_create_status" data-toggle="privacy" data-option='{"ugcType":
	 * "status", "offsetX": 3, "offsetY": 2}'><i class=
	 * "privacy-icon picon-public"></i>\ <input type="hidden" value=
	 * "{&quot;sourceControl&quot;: 99}" name="privacyParams"><span>公开</span>
	 * <i class="privacy-icon picon-arrow-down"></i></a>
	 * <input class="submit" type="submit" value="发布" /> </div>
	 * <div class="last-status" style="display:none;"> <a ui-async="async" title
	 * ="afda" href="http://status.renren.com/status?id=702071657"> 2小时前:afda
	 * </a> </div> </div> </div> </div>
	 * <input type="hidden" name="hostid" value="882983487" /> </form>
	 * 
	 * 
	 * 
	 */
	// @Test
	public void loginRenrenWebsite() {

		String userName = "hitwh_yxx@163.com";
		String password = "9698198559";
		String redirectURL = "http://blog.renren.com/blog/304317577/449470467";
		String renRenLoginURL = "http://www.renren.com/PLogin.do";
		HttpResponse response;
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpost = new HttpPost(renRenLoginURL);
		// All the parameters post to the web site
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("origURL", redirectURL));
		nvps.add(new BasicNameValuePair("domain", "renren.com"));
		nvps.add(new BasicNameValuePair("isplogin", "true"));
		nvps.add(new BasicNameValuePair("formName", ""));
		nvps.add(new BasicNameValuePair("method", ""));
		nvps.add(new BasicNameValuePair("submit", "登录"));
		nvps.add(new BasicNameValuePair("email", userName));
		nvps.add(new BasicNameValuePair("password", password));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			httpost.abort();
		}

		log.info("StatusLine" + response.getStatusLine());

		Header locationHeader = response.getFirstHeader("Location");
		if (locationHeader == null) {
			return;
		}

		String redirectLocation = locationHeader.getValue();

		log.info("redirectLocation:" + redirectLocation);

		HttpGet httpget = new HttpGet(redirectLocation);
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";

		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
		}

		log.info("responseBody:" + responseBody);

	}

	// bbs.onmoon.com/forum.php
	// http://enewstree.com/discuz/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&username=HttpClient&password=HttpClient

	/**
	 * <form method="post" autocomplete="off" id="lsform" action=
	 * "member.php?mod=logging&amp;action=login&amp;loginsubmit=yes&amp;infloat=yes&amp;lssubmit=yes"
	 * onsubmit="return lsSubmit();"> <div class="fastlg cl"
	 * > <span id="return_ls" style="display:none"></span> <div class="y pns">
	 * <table cellspacing="0" cellpadding="0">
	 * <tr>
	 * <td><span class="ftid"> <select name="fastloginfield" id=
	 * "ls_fastloginfield" width="40" tabindex="900"> <option value="username">
	 * 用户名</option> <option value="email">Email</option> </select> </span>
	 * <script type="text/javascript">simulateSelect('ls_fastloginfield')
	 * </script></td>
	 * <td><input type="text" name="username" id="ls_username" autocomplete=
	 * "off" class="px vm" tabindex="901" /></td>
	 * <td class="fastlg_l"><label for="ls_cookietime"> <input type="checkbox"
	 * name="cookietime" id="ls_cookietime" class="pc" value="2592000" tabindex=
	 * "903" />自动登录</label></td>
	 * <td>&nbsp;<a href="javascript:;" onclick=
	 * "showWindow('login', 'member.php?mod=logging&action=login&viewlostpw=1')"
	 * >找回密码</a></td>
	 * </tr>
	 * <tr>
	 * <td><label for="ls_password" class="z psw_w">密码</label></td>
	 * <td><input type="password" name="password" id="ls_password" class=
	 * "px vm" autocomplete="off" tabindex="902" /></td>
	 * <td class="fastlg_l"><button type="submit" class=
	 * "pn vm" tabindex="904" style="width: 75px;"><em>登录</em></button></td>
	 * <td>&nbsp;<a href="member.php?mod=register" class="xi2 xw1">立即注册</a></td>
	 * </tr>
	 * </table>
	 * <input type="hidden" name="quickforward" value="yes" /> <input type=
	 * "hidden" name="handlekey" value="ls" /> </div> </div> </form>
	 * 
	 */
	//@Test
	public void testLoginEnewstree() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();

		HttpHost target = new HttpHost(
				"http://blog.csdn.net/hedan_hd/article/details/7886589",
				8080, "http");

		HttpHost proxy = new HttpHost("proxyforhitwh.oss-cn-beijing.aliyuncs.com");

		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		HttpPost httpPost = new HttpPost("http://blog.csdn.net/hedan_hd/article/details/7886589");

		httpPost.setConfig(config);

		log.info("代理地址：" + proxy.getAddress() + "  HOST " + proxy.getHostName());

		try {
			CloseableHttpResponse response = httpClient.execute(target, httpPost);

			log.info(response.getStatusLine());

			String content = HtmlParser.getHtmlDocWithoutEncoding(response.getEntity().getContent());

			Charset cs = HtmlParser.getCharset(content);
			String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(target, httpPost).getEntity().getContent(),
					cs);

			for (Header h : response.getAllHeaders()) {
				for (HeaderElement ele : h.getElements()) {
					log.info(ele.getName() + " -> " + ele.getValue());
				}
			}

			log.info(response.getFirstHeader("Location").getValue());

			log.info(utfDoc);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		httpClient.getConnectionManager().shutdown();

	}

	@Test
	public void testLoginEnewstreeWithoutProxy() {

		HttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("http://enewstree.com/discuz/member.php?"
				+ "mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&username=HttpClient&password=HttpClient");

		HttpResponse response;
		try {
			response = httpClient.execute(request);

			log.info(response.getStatusLine());
			String content = HtmlParser.getHtmlDocWithoutEncoding(response.getEntity().getContent());
			Charset cs = HtmlParser.getCharset(content);
			String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);

			for (Header h : response.getAllHeaders()) {
				for (HeaderElement ele : h.getElements()) {
					log.info(ele.getName() + " -> " + ele.getValue());
				}
			}

			log.info(utfDoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//@Test
	public void testProxy() {

		DefaultHttpClient httpClient = new DefaultHttpClient();

		/**
		 * 123.56.160.226:36152
		 */
		String proxyHost = "123.56.160.226";
		int proxyPort = 36152;
		String userName = "";
		String password = "";
		httpClient.getCredentialsProvider().setCredentials(new AuthScope(proxyHost, proxyPort),
				new UsernamePasswordCredentials(userName, password));
		HttpHost proxy = new HttpHost(proxyHost, proxyPort);

		httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

		//http://enewstree.com/discuz/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&username=HttpClient&password=HttpClient
		HttpUriRequest request = new HttpGet("http://blog.csdn.net/hedan_hd/article/details/7886589");

		HttpResponse response;
		try {
			response = httpClient.execute(request);

			log.info(response.getStatusLine());
			String content = HtmlParser.getHtmlDocWithoutEncoding(response.getEntity().getContent());
			Charset cs = HtmlParser.getCharset(content);
			String utfDoc = HtmlParser.getHtmlDocWithUtf8(httpClient.execute(request).getEntity().getContent(), cs);

			for (Header h : response.getAllHeaders()) {
				for (HeaderElement ele : h.getElements()) {
					log.info(ele.getName() + " -> " + ele.getValue());
				}
			}

			log.info(utfDoc);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
