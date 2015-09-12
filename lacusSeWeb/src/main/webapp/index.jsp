<!doctype html>
<%@taglib prefix="s" uri="/struts-tags"%><%@taglib prefix="s" uri="/struts-tags"%>
<%String path=request.getScheme()+"://" +request.getServerName()+":"+request.getServerPort()+request.getContextPath();%>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<body>
<h2>lacusSe</h2>


<form action="search" method="post">
	<input class="text" name="searchWord" type="text" size="26" placeholder="输入关键词吧..." />
    <input class="submit" type="submit" value="Yes"/>
</form>
<hr>

<s:actionmessage/>
<h4> hitwh131110526 </h4>

</body>
</html>
