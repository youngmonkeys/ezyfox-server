<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Product List</title>
</head>
<body>
    <div align="center">
        <table width="80%" border="1" style="border-collapse: collapse;">
            <tr>
                <th>No</th>
                <th>Product Name</th>
                <th>Description</th>
                <th>Price</th>
            </tr>
            <s:iterator value="products" status="stat">
                <tr>
                    <td><s:property value="#stat.count" /></td>
                    <td><s:property value="name" /></td>
                    <td><s:property value="description" /></td>
                    <td><s:property value="price" /></td>
                </tr>
            </s:iterator>         
        </table>
    </div>
</body>
</html>