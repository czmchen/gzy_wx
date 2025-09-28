<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>线下商店购买</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/css/weui.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/css/jquery-weui.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/animate.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/global.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/loading.css">
<script type="text/javascript" src="https://mapapi.qq.com/web/mapComponents/geoLocation/v/geolocation.min.js"></script>

</head>
<body>
<form id="searchForm">
	<input type="hidden" name="latitude" id="latitude" value=""/>
	<input type="hidden" name="longitude" id="longitude" value=""/>
</form>
<!-- body 最后 -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-weui.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>
<script src="${pageContext.request.contextPath}/page/distribution/js/loading.js"></script>

	
<script>
	
	window.addEventListener('message', function(event) {
        // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
        var loc = event.data;
    }, false);
    var geolocation = new qq.maps.Geolocation("ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3", "ganzhuyou");
 	var options = {timeout: 10000};
 	
	function getPreciseLocation(){
		geolocation.getLocation(showQQMapsPosition, showErr, options);//初始获取当前位置
	}
	
    function showQQMapsPosition(position) {
   	 	geolocation.watchPosition(showWatchPosition);//一直监控定位开启
    };
    
    var isGetLocation = 0;
    function showWatchPosition(position) {
        $('#latitude').val(position.lat);
		$('#longitude').val(position.lng);
		isGetLocation = 1;
    }
    
	function showErr() {
		//geolocation.clearWatch();
		$.alert("GPS定位失败！受天气、位置、环境影响，获取位置超时，请打开定位再刷新本页面重试！");
	};
</script>
	
	
<script type="text/javascript">
	wx.config({
	    debug: false,
	    appId: "${objWxJSApiInfo.appId}",
	    timestamp: "${objWxJSApiInfo.timestamp}",
	    nonceStr: "${objWxJSApiInfo.nonceStr}",
	    signature: "${objWxJSApiInfo.signature}",
	    jsApiList: ['checkJsApi', 'getLocation', 'openLocation']
	});
	wx.checkJsApi({
   	  jsApiList: ['checkJsApi', 'getLocation', 'openLocation'], // 需要检测的 JS 接口列表，所有 JS 接口列表见附录2,
   	  success: function(res) {
   		  console.log("checkJsApi:"+res);
   	  // 以键值对的形式返回，可用的 api 值true，不可用为false
   	  // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
   	  }
   	});
	wx.error(function(res){
		alert('微信config失败：'+ res);
	});
	
	wx.ready(function(){// config信息验证后会执行 ready 方法，所有接口调用都必须在 config 接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在 ready 函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在 ready 函数中。
		wx.getLocation({
			   type: 'wgs84',
		       success: function (res) {
		         if(res.errMsg.indexOf('ok')!=-1){
		        	 /* $('#latitude').val(res.latitude);
	   		         $('#longitude').val(res.longitude); */
		        	//这里判断，如果没有加载数据，会显示loading    
				    $('body').loading({
						loadingWidth:240,
						title:'请稍等!',
						name:'loadingDataList',
						discription:'正在加载数据中...',
						direction:'column',
						type:'origin',
						originDivWidth:40,
						originDivHeight:40,
						originWidth:6,
						originHeight:6,
						smallLoading:false,
						loadingMaskBg:'rgba(0,0,0,0.2)'
				    });
	   		         
	   		      	getPreciseLocation();
	   		         
	       			setTimeout(function (){
	       				genearByStore();
				    }, 1000);
		         }
		       },
		       cancel: function (res) {
		       	$.alert("拒绝授权获取地理位置！设置失败!");
		       }
		    });
	});
	
	
	var runTimes = 0;
	
	function genearByStore(){
		if(isGetLocation==0){
			if(runTimes==20){//一直无获取到为止，则8S后刷新一次位置
				runTimes++;
				$.alert("GPS定位失败！受天气、位置、环境影响，获取位置超时，确定后开始自动刷新本页面！");
				location.reload();
			}
			setTimeout(function (){
				//getNearLocation();
   				genearByStore();
		    }, 500);
			return ;
		}
		
		$.ajax({
			url : '${pageContext.request.contextPath}/page/getNearByStore.html',
			data : $('#searchForm').serialize(),
			type : "POST",
			dataType : "json",
			beforeSend: function(data){
			},success : function(data) {
				if (data != null && data != "null") {
					var marketVal = "";
					var item = data;
					var khmc = item.kHMC;
					var cityPickerCN = item.cityPickerCN;
					var crm_addr_name = item.crm_addr_name;
					var longitude = item.longitude;
					var latitude = item.latitude;
					var reLongitude = item.reLongitude;
					var reLatitude = item.reLatitude;
					//marketVal += reLongitude+","+reLatitude+","+crm_addr_name+"|";
					setTimeout(function (){
						wx.openLocation({
						    latitude: Number(reLatitude),
						    longitude: Number(reLongitude),
						    name: khmc,
						    address: cityPickerCN+crm_addr_name,
						    scale: 14,
						    infoUrl: 'https://ganzhuyou.cn'
					  	 });
						$("#locationPopup").popup();
				    }, 500);
				}else{
					$.alert("您附近暂无经销商！");
				}
			},complete: function(data){//数据加载成功后调用的方法  complete()
				removeLoading('loadingDataList');
		    }, error: function(XMLHttpRequest, textStatus, errorThrown) {
				removeLoading('loadingDataList');
				$.alert("加载数据失败，请再次操作！");
			}
		});
	}
</script>
</body>
</html>