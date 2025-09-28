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
<link rel="stylesheet" href="${pageContext.request.contextPath }/components/city-pickers/city-pcicker-four-v1.2.css">

<style type="text/css">
		.weui-icon_msg {
		    font-size: 115px;
		    margin-top: 10px;
		}
		#warnId{
			text-align:center
		}
		#mobileTitleId{
			text-align:center;
			font-size: 23px;
		}
		#mobileContentId{
			font-size: 18px;
		}
		.weui-cells:before {
	    	position: fixed;
	    }
	    #readTextId{
	        padding: 2px;
	    }
	    #confimTextId{
	    	margin-top: 10px;
	    }
</style>
<script type="text/javascript" src="https://mapapi.qq.com/web/mapComponents/geoLocation/v/geolocation.min.js"></script>

</head>
<body>
<div id="locationPopup" class="weui-popup__container">
  <div class="weui-popup__overlay"></div>
  <div class="weui-popup__modal">
	<iframe id="mapPage" width="100%" height="100%" frameborder="0" allow="geolocation"
	    src="https://apis.map.qq.com/tools/locpicker?search=1&type=1&zoom=18&key=ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3&referer=ganzhuyou">
	</iframe>
  </div>
</div>

 <div class="page">
  <div class="weui-form">
  	 <div class="weui_msg">
        <div class="page-bd" id="warnId">
            <i class="weui-icon-info weui-icon_msg"></i>
        </div>
    </div>
    <div class="weui-form__text-area">
      <h2 class="weui-form__title" id="mobileTitleId">新客福利领取</h2>
      <div class="weui-form__desc" id="mobileContentId"></div>
    </div>
    <div class="weui-cell">
         	<div class="weui-cell__hd"><label class="weui-label">收货人</label></div>
         	<div class="weui-cell__bd">
             <input class="weui-input" type="text"  placeholder="请输入收货人"  value="" id="KHLXR" onkeyup="KHLXRValidate();"/>
         	</div>
         	<div class="weui-cell__ft weui-cell_warn">
       		<i class="weui-icon-warn" id="KHLXRValidateId"></i>
  		 </div>
    </div>
    <div class="weui-cell">
         	<div class="weui-cell__hd"><label class="weui-label">手机号码</label></div>
         	<div class="weui-cell__bd">
             <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号" value="" id="idPhone" onkeyup="phoneValidate();"/>
         	</div>
         	<div class="weui-cell__ft weui-cell_warn">
       		<i class="weui-icon-warn" id="phoneValidateId"></i>
  		 </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">所在地区</label></div>
        <div class="weui-cell__bd">
	        <label for="Addr" id="areaLabel" class="address">
	        	<textarea class="weui-textarea" placeholder="请选择地区" rows="3" cols="27" id="Addr" name="Addr" readonly="readonly"></textarea>
	        </label>
        </div>
     </div>
      <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">详细地址</label></div>
        <div class="weui-cell__bd">
        	<textarea class="weui-textarea" placeholder="街道、楼牌号等" rows="2" cols="27" id="crm_addr_name"></textarea>
        </div>
     </div>
     <div class="weui-cell">
     <div class="weui-cell__hd"><label class="weui-label"></label></div>
        <div class="weui-cell__bd">
        	<input type="hidden" id="latitude" value=""/>
        	<input type="hidden" id="longitude" value="" />
        	
        	<input type="hidden" id="temp_latitude" value=""/>
        	<input type="hidden" id="temp_longitude" value="" />
        	<a href="javascript:void(0)" onclick="getNowLocation();" id="nowLocationId">精准收货地址，请点此！</a>
        </div>
     </div>
  </div>
 </div>
 <div class="weui-cells weui-cells_checkbox">
	    <label class="weui-cell weui-check__label" for="haveReaded" id="readTextId">
	        <div class="weui-cell__hd">
	            <input class="weui-check" name="checkbox1" id="haveReaded"  type="checkbox">
	            <i class="weui-icon-checked"></i>已检查数据，输入无误
	        </div>
	    </label>
	</div>
    <div class="weui-form__opr-area" id="confimTextId">
      <a class="weui-btn weui-btn_primary" href="javascript:" id="showTooltips">提交</a>
    </div>
	<div id="addressSelectWrapper" style="z-index:9999;">
	    <div id="addressSelect">
	        <div class="tip">
	            <h3>所在地区</h3>
	            <button type="button" id="cancel" class="cancel"></button>
	        </div>
	        <div id="address">
	            <ul class="selected-address">
	                <li class="lastarea" id="lastprovince">请选择</li>
	                <li class="lastarea" id="lastcity">请选择</li>
	                <li class="lastarea" id="lastarea">请选择</li>
	                <li class="lastarea" id="lasttown">请选择</li>
	            </ul>
	            <div class="address-content">
	                <ul class="province" id="province"></ul>
	                <ul class="city" id="city"></ul>
	                <ul class="area" id="area"></ul>
	                <ul class="town" id="town"></ul>
	            </div>
	        </div>
	    </div>
	</div>
	
