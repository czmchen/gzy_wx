<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>司机送货</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, shrink-to-fit=no">

<link rel="stylesheet" href="${pageContext.request.contextPath}/page/css/weui.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/css/jquery-weui.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/animate.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/global.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/loading.css">
<style type="text/css">
	.weui-form-preview__bd {
	    text-align: left;
	}
	body {
	    font-size: 12px;
	}
	.weui-icon_msg {
   		font-size: 28px;
	}
	.sortClass{
	    float: right;
	    font-size: 16px;
	    font-weight: bold;
	    margin-inline: auto;
	    background-image: url(${pageContext.request.contextPath}/page/distribution/images/numberCount.png);
	    width: 37px;
	    height: 37px;
	    color: white;
	    text-align: center;
	}
	.nowLocation{
		float: right;
	    margin-inline: auto;
	    margin-right: 10px;
	}
    #container {
        width: 100%;
        height: 100%;
    }
    #panel {
        position: fixed;
        background-color: white;
        max-height: 90%;
        overflow-y: auto;
        top: 10px;
        right: 10px;
        width: 280px;
    }
    #panel .amap-lib-driving {
	    border-radius: 4px;
        overflow: hidden;
    }
    .floatDiv{
		position:fixed;
		border-radius:25px;
		z-index: 999999;
	}
	#refreshContainer {background-color: #ffffff;margin-bottom: 1px; }
	.refreshText{ position: absolute; width: 100%; line-height: 50px; text-align: center; left: 0; top: 0; }
</style>

<script type="text/javascript">
  window._AMapSecurityConfig = {
	  /* serviceHost:'http://127.0.0.1:8000/_AMapService' */
	  securityJsCode:'c4b3ed22a89083edbd3b6d994e4814e8',
  }
</script>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=7d87e02d3cab51a672c63df409cd8bae"></script>
</head>
<body>
	<p class="refreshText"></p>
	<div id="refreshContainer">
		<form action="" id="searchForm">
		 <div class="weui-cell">
		    <div class="weui-cell__bd">
		      <input class="weui-input" type="date" value="" id="recDate" name="recDate">
		    </div>
		    <!-- <div class="weui-cells_checkbox">
			  <label class="weui-cell weui-check__label" for="s11">
			    <div class="weui-cell__hd">
			      <input type="checkbox" class="weui-check" name="recStatus" value="0" id="s11" checked="checked">
			      <i class="weui-icon-checked"></i>
			    </div>
			    <div class="weui-cell__bd">
			      <p>待送</p>
			    </div>
			  </label>
			</div> -->
			<div class="weui-cell__bd">
		      <input class="weui-input" placeholder="全文模糊检索" name="goodsDetail" id="goodsDetail">
		    </div>
		  </div>
		</form>
		<div id="dataList"></div>	
	</div>        

<!-- body 最后 -->
<script src="${pageContext.request.contextPath}/page/distribution/js/weui.min.js"></script>
<script src="${pageContext.request.contextPath}/page/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/page/js/jquery-weui.min.js"></script>
<script src="${pageContext.request.contextPath}/page/distribution/js/loading.js"></script>

<div id="locationPopup" class="weui-popup__container">
  <div class="weui-popup__overlay"></div>
  <div class="weui-popup__modal">
    <iframe id="mapPage" width="100%" height="100%" frameborder="0" allow="" src="">
	</iframe>
  </div>
</div>
<div id="storeImgPopup" class="weui-popup__container">
  <div class="weui-popup__overlay"></div>
  <div class="weui-popup__modal">
	 <div class="weui-cells__title">店铺图片上传</div>
	 <div class="weui-cells weui-cells_form" id="uploader">
	    <div class="weui-cell">
	        <div class="weui-cell__bd">
	            <div class="weui-uploader">
	                <div class="weui-uploader__hd">
	                    <p class="weui-uploader__title">图片列表</p>
	                    <div class="weui-uploader__info">
	                        <span id="uploadCount">0</span>/5
	                    </div>
	                </div>
	                <div class="weui-uploader__bd">
	                    <ul class="weui-uploader__files" id="uploaderFiles"></ul>
	                    <div class="weui-uploader__input-box">
	                        <input id="uploaderInput" class="weui-uploader__input" type="file"
	                               accept="image/*" multiple>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
        <div><a href="javascript:;" class="weui-btn weui-btn_primary close-popup" onclick="closeStoreImgPopup();" style="margin-top: 36px;">关闭</a></div>
	</div>
  </div>
</div>

<div class="floatDiv" id="floatDiv" style="display: none;"><img src="${pageContext.request.contextPath}/page/distribution/images/back.png"></div>

<input type="hidden" id="latitude" value=""/>
<input type="hidden" id="longitude" value=""/>


