<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title></title>
<link type="text/css" href="${pageContext.request.contextPath }/page/css/weuix.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath }/page/css/weui.min.css">
</head>

<body>
	<c:if test="${isYLZF!=1 }">
		<h5>二维码商家：佛山市顺德区东方油类实业有限公司</h5>
		<h5 style="color: red;">&nbsp;&nbsp;&nbsp;确认无误</br>
			&nbsp;&nbsp;&nbsp;①长按图片二维码，识别图中二维码</br>
			&nbsp;&nbsp;&nbsp;②输入客户名称，输入金额 <fmt:formatNumber value="${payAmount}" pattern="#.00" type="number" /></br>
			&nbsp;&nbsp;&nbsp;③支付缴费
		</h5>
		<div>
			<img alt="" src="${pageContext.request.contextPath }/page/images/payImg.jpg" style="width: 100%;height: 90%;">
		</div>
	</c:if>
	<c:if test="${isYLZF==1 }">
		<div style="text-align: center;margin-top: 25%;">
			<div class="weui-icon-warn weui-icon_msg" style="font-size: 155px;"></div>
			<div style=""><div style="width: 90%;margin: auto;">此订单暂不允许通过银联支付，请与客服联系， <a class="slide-link" href="tel:075727685533">0757-27685533</a></div></div>
	    </div>
	</c:if>
</body>
</html>