<!-- body 最后 -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-weui.min.js"></script>

<!-- 如果使用了某些拓展插件还需要额外的JS -->
<script type="text/javascript" src="${pageContext.request.contextPath }/components/city-pickers/city-picker-four.js" charset="utf-8"></script>

<script type="text/javascript" src="js/wechat_validate.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>

<script>
    window.addEventListener('message', function(event) {
        // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
        var loc = event.data;
        if (loc && loc.module == 'locationPicker') {//防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
        	 var latitude = loc.latlng.lat;
        	 var longitude = loc.latlng.lng;
	         $.confirm({
			  title: '位置选取服务',
			  text: '是否确认选取当前位置为精准收货地址',
			  onOK: function () {
				  $('#latitude').val(latitude);
   		          $('#longitude').val(longitude);
   		          $('#nowLocationId').html("已设置精准收货地址，点此重新设置！");
   		       	  $.closePopup();
			  },
			  onCancel: function () {
			  }
			});
        }
    }, false);
    
    var geolocation = new qq.maps.Geolocation("ZNFBZ-5CS66-NIFSJ-MZINS-FWNN7-P7BT3", "ganzhuyou");
    
 	var options = {timeout: 5000};
    
	geolocation.getLocation(showQQMapsPosition, showErr, options);//初始获取当前位置
	
    function showQQMapsPosition(position) {
   	 	geolocation.watchPosition(showWatchPosition);//一直监控定位开启
    };
    
    function showWatchPosition(position) {
        $('#temp_latitude').val(position.lat);
		$('#temp_longitude').val(position.lng);
    }
    
	function showErr() {
		geolocation.clearWatch();
		$.alert("定位失败！请打开定位再重试！");
	};
