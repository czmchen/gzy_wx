<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>客户注册</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
	<script type="text/javascript">
		var pageURI = "${pageContext.request.contextPath }";
	</script>
<!-- Bootstrap -->
<link type="text/css" href="css/weui.css" rel="stylesheet">
<link type="text/css" href="css/weuix.css" rel="stylesheet">

<link rel="stylesheet" href="css/weui.min.css">
<link rel="stylesheet" href="css/jquery-weui.min.css">
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
</head>
<body>
<div class="content" style="text-align:center;vertical-align: middle;">
		<c:if test="${operResult==1}">
			<c:if test="${order.expressCost==0}">
				<div class="weui-icon-success weui-icon_msg"></div>
				<div><a href="javascript:;">领取成功，请等待<span id="second">3</span>秒后自动关闭，或者点此手动关闭!</a></div>
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
				           	if("${order.expressCost==0}"){
				           		if(sessionStorage.getItem('wxClose')){
				           		       //这个可以关闭安卓系统的手机
				           		       document.addEventListener('WeixinJSBridgeReady', function(){         
				           		                  WeixinJSBridge.call('closeWindow'); }, false);
				           		       //这个可以关闭ios系统的手机
				           		                WeixinJSBridge.call('closeWindow');
				           		 
				           		}
				           	}
				           }
				       }, 1000);
				  	};
				  	sessionStorage.setItem('wxClose', 'close');
				</script>
			</c:if>
			<c:if test="${order.expressCost>0}">
				<div class="weui-icon-info weui-icon_msg"></div>
				<div><a href="javascript:;">领取成功，但您所处区域非免运费！请支付运费￥<span style="color: red;font-size: 25px;">${order.expressCost }</span>元！<span id="second">3</span>秒后自动弹出支付窗口!</a></div>
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
					           	 	WeixinJSBridge.invoke('getBrandWCPayRequest',
					         			{
					         			 "appId" : "${payDataAttr['appId']}","timeStamp" : "${payDataAttr['timeStamp']}", "nonceStr" : "${payDataAttr['nonceStr']}", "package" : "${payDataAttr['package']}","signType" : "${payDataAttr['signType']}", "paySign" : "${payDataAttr['paySign']}" 
					            			},function(res){
					         				WeixinJSBridge.log(res.err_msg);
				//	          				alert(res.err_code + res.err_desc + res.err_msg);
					         	            if(res.err_msg == "get_brand_wcpay_request:ok"){  
					         	                alert("微信支付成功!");  
					         	                window.location.href='orderList.html';
					         	            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
					         	                alert("用户取消支付!");  
					         	            }else{  
					         	               alert("支付失败!");  
					         	            }  
				         			})
					           	}
					       }, 1000);
					   };
				</script>
			</c:if>
		</c:if>
		<c:if test="${operResult==0}">
			<div class="weui-icon-warn weui-icon_msg"></div>
			<br><div style="font-size: 18px;color:red;">领取失败：${errorMsg }</div>
		</c:if>
</div>
<!-- body 最后 -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-weui.min.js"></script>

</body>
</html>