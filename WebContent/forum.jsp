<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
	function giveJson(){
		$.ajax({
			type:'post',
			url:'${pageContext.request.contextPath }/forum/upload.action',
			contentType:'application/json;charset=utf-8',
			data:'{"userId":1,"picture":"userPicture","username":"小强","description":"再见2","content":"再见了2","date":"10月4日"}',
			success:function(data){
				data;
			}
		});
	}
</script>
</head>
<body>
	<input type="button" onclick="giveJson()" value="给服务端json数据"/>
</body>
</html>