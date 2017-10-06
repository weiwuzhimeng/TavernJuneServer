<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
//请求json，输出是json
function requestJson(){
	$.ajax({
		type:'post',
		url:'${pageContext.request.contextPath }/requestJson.action',
		contentType:'application/json;charset=utf-8',
		//数据格式是json串，商品信息
		data:'{"age":18,"name":"xiaoming"}',
		success:function(data){//返回json结果
			alert(data.name);
		}
	});

}

//请求key/value，输出是json
function responseJson(){
	$.ajax({
		type:'post',
		url:'${pageContext.request.contextPath }/requestJson.action',
		//请求是key/value这里不需要指定contentType，因为默认就 是key/value类型
		//contentType:'application/json;charset=utf-8',
		//数据格式是json串，商品信息
		data:'age=19&name=旺财',
		success:function(data){//返回json结果
			//alert(data.name);
			data;
		}
	});
}
</script>
</head>
<body>
	<form method="post" 
		action="${pageContext.request.contextPath }/user/login.action">
		用户账号：<input type="text" name="username" /><br/> 
		用户密码 ：<input type="password" name="password" /><br/> 
			<input type="submit"value="提交"/>
	</form>
</body>
</html>