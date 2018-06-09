<%@ page contentType="text/html; charset=UTF8"%>
<%@ page language="java" import="java.util.List,org.sample.dubbo.order.entity.Product"%>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>商品列表</title>
</head>
<body>
<%
List<Product>  products = (List<Product>)request.getAttribute("products");

for(Product product: products){
%>
<p><%=product.getProductName()%>
(<%=product.getPrice()%>)&nbsp;&nbsp;&nbsp;&nbsp;<span><a href="/user/${userId}/shop/${shopId}/product/<%=product.getProductId()%>/confirm">购买</a></span></p>
<%}%>
</body>
</html>