<script>
	var storeImgId = "";
	var pullDownFlushFlag = 0;
	var msgNoteMaxTime = 1;
	AMap.plugin('AMap.Geolocation', function() {
        var geolocation = new AMap.Geolocation({
        	enableHighAccuracy: true,//是否使用高精度定位，默认:true
 	        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
 	       	noIpLocate:1,			 //
 	        needAddress:true,        //获取返回的地址信息。
 	        maximumAge: 0,           //定位结果缓存0毫秒，默认：0
 	        convert: true,           //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
 	        showButton: true,        //显示定位按钮，默认：true
 	        buttonPosition: 'RB',    //定位按钮停靠位置，默认：'RB'，右下角
 	        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
 	        showMarker: true,        //定位成功后在定位到的位置显示点标记，默认：true
 	        showCircle: true,        //定位成功后用圆圈表示定位精度范围，默认：true
 	        panToLocation: false,     //定位成功后将定位到的位置作为地图中心点，默认：true
 	        zoomToAccuracy:false      //定位成功后自动调整地图视野到定位到的位置
        });
        
        geolocation.getCurrentPosition(function(status,result){
            if(status=='complete'){
                onComplete(result);
            }else{
                onError(result)
            }
        });
        
         setInterval(function () {
        	geolocation.getCurrentPosition(function(status,result){
                if(status=='complete'){
                    onComplete(result);
                }
            });
        }, 5000);  // 每秒钟更新一次定位点位置
    });
	
	 //解析定位结果
    function onComplete(data) {
   	   $('#latitude').val(data.position.lat);
   	   $('#longitude').val(data.position.lng);
    }
	 
    //解析定位错误信息
    var msgNoteTime = 0;
    function onError(data) {
    	if(msgNoteTime<msgNoteMaxTime){
    	   msgNoteTime++;
	 	   alert("定位超时，请打开定位服务！");
    	}
    }

	function settingNowLocation2DB(latitude,longitude){
		$.confirm({
			  title: '位置选取服务',
			  text: '是否确认选取当前位置为精准送货地址',
			  onOK: function () {
				  $.ajax({
						url : '${pageContext.request.contextPath}/distribution/setttingNowLocation.html?id='+dataRowId+"&latitude="+latitude+"&longitude="+longitude,
						type : "POST",
						dataType : "json",
						async : false,
						success : function(data) {
							if (data == 1) {
								$('#nowLocationImgId_'+dataRowId).attr("src","${pageContext.request.contextPath}/page/distribution/images/nowLocation.png");
								$('#gaodeNavigation_'+dataRowId).attr("latitudeContent",latitude);
								$('#gaodeNavigation_'+dataRowId).attr("longitudeContent",longitude);
								dataRowId = "";
								$.alert("设置成功！");
								$.closePopup();
							}else {
								$.alert("设置失败，请重新再试！");
							}
						}
				  });
			  },
			  onCancel: function () {
			  }
			});
	}
	
