<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>welcome</title>
</head>
<script type="text/javascript">
	function go2Focus(){
		windows.location.href="https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzA3MjI3MTI1Mw==&scene=110#wechat_redirect";
	}
</script>
<body>
	<jsp:forward page="${pageContext.request.contextPath }/page/go2Order.html" />
	<%-- <c:if test="${isWXFocus==null||isWXFocus==-1}">
		<h5>甘竹油温馨提示：您未关注公众号，请长按图片二维码，识别图中二维码进行关注，或者自行搜索"甘竹花生油"进行关注下单。</h5>
		<div>
			<img alt="" src="images/qrcode_for_gh_16a43faeed1e_1280.jpg" onclick="go2Focus();" style="width: 100%;height: 90%;">
		</div>
	</c:if>
	<c:if test="${isWXFocus!=null&&isWXFocus==1}">
		<jsp:forward page="${pageContext.request.contextPath }/page/go2Order.html" />
	</c:if> --%>
</body>
</html>