<%
	String path = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>lacusSe</title>

<link rel="stylesheet" type="text/css"
	href="<%=path%>/style/styles.css" />
</head>

<body>

	<div id="page">

		<h1>LacusSe Powered Site Search</h1>

		<s:form id="searchForm" method="post" action="search.action">
			<fieldset>
			    <!--<s:textfield id ="s" name="searchWord" />-->
				<input id="s" type="text" name="searchWord"/>
				
				<div id="searchInContainer"> It is powered by lacusSe 1.0 .</div>
				<input type="submit" value="Submit" id="submitButton" />
			</fieldset>
		</s:form>

		<div id="resultsDiv">
			<s:iterator value="#request.results" id = "re">
				<div class="webResult">
					<h2>
						<a href="" target="_blank"><s:property value="#re.title"/></a>
					</h2>
					<p><s:property value="#re.highLight"/></p>
					<a href="<s:property value="#re.url"/>" target="_blank"><s:property value="#re.url"/></a>
				</div>	
			</s:iterator>

			
		</div>

	</div>


	<script src="<%=path%>/style/js/jquery.min.js"></script>
	<script src="<%=path%>/style1/js/script.js"></script>
	<div style="text-align: center; clear: both">
		<p>lacusSeeker + MYSQL5.6 + SOLR4.8 + SSH框架</p>
		<br />
		<p>
			author：<a href="#" target="_blank">hitwh 131110526</a>
		</p>
	</div>
</body>
</html>