</script>
<script type="text/javascript">
	function listData() {
		$.ajax({
			url : '${pageContext.request.contextPath}/distribution/list.html',
			data : $('#searchForm').serialize(),
			type : "POST",
			dataType : "json",
			//async : false,
			beforeSend: function(data){
			   //这里判断，如果没有加载数据，会显示loading    
			   if(data.readyState == 0){
				   pullDownFlushFlag = 0;
				   $('body').loading({
						loadingWidth:240,
						title:'请稍等!',
						name:'loadingDataList',
						discription:'正在加载数据中...',
						direction:'column',
						type:'origin',
						// originBg:'#71EA71',
						originDivWidth:40,
						originDivHeight:40,
						originWidth:6,
						originHeight:6,
						smallLoading:false,
						loadingMaskBg:'rgba(0,0,0,0.2)'
				   });
			   }
			},success : function(data) {
				if (data != null && data != "null" && data.length>0) {
					showData(data);
				}else{
					$("#dataList").empty();
				}
			},complete: function(data){//数据加载成功后调用的方法  complete()
				pullDownFlushFlag = 1;
				removeLoading('loadingDataList');
		    }, error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	pullDownFlushFlag = 1;
				removeLoading('loadingDataList');
				$.alert("加载数据失败，请再次操作！");
			}
		});
	}
	
	function showData(data){
		var showTemp = "";
		for(var index in data){
			var item = data[index];
			var recieverData = item.reciever;
			var custStoreImgData = item.custStoreImg;
			var custStoreJson = "";
			if(custStoreImgData!='[]'&&custStoreImgData!=''&&custStoreImgData!=null&&custStoreImgData!=undefined){
				custStoreJson = encodeURI(custStoreImgData);
			}
			showTemp+="<div class=\"weui-form-preview\">                                                                                                                                                          "
				+"	  <div class=\"weui-form-preview__hd\">                                                                                                                                                   "
				+"	    <div style=\"text-align: left;line-height: 1em;\"><span id='title_span_"+item.id+"' custStoreJson='"+custStoreJson+"' style='"+((custStoreImgData=='[]'||custStoreImgData==''||custStoreImgData==null||custStoreImgData==undefined)?"color: red;":"")+"' onclick=\"go2StoreImg('"+item.id+"','"+item.openid+"','"+item.custId+"','"+custStoreJson+"')\">"+item.reciever+"</span><span><img onclick=\"shrinkData('"+item.orderNum+"');\" id=\"shrinkImg_"+item.orderNum+"\" style=\"float: right;\" src=\"${pageContext.request.contextPath }/page/distribution/images/shrink_down.png\"/></span></div>                                                                                                          "
				+"	  </div>                                                                                                                                                                                  "
				+"	  <div class=\"weui-form-preview__bd\">                                                                                                                                                   "
				+"	  <div id=\"shrink_"+item.orderNum+"\">                                                                                                                                               "
				+"	    <div class=\"weui-form-preview__item\">                                                                                                                                               "
				+"	      <label class=\"weui-form-preview__label\">订单号</label>                                                                                                                            "
				+"	      <span class=\"weui-form-preview__value\">"+item.orderNum+"</span>                                                                                                                   "
				+"	    </div>                                                                                                                                                                                "
				+"	    <div class=\"weui-form-preview__item\" style=\"display: inline-flex;\">                                                                                                               "
				+"	      	<label class=\"weui-form-preview__label\">商品信息</label>                                                                                                                        "
				+"	      	<span class=\"weui-form-preview__value\">"+item.goodsDetail+"                                                                                                                      "
				+"			</span>                                                                                                                                                                           "
				+"			<span style=\"margin-left: 10px;\">"+(item.signStatus==1?"<img alt=\"签收状态\" src=\"${pageContext.request.contextPath }/page/distribution/images/signed.png\"/>":"")+"</span>                                 "
				+"	    </div>                                                                                                                                                                                "
				+"	    <div class=\"weui-form-preview__item\">                                                                                                                                               "
				+"	      <label class=\"weui-form-preview__label\">送货地址</label>                                                                                                                          "
				+"	      <span class=\"weui-form-preview__value\">"+item.address+"</span>                                                                                  "
				+"	    </div>                                                                                                                                                                                "
				+"	  </div>                                                                                                                                                                                "
				+"	    <div class=\"weui-form-preview__item\">                                                                                                                                               "
				+"	      <label class=\"weui-form-preview__label\" style=\"margin-top: 6px;\">电话</label>                                                                                                   "
				+"	      <span class=\"weui-form-preview__value\">";
				var mobilePhones = item.mobileNum.split(";");
				for(var iMobile = 0;iMobile<mobilePhones.length;iMobile++){
					showTemp+="	      	<span>"+mobilePhones[iMobile]+"</span><span style=\"vertical-align: middle;\">&nbsp;&nbsp;<a href=\"tel:"+mobilePhones[iMobile]+"\"><img alt=\"打电话\" src=\"${pageContext.request.contextPath}/page/distribution/images/call.png\"/></a></span>";
				}
				showTemp+="	      </span>"
				+"	    </div>                                                                                                                                                                                "
				+"	    <div class=\"weui-form-preview__item\">                                                                                                                                               "
				+"	      <label class=\"weui-form-preview__label\">签收状态</label>                                                                                                                      "
				+"	      <span class=\""+(item.signStatus==1?"":"weui-icon-waiting weui-icon_msg")+"\" id=\"driverSignConfirmId_"+item.id+"\" onclick=\"driverSignConfirm('"+item.id+"','"+item.recStatus+"')\">"+(item.signStatus==1?"<img alt=\"签收状态\" src=\"${pageContext.request.contextPath }/page/distribution/images/signed.png\"/>":"")+"</span>";
				showTemp+=(item.signStatus==1?"":"<span class=\"sortClass\" onclick=\"settingSort('"+item.id+"')\">"+item.sortNum+"</span>")
				+"	      <span class=\"nowLocation\" onclick=\"setttingNowLocation('"+item.id+"','"+item.custId+"','"+item.openid+"','"+item.address+"','"+item.longitude+"','"+item.latitude+"','"+item.khmc+"')\"><img alt=\"定位当前位置\" id=\"nowLocationImgId_"+item.id+"\" src=\"${pageContext.request.contextPath }/page/distribution/images/"+(item.latitude==null||item.latitude==""?"nowLocation_notset.png":"nowLocation.png")+"\"/></span>"
				+"	    </div>                                                                                                                                                                                "
				+"	  </div>                                                                                                                                                                                  "
				+"	  <div class=\"weui-form-preview__ft\">        "
				+"	    <button type=\"submit\" class=\"weui-form-preview__btn weui-form-preview__btn_primary\" href=\"javascript:\" id=\"gaodeNavigation_"+item.id+"\" singFlag=\""+item.signStatus+"\" latitudeContent=\""+item.latitude+"\" longitudeContent=\""+item.longitude+"\" onclick=\"gaodeNavigation('"+item.id+"','"+recieverData+"','"+item.address+"')\">打开高德导航</button>                                                    "
				+"	  </div>                                                                                                                                                                                  "
				+"	</div>                                                                                                                                                                                    ";
		}
		$("#dataList").empty();
		$("#dataList").append(showTemp);
		
		$("div[id^='shrink_']").each(function(index,obj){
			$(obj).attr("style","display:none");
		});
	}
	
	function shrinkData(orderId){
		if($('#shrink_'+orderId).css("display")=='none') {
			$('#shrink_'+orderId).slideDown();
			$('#shrinkImg_'+orderId).attr("src", "${pageContext.request.contextPath }/page/distribution/images/shrink_up.png");
		}else{
			$('#shrink_'+orderId).slideUp();
			$('#shrinkImg_'+orderId).attr("src", "${pageContext.request.contextPath }/page/distribution/images/shrink_down.png");
		}
	}
	
	Date.prototype.Format = function (fmt) {
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "H+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	
	$('#recDate').val(new Date().Format("yyyy-MM-dd"));
	
	$('#goodsDetail').on('input', function(){
		var goodsDetailVal = $('#goodsDetail').val();
		if(goodsDetailVal.length>1||goodsDetailVal.length==0){
			listData();
		}
	});
	$('#s11').on('change', function(){
		listData();
	});
	$('#recDate').on('input', function(){
		listData();
	});
	
	function driverSignConfirm(dataId,recStatus){
		if(recStatus=='1'){
			$.alert("已标识配送状态，无需再次操作！");
		}else{
			$.confirm({
			  title: '客户签收标识-司机端',
			  text: '客户已签收？点击确认后该数据不会出现在待送清单！',
			  onOK: function () {
				  $.ajax({
						url : '${pageContext.request.contextPath}/distribution/driverSignConfirm.html?recordId='+dataId,
						type : "POST",
						dataType : "json",
						async : false,
						success : function(data) {
							if (data == 1) {
								listData();
							}else {
								$.alert("设置失败，请重新再试！");
							}
						}
					});
			  },
			  onCancel: function () {
			  }
			});
		}
	}
	
	function settingSort(dataId){
		$.prompt({
		  title: '请输入排序序号',
		  text: '输入的只能为数字，相同的序号优先排序后进的',
		  input: '1',
		  empty: false, // 是否允许为空
		  onOK: function (input) {
			  if(isNaN(Number(input))){  //当输入不是数字的时候，Number后返回的值是NaN;然后用isNaN判断。
				 $.alert('输入的不是数字，请重新输入！');
			  	 return ;
			  }
			  $.ajax({
					url : '${pageContext.request.contextPath}/distribution/settingSort.html?id='+dataId+"&sortNum="+input,
					type : "POST",
					dataType : "json",
					async : false,
					success : function(data) {console.log(data);
						if (data == 1) {
							listData();
						}else {
							$.alert("设置失败，请重新再试！");
						}
					}
				});
		  },
		  onCancel: function () {
		    //点击取消
		  }
		});
	}
	
	var dataRowId = "";
	function setttingNowLocation(dataId,custId,custOpenId,address,lng,lat,khmc){
		$.modal({
	   		  title: "精准送货位置设置服务",
	   		  text: '是否确认设置\042'+address+'\042的送货地址为当前位置？',
	   		  buttons: [
	   		    { text: "我已打开位置服务点此设置", onClick: function(){
	   		    	dataRowId = dataId;
	   		    	var url = '${pageContext.request.contextPath}/distribution/go2Location.html?address='+address+'&lat='+$('#latitude').val()+'&lng='+$('#longitude').val()+'&custId='+custId+'&custOpenId='+custOpenId+'&distributionId='+dataId+'&khmc='+khmc;

	   		    	var latitude_ = $("#latitude").val();
	   		    	 if(latitude_!=null&&latitude_!=''){
	   			    	$("#mapPage").attr("src",url);
	   		    	 }
	   		    	 $("#locationPopup").popup();
	   		    	 closeDialogAct();
	   		    }}
	   		  ]
		});
	}
	
	// 7 地理位置接口
	// 7.1 查看地理位置
	function gaodeNavigation(dataId,inName,inAddress){
		var latitude = $('#latitude').val();
		var longitude = $('#longitude').val();
		var inLatitude = $('#gaodeNavigation_'+dataId).attr("latitudeContent");
		var inLongitude = $('#gaodeNavigation_'+dataId).attr("longitudeContent");
		if(inLatitude=="undefined"||inLatitude==undefined||inLatitude==null||inLatitude==''||inLatitude=='null'){
			$.alert({
			  title: '警告！',
			  text: '当前的收货未精准定位，从收货地址中查找导航，只供参考！',
			  onOK: function () {
				  /* $.ajax({
 						url : '${pageContext.request.contextPath}/distribution/getNavigation.html?address='+inAddress,
 						type : "POST",
 						dataType : "json",
 						async : false,
 						success : function(data) {
 							if (data!=null) {
 								var tempLatitude = data.result.location.lat;
 								var templongitude = data.result.location.lng;
 								
 								wx.openLocation({
 								    latitude: tempLatitude,
 								    longitude: templongitude,
 								    name: inName,
 								    address: inAddress,
 								    scale: 14,
 								    infoUrl: 'https://ganzhuyou.cn'
 							  	 });
 							}else {
 								$.alert("获取地址失败，请稍后再试！");
 							}
 						}
 					}); */
 					
 					AMap.plugin('AMap.Geocoder', function() {
					  var geocoder = new AMap.Geocoder({});
					  geocoder.getLocation(inAddress, function(status, result) {
						  if (status === 'complete' && result.info === 'OK') {
						    // 解析成功，result包含经纬度信息
						    var location = result.geocodes[0].location;
						    console.log(location.getLng() + ", " + location.getLat());
						     var url = 'https://uri.amap.com/navigation?from='+longitude+','+latitude+',我的位置&to='+location.getLng()+','+location.getLat()+','+inAddress+'&mode=car&policy=1&src=ganzhuyou&coordinate=gaode&callnative=0'
							 window.open(url);
						  }
						});
 					})
			  }
			});
		}else{
			var url = 'https://uri.amap.com/navigation?from='+longitude+','+latitude+',我的位置&to='+inLongitude+','+inLatitude+','+inAddress+'&mode=car&policy=1&src=ganzhuyou&coordinate=gaode&callnative=0'
			window.open(url);
			/* wx.openLocation({
				    latitude: tempLatitude,
				    longitude: templongitude,
				    name: inName,
				    address: inAddress,
				    scale: 14,
				    infoUrl: 'https://ganzhuyou.cn'
			  	 }); */
		}
	}
	
	listData();
	
	
	
</script>

	<script>
	
	 function uuid() {
	        var s = [];
	        var hexDigits = "0123456789abcdef";
	        for (var i = 0; i < 36; i++) {
	            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	        }
	        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
	        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
	        s[8] = s[13] = s[18] = s[23] = "-";
	        var uuid = s.join("");
	        return uuid;
	  }
	 
	 
	var uploadCount = 0;
	var uploadUUIDToken = uuid();
	var storeOpenIdVal = "";
	var custIdVal = "";
	var uploadList = [];
	var uploadCountDom = document.getElementById("uploadCount");
	weui.uploader('#uploader', {
   		url: '${pageContext.request.contextPath}/distribution/storeImgUpload',
	    auto: true,
	    type: 'file',
	    fileVal: 'fileVal',
	    compress: {
	        width: 1600,
	        height: 1600,
	        quality: .8
	    },
	    onBeforeQueued: function(files) {
	        // `this` 是轮询到的文件, `files` 是所有文件
	        if(["image/jpg", "image/jpeg", "image/png", "image/gif"].indexOf(this.type) < 0) {
	            weui.alert('请上传图片');
	            return false; // 阻止文件添加
	        }
	        if(this.size > 10 * 1024 * 1024) {
	            weui.alert('请上传不超过10M的图片');
	            return false;
	        }
	        if(files.length > 5) { // 防止一下子选择过多文件
	            weui.alert('最多只能上传5张图片，请重新选择');
	            return false;
	        }
	        if(uploadCount + 1 > 5) {
	            weui.alert('最多只能上传5张图片');
	            return false;
	        }
	        ++uploadCount;
	         uploadCountDom.innerHTML = uploadCount;
	        // return true; // 阻止默认行为，不插入预览图的框架
	    },
	    onQueued: function() {
	        console.log("onQueued:",this);
	         uploadList.push(this);
	        // console.log(this.status); // 文件的状态：'ready', 'progress', 'success', 'fail'
	        // console.log(this.base64); // 如果是base64上传，file.base64可以获得文件的base64
	        // this.upload(); // 如果是手动上传，这里可以通过调用upload来实现；也可以用它来实现重传。
	        // this.stop(); // 中断上传
	        // return true; // 阻止默认行为，不显示预览图的图像
	    },
	    onBeforeSend: function(data, headers) {
	    	if((storeOpenIdVal=="undefined"||storeOpenIdVal==undefined||storeOpenIdVal==null||storeOpenIdVal==''||storeOpenIdVal=='null')&&(custIdVal=="undefined"||custIdVal==undefined||custIdVal==null||custIdVal==''||custIdVal=='null')){
	    		$.alert("数据异常，请联系客服处理！");
	    		return false;
	    	}
	        $.extend(data, { uploadToken: uploadUUIDToken,def1:storeOpenIdVal,def2:custIdVal,def3:storeImgId });
	        console.log("onBeforeSend:",this, data, headers);
	        // $.extend(data, { test: 1 }); // 可以扩展此对象来控制上传参数
	        // $.extend(headers, { Origin: 'http://127.0.0.1' }); // 可以扩展此对象来控制上传头部
	        // return false; // 阻止文件上传
	    },
	    onProgress: function(procent) {
	        console.log("onProgress:",this, procent);
	        // return true; // 阻止默认行为，不使用默认的进度显示
	    },
	    onSuccess: function(ret) {
	        console.log("onSuccess:",this, ret);
	       /*  var uploadID = this.id;;
	        var custStoreImg = $('#title_span_'+id).attr('custStoreJson');
	        $("#uploaderFiles li").each(function () {
	            if ($(this).attr("data-id") == uploadID) {
	            	var fileName = ret.lstUploadDetailVo[0].filedeskName;
	            	var fileFloder = ret.lstUploadDetailVo[0].fileFloder;
	                $(this).attr("filedeskName", fileName);  //图片后台对应的唯一编号
	                var url = "${pageContext.request.contextPath}/cloud/download.html?fileName="+fileName+"&fileType="+fileFloder;
	                $(this).attr("url", url);  //图片存放地址
	            }
	        }); */
	        var custStoreImgData = JSON.stringify(ret);
			var custStoreJson;
			if(custStoreImgData!='[]'&&custStoreImgData!=''&&custStoreImgData!=null&&custStoreImgData!=undefined){
				custStoreJson = encodeURI(custStoreImgData);
			}
			var custStoreImg = $('#title_span_'+storeImgId).attr('custStoreJson',custStoreJson);
	        filesDisplay();
	        
	        // return true; // 阻止默认行为，不使用默认的成功态
	    },
	    onError: function(err) {
	        console.log("onError:",this, err);
	        // return true; // 阻止默认行为，不使用默认的失败态
	    }
	});

	document.querySelector('#uploaderFiles').addEventListener('click', function (e) {
	    var target = e.target;
	    while (!target.classList.contains('weui-uploader__file') && target) {
	        target = target.parentNode;
	    }
	    if (!target) return;
	    
	    var filedeskName = target.getAttribute('filedeskName');  //图片唯一编号
	    var id = target.getAttribute('data-id');  //点击图片对应的id
	    var url = target.getAttribute('url');  //图片存放地址
	    //var url = target.getAttribute('style') || '';
	    /* if (url) {
	        url = url.match(/url\((.*?)\)/)[1].replace(/"/g, '');
	    } */
	    var gallery = weui.gallery(url, {
	        className: 'custom-name',
	        onDelete: function onDelete() {
	        	$.confirm({
	  			  title: '图片删除确认',
	  			  text: '确定删除该图片？',
	  			  onOK: function () {
	  				$.ajax({
						url : '${pageContext.request.contextPath}/distribution/removeStoreImg.html?distributionId='+storeImgId+'&storeOpenId='+storeOpenIdVal+"&custId="+custIdVal+"&filedeskName="+filedeskName,
						type : "POST",
						async : false,
						success : function(data) {
							if (data == '0') {
								$.alert("删除失败，请重新再试！");
							}else {
								var custStoreImgData = data;
								var custStoreJson;
								if(custStoreImgData!='[]'&&custStoreImgData!=''&&custStoreImgData!=null&&custStoreImgData!=undefined){
									custStoreJson = encodeURI(custStoreImgData);
								}
								if(custStoreJson==undefined){
									$('#title_span_'+storeImgId).attr('custStoreJson','');
								}else{
									$('#title_span_'+storeImgId).attr('custStoreJson',custStoreJson);
								}
						        filesDisplay();
							}
						}
					});
	                /* target.remove(); */
	                gallery.hide();
	  					
	  			  },
	  			  onCancel: function () {
	  			  }
	  			});
	        }
	    });
	});

	var storeImgRunTimes = 0;
	function go2StoreImg(id,openId,custId,custStoreImgVal){
		$('#storeImgPopup').show();
		if(storeImgRunTimes>1){
			return ;
		}
		
		storeImgRunTimes++;
		 $('body').loading({
			loadingWidth:240,
			title:'请稍等!',
			name:'loadingStoreImg',
			discription:'正在加载数据中...',
			direction:'column',
			type:'origin',
			// originBg:'#71EA71',
			originDivWidth:40,
			originDivHeight:40,
			originWidth:6,
			originHeight:6,
			smallLoading:false,
			loadingMaskBg:'rgba(0,0,0,0.2)'
	    });
		uploadUUIDToken = uuid();
		storeImgId = id;
		
		if(openId=="undefined"||openId==undefined||openId==null||openId==''||openId=='null'){
			openId = "";
		}if(custId=="undefined"||custId==undefined||custId==null||custId==''||custId=='null'){
			custId = "";
		}
		storeOpenIdVal = openId;
		custIdVal = custId;
		
		filesDisplay();
		
		$('#storeImgPopup').show();
		$("#storeImgPopup").popup();
		removeLoading('loadingStoreImg');
		storeImgRunTimes = 0;
	}
	
	function filesDisplay(){
		var custStoreImg = $('#title_span_'+storeImgId).attr('custStoreJson');
		$("#uploaderFiles").html("");
		if(custStoreImg!=''&&custStoreImg!='[]'&&custStoreImg!=null&&custStoreImg!=undefined&&custStoreImg!="undefined"){
			var custStoreData = JSON.parse(decodeURI(custStoreImg));
			var imgContent = "";
			for(var i = 0;i<custStoreData.length;i++){
				var url = "${pageContext.request.contextPath}/cloud/download.html?fileName="+custStoreData[i].filedeskName+"&fileType="+custStoreData[i].fileFloder;
				imgContent += "<li class=\"weui-uploader__file\" data-id=\""+uuid()+"\" role=\"img\" filedeskname=\""+custStoreData[i].filedeskName+"\" url=\""+url+"\" style=\"background-image:url("+url+");\"></li>";
			}
			$("#uploaderFiles").html(imgContent);
			uploadCount = custStoreData.length;
			$("#uploadCount").html(custStoreData.length);
		}else{
			$("#uploaderFiles").html("");
			uploadCount = 0;
			$("#uploadCount").html(uploadCount);
		}
	}
	
	
	function closeStoreImgPopup(){
		if(uploadCount>0){
			$('#title_span_'+storeImgId).attr('style','');
		}else{
			$('#title_span_'+storeImgId).attr('style','color: red;');
		}
		uploadCount = 0;
		storeOpenIdVal = "";
		custIdVal = "";
	}


	</script>
	
<script type="text/javascript">
	$("#floatDiv").hide();
	function closeDialogAct(){
		$("#floatDiv").show();
		var flag = -1; //标记是拖曳还是点击
		var oDiv = document.getElementById('floatDiv');
		var disX,moveX,L,T,starX,starY,starXEnd,starYEnd;
		oDiv.addEventListener('touchstart',function(e){
			flag = 0;
			e.preventDefault();//阻止触摸时页面的滚动，缩放
			disX = e.touches[0].clientX - this.offsetLeft;
			disY = e.touches[0].clientY - this.offsetTop;
			//手指按下时的坐标
			starX = e.touches[0].clientX;
			starY = e.touches[0].clientY;
			console.log('floatDiv closeDialogAct touchstart:'+disX);
		});
		oDiv.addEventListener('touchmove',function(e){
			flag = 1;
			L = e.touches[0].clientX - disX ;
			T = e.touches[0].clientY - disY ;
			//移动时 当前位置与起始位置之间的差值
			starXEnd = e.touches[0].clientX - starX;
			starYEnd = e.touches[0].clientY - starY;
			//console.log(L);
			if(L<0){//限制拖拽的X范围，不能拖出屏幕
				L = 0;
			}else if(L > document.documentElement.clientWidth - this.offsetWidth){
				L=document.documentElement.clientWidth - this.offsetWidth;
			}
			if(T<0){//限制拖拽的Y范围，不能拖出屏幕
				T=0;
			}else if(T>document.documentElement.clientHeight - this.offsetHeight){
				T = document.documentElement.clientHeight - this.offsetHeight;
			}
			moveX = L + 'px';
			moveY = T + 'px';
			console.log('floatDiv closeDialogAct touchmove:'+moveX);
			this.style.left = moveX;
			this.style.top = moveY;
		});
		window.addEventListener('touchend',function(e){
			//alert(parseInt(moveX))
			console.log('floatDiv closeDialogAct touchend:'+moveX);
			//判断滑动方向
			if(flag === 0) {//点击
				flag = -1;
				$.closePopup();
				$("#floatDiv").hide();
			}
		});
		
		var rightLocation = document.documentElement.clientWidth-64;
		var leftLocation = (document.documentElement.clientHeight/2-64);
		$("#floatDiv").css("left",rightLocation);
		$("#floatDiv").css("top",leftLocation);
	}
</script>
<script type="text/javascript">

function flushFunc(){
	var _element = document.getElementById('refreshContainer'),
	  _refreshText = document.querySelector('.refreshText'),
	  _startPos = 0,
	  _transitionHeight = 0;
	var _reFreshflag = -1; //标记是拖曳还是点击
    var phoneHeigh = document.documentElement.clientHeight;
	_element.addEventListener('touchstart', function(e) {
		var _pageTop = document.body.scrollTop;
		var __pageTop = document.documentElement.scrollTop;
		_startPos = e.touches[0].pageY;
		_reFreshflag = 0;
		if (_startPos>phoneHeigh) {
			console.log("_startPos:"+_startPos+"||phoneHeigh:"+(phoneHeigh));
			_element.style='';
			_refreshText.innerHTML = '';
			return;
		}
		console.log('初始位置：', e.touches[0].pageY+"document.documentElement.scrollTop:"+document.body.scrollTop);
		_element.style.position = 'relative';
		_element.style.transition = 'transform 0s';
	}, { passive: false });
	
	_element.addEventListener('touchmove', function(e) {
		_transitionHeight = (e.touches[0].pageY - _startPos);
		var _pageTop = document.body.scrollTop;
		var __pageTop = document.documentElement.scrollTop;
		
		if (_startPos>phoneHeigh||(e.touches[0].pageY<_startPos)) {
			console.log("_startPos touchmove:"+_startPos+"||phoneHeigh:"+(phoneHeigh));
			_element.style='';
			_refreshText.innerHTML = '';
			return;
		}
		
		if ((e.touches[0].pageY>_startPos)&&__pageTop===0&&__pageTop===0) {
			 e.preventDefault();
		}
		
		console.log('touchmove位置：',((e.touches[0].pageY>_startPos)&&__pageTop===0&&__pageTop===0)+"||e.touches[0].pageY"+ e.touches[0].pageY+"||_startPos:"+_startPos+"document.documentElement.scrollTop:"+_pageTop+"||document.documentElement.scrollTop:"+__pageTop);
		if (_pageTop===0&&__pageTop===0&&_transitionHeight > 0 && _transitionHeight < 100) {
			console.log("_transitionHeight:"+_transitionHeight);
			_refreshText.innerHTML = '↓下拉刷新';
			_element.style.transform = 'translateY('+_transitionHeight+'px)';
			_reFreshflag = 0;
			if ((e.touches[0].pageY - _startPos) > 55) {
				_reFreshflag = 1;
			    _refreshText.innerHTML = '↑释放更新';
			}
		}
	}, { passive: false });

	_element.addEventListener('touchend',function(e){
			_element.style.transition = 'transform 0.5s ease 1s';
		if (_startPos>phoneHeigh) {
			console.log("_startPos touchmove:"+_startPos+"||phoneHeigh:"+(phoneHeigh));
			_element.style='';
			_refreshText.innerHTML = '';
			return;
		}
		
		//判断滑动方向
	 	if(document.body.scrollTop===0&&_reFreshflag === 1) {//点击
			_reFreshflag = -1;
			_refreshText.innerHTML = '正在努力更新中';
			_refreshText.innerHTML = '<div class=\"weui-loadmore\" style=\"font-size: 12px;font: inherit;margin-top: -2px;\"><i class=\"weui-loading\"></i><span class=\"weui-loadmore__tips\">正在努力更新中</span></div>';
			listData();
			
		    var pullDownFlushInterval = setInterval(function(){
				if(pullDownFlushFlag===1){
					_element.style.transform = 'translateY(0px)';
					_element.style.transition = 'transform 0s ease 0s';
					clearInterval(pullDownFlushInterval);
				}
			},500);
		}
	}, { passive: false });
}
flushFunc();
/* document.addEventListener('touchmove', function(e) {
	
}, { passive: false });  */
  
  //禁止页面拖动
	/* document.addEventListener('touchmove', function(e) {
       console.log('禁止下拉出现网页来源信息'+"document.querySelector('#refreshContainer'):"+document.querySelector('#refreshContainer')+"e.target:"+e.target)
	   if (document.querySelector('#dataList').contains(e.target)) {
          e.preventDefault();
       }
    }, { passive: false }) */
</script>

</body>
</html>