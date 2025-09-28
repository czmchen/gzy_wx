<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>操作结果反馈</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<!-- Bootstrap -->
<script src="js/jquery.min.js"></script>
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weui.css" rel="stylesheet">
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weuix.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/page/distribution/js/jweixin-1.6.0.js"></script>
<script src="${pageContext.request.contextPath}/page/distribution/js/loading.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/loading.css">


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
		 function onBridgeReady(){
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
		         		go2URL();
		         	}
		         }
		     }, 1000);
		 }
		 
		 function go2URL(){
			 $('body').loading({
				loadingWidth:240,
				title:'系统消息提醒',
				name:'loadingSubmit',
				discription:'正在跳转中，请稍等...',
				direction:'column',
				type:'origin',
				originDivWidth:40,
				originDivHeight:40,
				originWidth:6,
				originHeight:6,
				smallLoading:false,
				loadingMaskBg:'rgba(0,0,0,0.2)'
		    });
			 window.location.href = "${returnURL}";
			 removeLoading('loadingSubmit');
		 }
	 
		 if (typeof WeixinJSBridge == "undefined") {
		    if (document.addEventListener) {
		        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
		    } else if (document.attachEvent) {
		        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
		        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		    }
		}else {
		    onBridgeReady();
		}
        sessionStorage.setItem('wxClose', 'close');
        
        
    </script>
</head>
<body>
	<div class="content" style="text-align:center;vertical-align: middle;">
		<div class="${operResult==1?'weui-icon-success weui-icon_msg':'weui-icon-warn weui-icon_msg' }"></div>
		<c:if test="${operType=='close'}"><div><a href="javascript:;">操作${operResult==1?'成功':'失败'}，请等待<span id="second">3</span>秒后自动关闭，或者手动关闭!</a></div></c:if>
		<c:if test="${operType==''||operType==null}"><div><a href="javascript:;" onclick="go2URL();">操作${operResult==1?'成功':'失败'}，请等待<span id="second">3</span>秒后跳转，或点击跳转</a></div></c:if>
		<c:if test="${operResult==0}"><br><div style="font-size: 18px;color:red;">错误信息：${errorMsg }</div></c:if>
	</div>
</body>
</html>