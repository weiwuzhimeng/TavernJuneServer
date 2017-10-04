<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 添加jstl标签 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form id="itemForm"
		action="${pageContext.request.contextPath }/user/picUpload.action"
		method="post" enctype="multipart/form-data">
		<table width="100%" border=1>
			<tr>
				<td>商品图片</td>
				<td> <input type="file" name="picFile" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="提交" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>