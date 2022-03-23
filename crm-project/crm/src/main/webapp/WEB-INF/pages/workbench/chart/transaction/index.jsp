<%--
  Created by IntelliJ IDEA.
  User: 辛黎明
  Date: 2022/3/16
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+
            request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {

        })
    </script>
</head>
<body>
<div id="main" style=""></div>
</body>
</html>
