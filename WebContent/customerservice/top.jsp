<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<script type="text/javascript">
	function checkLogin() {
		if ("${customerLoginUserData}" == null||"${customerLoginUserData}" == '') {
			alert("你未登陆或者登陆已经超时，请重新扫码登陆!");
			window.location.href = '${pageContext.request.contextPath}/customerservice/go2LoginPage.html';
		}
	}
	checkLogin();
	window.setInterval(checkLogin, 10000);
</script>
</head>
<body>
</body>
</html>