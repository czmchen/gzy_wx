<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weui.css" rel="stylesheet">
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weuix.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-3.5.1.min.js"></script>
<style type="text/css">
	body{
		background: black;
	}
</style>
</head>
<body>
 	<div style="position:absolute; top:42%; left:42%;">
		<div><img alt="" src="" id="qrCodeImg" width="300px" height="300px;"></div>
		<br/>
		<div>
			<div class="weui-icon-warn" style="display:inline" id="qrStatus"></div>
			<div style="font-size: 18px; color: white;display:inline">请扫码登陆甘竹花生油质检报告系统</div>
		</div>
	</div>
<input type="hidden" id="qrCode">        
<script type="text/javascript">
	function initLoginCode(){
		$.ajax({
			url:'${pageContext.request.contextPath }/goodsReport/qrcode.html',
			type:'get',
			success:function(rs){
				$("#qrCodeImg").val(rs);
				$("#qrCodeImg").attr("src","${pageContext.request.contextPath}/goodsReportManager/common/img/qrcode/"+rs+".jpg");
			}
		})
	}
	
	function checkLoginStatus(){
		$.ajax({
			url:'${pageContext.request.contextPath }/goodsReport/checkLogin.html',
			data:'uuid='+$("#qrCodeImg").val(),
			type:'get',
			success:function(rs){
				if(rs==1){
					$("#qrStatus").removeClass("weui-icon-warn");
					$("#qrStatus").addClass("weui-icon-success");
					window.setTimeout(function(){
						window.location.href='${pageContext.request.contextPath}/goodsReport/go2index.html?uuid='+$("#qrCodeImg").val();
					},2000);
				}
			}
		})
	}
	
	initLoginCode();
	window.setInterval(checkLoginStatus,3000);
</script>
</body>
</html>