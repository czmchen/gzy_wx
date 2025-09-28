<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>下单结果反馈</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<!-- Bootstrap -->
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weui.css" rel="stylesheet">
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weuix.css" rel="stylesheet">

<style type="text/css">
 		html,body {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }
        .content {
            width: 300px;
            height: 300px;
            margin: 0 auto; /*水平居中*/
            position: relative;
            top: 50%; /*偏移*/
            transform: translateY(-50%);
        }
        
        .weui-icon_msg {
		    font-size: 139px;
		}
</style>

 <script type="text/javascript" >
        onload = function () {
            var span = document.getElementById("second");
            var intervalId = setInterval(function () {
                var numstr = span.innerHTML;
                var num = parseInt(numstr);
                num--;
                span.innerHTML = num;
                if (num <= 0) {// 停下计时器
                    clearInterval(intervalId);
                	if("${operType}"=="close"){
                		if(sessionStorage.getItem('wxClose')){
                		       //这个可以关闭安卓系统的手机
                		       document.addEventListener('WeixinJSBridgeReady', function(){         
                		                  WeixinJSBridge.call('closeWindow'); }, false);
                		       //这个可以关闭ios系统的手机
                		                WeixinJSBridge.call('closeWindow');
                		 
                		}
                	}if("${operType}"==""||"${operType}"==null){
                		window.location.href = "${returnURL}";
                	}
                }
            }, 1000);
        };
        sessionStorage.setItem('wxClose', 'close');
        
        
    </script>
</head>
<body>
	<div class="content" style="text-align:center;vertical-align: middle;">
		<div class="weui-icon-success weui-icon_msg"></div>
		<a href="${returnURL}">
			<div>单号：${orderNum}</div>
			<div>订单下单成功，请等待<span id="second">3</span>秒后跳转，或点击跳转</div>
		</a>
	</div>
</body>
</html>