</script>
	
	
<script type="text/javascript">
	$(function () {
        // 地址选择器遮罩层打开与关闭
        $("#areaLabel").click(function (e) {
            $("#addressSelectWrapper").show();
            e.stopPropagation();
        });
        $(document).click(function () {
            $("#addressSelectWrapper").hide();
        });
        $("#cancel").click(function () {
            $("#addressSelectWrapper").hide();
        });
        $("#addressSelect").click(function (e) {
            e.stopPropagation();
        });

        initAddress();
    });

    //初始化地址选择
    function initAddress() {
        $("#Addr").cityLinkage({
            containerId: "addressSelectWrapper",
            getSelectedCode: function () {
                return $("#Addr").data("code");
            },
            callback: function (data) {
                $("#Addr").val(data.addr).data("code", data.code);
            }
        });
    }
    
	function KHLXRValidate(){
		var KHLXRVal = $("#KHLXR").val();
		if(KHLXRVal==null||KHLXRVal==""){
			$("#KHLXRValidateId").removeClass("weui-icon-success");
			$("#KHLXRValidateId").addClass("weui-icon-warn");
		}else{
			$("#KHLXRValidateId").removeClass("weui-icon-warn");
			$("#KHLXRValidateId").addClass("weui-icon-success");
		}
	}
    
    
	/**
	 * 手机号码格式校验
	 * @returns
	 */
	function phoneValidate(){
		var phoneVal = $("#idPhone").val();
		if(phoneVal==null||phoneVal==""){
			$("#phoneValidateId").removeClass("weui-icon-success");
			$("#phoneValidateId").addClass("weui-icon-warn");
			return ;
		}
		
		var tmp = $.wxregex.mobileTest(phoneVal);
		if(!tmp){
			$("#phoneValidateId").removeClass("weui-icon-success");
			$("#phoneValidateId").addClass("weui-icon-warn");
		}else{
			$("#phoneValidateId").removeClass("weui-icon-warn");
			$("#phoneValidateId").addClass("weui-icon-success");
		}
	}
	
    $(function(){
    	KHLXRValidate();    	
    	phoneValidate();
    	
        $('#showTooltips').on('click', function(){
        	var phoneVal = $("#idPhone").val();
        	var KHMCVal = $("#KHMC").val();
    		var KHLXRVal = $("#KHLXR").val();
    		var cityPickerVal = $("#Addr").val();
    		var crmAddrNameVal = $("#crm_addr_name").val();
    		var provinceVal = $("#province .active").attr("data-code");
    		var cityVal = $("#city .active").attr("data-code");
    		var areaVal = $("#area .active").attr("data-code");
    		var townVal = $("#town .active").attr("data-code");
    		
        	if($.wxregex.mobileTest(phoneVal)==false){
	        	$.toptip("请输入正确的手机号码!");
	        	return ;
        	}
        	if(KHLXRVal==null||KHLXRVal==""){
        		$.toptip("请输入收货人!");
    			return ;
    		}if(cityPickerVal==null||cityPickerVal==""){
        		$.toptip("请选择正确的省市区镇!");
    			return ;
    		}if(crmAddrNameVal==null||crmAddrNameVal==""){
        		$.toptip("请选择正确的收货地址，街道、楼牌号等!");
    			return ;
    		}

        	if(!$("#haveReaded").is(":checked")){
        		$.toptip("信息确认无误，请勾选“已检查数据，输入无效”");
        		return ;
        	}
        	
			$('#_mphone').val(phoneVal);
			$('#_KHLXR').val(KHLXRVal);
			$('#_crm_province').val(provinceVal);
			$('#_crm_city').val(cityVal);
			$('#_crm_area').val(areaVal);
			$('#_crm_town').val(townVal);
			$('#_crmAddrName').val(crmAddrNameVal);
			$('#_cityPickerCN').val(cityPickerVal);
			$('#_latitude').val($('#latitude').val());
			$('#_longitude').val($('#longitude').val());
			
			$('#actionForm').submit();
        });
    });
    
    
    wx.config({
	      debug: false,
	      appId: "${objWxJSApiInfo.appId}",
	      timestamp: "${objWxJSApiInfo.timestamp}",
	      nonceStr: "${objWxJSApiInfo.nonceStr}",
	      signature: "${objWxJSApiInfo.signature}",
	      jsApiList: ['checkJsApi', 'getLocation', 'openLocation']
	});
    
    
    function getNowLocation(){
    	$.modal({
   		  title: "精准收货位置设置服务",
   		  text: "设置精准收货地址，将会获取当前的位置，司机将会配送到当前定位，请在常用的收货位置打开手机的定位服务并设置。",
   		  buttons: [
   		    { text: "我已打开位置服务点此设置", onClick: function(){
   		    	/* var latitude = $("#temp_latitude").val();
   		    	if(latitude!=null&&latitude!=''){
   			    	var longitude = $("#temp_longitude").val();
   			    	var iframeSrc = $("#mapPage").attr("src").substr(0,121);
   			    	$("#mapPage").attr("src",(iframeSrc+"&coord="+latitude+","+longitude));
   		    	} */
   		    	$("#locationPopup").popup();
   		    	
   		    	
   		    	/* wx.getLocation({
	   		        success: function (res) {
	   		          if(res.errMsg.indexOf('ok')!=-1){
		   		          $('#latitude').val(res.latitude);
		   		          $('#longitude').val(res.longitude);
		   		          $('#nowLocationId').html("已设置精准收货地址，点此重新设置！");
		   		       	  $.alert("设置成功！请点击提交！");
	   		          }else{
	   		        	$.alert("设置失败，请与客服联系！");
	   		          }
	   		        },
	   		        cancel: function (res) {
	   		        	$.alert("拒绝授权获取地理位置！设置失败!");
	   		        }
   		      	}); */
   		    }}
   		  ]
   		});
    }
    
    
   wx.getLocation({
       success: function (res) {
         if(res.errMsg.indexOf('ok')!=-1){
        	var inputLatitude = $('#latitude').val();
       		if(inputLatitude == null || inputLatitude == "null"  ||  inputLatitude=="" || inputLatitude.length>0){
	          	$('#temp_latitude').val(res.latitude);
				$('#temp_longitude').val(res.longitude);
       		}
         }
       },
       cancel: function (res) {
       	$.alert("拒绝授权获取地理位置！设置失败!");
       }
    }); 
    
</script>
<form action="${pageContext.request.contextPath}/page/newCustSave.html" id="actionForm">
	<input type="hidden" id="_mphone" name="_mphone"/>
	<input type="hidden" id="_KHLXR" name="_KHLXR"/>
	<input type="hidden" id="_crm_province" name="_crm_province"/>
	<input type="hidden" id="_crm_city" name="_crm_city"/>
	<input type="hidden" id="_crm_area" name="_crm_area"/>
	<input type="hidden" id="_crm_town" name="_crm_town"/>
	<input type="hidden" id="_crmAddrName" name="_crmAddrName"/>
	<input type="hidden" id="_cityPickerCN" name="_cityPickerCN"/>
	<input type="hidden" id="_latitude" name="_latitude"/>
	<input type="hidden" id="_longitude" name="_longitude"/>
</form>
</body>
</html>