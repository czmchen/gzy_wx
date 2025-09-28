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
<script type="text/javascript">
  window._AMapSecurityConfig = {
	  /* serviceHost:'http://127.0.0.1:8000/_AMapService' */
	  securityJsCode:'c4b3ed22a89083edbd3b6d994e4814e8',
  }
</script>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=7d87e02d3cab51a672c63df409cd8bae"></script>

</head>
<body>
<div id="locationPopup" class="weui-popup__container">
  <div class="weui-popup__overlay"></div>
  <div class="weui-popup__modal">
	<iframe id="mapPage" width="100%" height="100%" frameborder="0" allow="geolocation" src="">
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
      <h2 class="weui-form__title" id="mobileTitleId">客户注册</h2>
      <div class="weui-form__desc" id="mobileContentId"></div>
    </div>
     <div class="weui-cell">
         <div class="weui-cell__hd"><label class="weui-label">客户编号</label></div>
         <div class="weui-cell__bd">
             <input class="weui-input" type="text" placeholder="注册期间不用输入" value="${data.custid }" readonly="readonly"/>
         </div>
     </div>
    <div class="weui-cell">
         	<div class="weui-cell__hd"><label class="weui-label">手机号码</label></div>
         	<div class="weui-cell__bd">
             <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号" value="${data.mphone }" id="idPhone" onkeyup="phoneValidate();"/>
         	</div>
         	<div class="weui-cell__ft weui-cell_warn">
       		<i class="weui-icon-warn" id="phoneValidateId"></i>
  		 </div>
    </div>
     <div class="weui-cell">
         <div class="weui-cell__hd"><label class="weui-label">客户名称</label></div>
         <div class="weui-cell__bd">
             <input class="weui-input" type="text" placeholder="请输入客户名称" value="${data.KHMC }" id="KHMC"/>
         </div>
     </div>
    <div class="weui-cell">
         <div class="weui-cell__hd"><label class="weui-label">联系人</label></div>
         <div class="weui-cell__bd">
             <input class="weui-input" type="text" placeholder="请输入联系人" value="${data.KHLXR }" id="KHLXR"/>
         </div>
     </div>
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">所在地区</label></div>
        <div class="weui-cell__bd">
	        <label for="Addr" id="areaLabel" class="address">
	        	<textarea class="weui-textarea" placeholder="请选择地区" rows="3" cols="27" id="Addr" name="Addr" readonly="readonly">${data.cityPickerCN }</textarea>
	        </label>
        </div>
     </div>
      <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">收货地址</label></div>
        <div class="weui-cell__bd">
        	<textarea class="weui-textarea" placeholder="街道、楼牌号等" rows="2" cols="27" id="crm_addr_name">${data.crm_addr_name }</textarea>
        </div>
     </div>
     <div class="weui-cell">
     <div class="weui-cell__hd"><label class="weui-label"></label></div>
        <div class="weui-cell__bd">
        	<input type="hidden" id="latitude" value="${data.latitude }"/>
        	<input type="hidden" id="longitude" value="${data.longitude }" />
        	<input type="hidden" id="gaodeId" value="${data.gaodeId }" />
        	<input type="hidden" id="merchantsId" value="${data.merchantsId }" />
        	
        	<input type="hidden" id="temp_latitude" value=""/>
        	<input type="hidden" id="temp_longitude" value="" />
        	<a href="javascript:void(0)" onclick="getNowLocation();" id="nowLocationId">${(data.latitude!=null&&data.latitude!='')?"已设置精准收货地址，点此重新设置！":"精准收货地址，请点此！"  }</a>
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
                }else{
                    onError(result);
                }
            });
        }, 5000);  // 每秒钟更新一次定位点位置
    });
	
	 //解析定位结果
    function onComplete(data) {
   	  // $('#latitude').val(data.position.lat);
   	  // $('#longitude').val(data.position.lng);
	   $('#temp_latitude').val(data.position.lat);
	   $('#temp_longitude').val(data.position.lng);
    }
	 
    //解析定位错误信息
    var msgNoteTime = 0;
    function onError(data) {
    	if(msgNoteTime<msgNoteMaxTime){
    	   msgNoteTime++;
	 	   alert("定位超时，请打开定位服务！");
    	}
    }
</script>
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
        	if(KHMCVal==null||KHMCVal==""){
        		$.toptip("请输入客户名称!");
    			return ;
    		}if(KHLXRVal==null||KHLXRVal==""){
        		$.toptip("请输入联系人!");
    			return ;
    		}if(cityPickerVal==null||cityPickerVal==""){
        		$.toptip("请选择正确的省市区镇!");
    			return ;
    		}if(crmAddrNameVal==null||crmAddrNameVal==""){
        		$.toptip("请选择正确的收货地址，街道、楼牌号等!");
    			return ;
    		}if(provinceVal=="undefined"||provinceVal==undefined){
    			provinceVal = "${data.crm_province }";
    			cityVal = "${data.crm_city }";
    			areaVal = "${data.crm_area }";
    			townVal = "${data.crm_town }";
    		}
    		
        	if(!$("#haveReaded").is(":checked")){
        		$.toptip("请阅读相关注意事项并在\042已知会并输入无误\042前打上√!");
        		return ;
        	}
        	
        	$.ajax({
    			url:'${pageContext.request.contextPath}/page/binding.html',
    			type:'post',
    			data:'mphone='+phoneVal+"&KHMC="+KHMCVal+"&KHLXR="+KHLXRVal+"&crm_province="+provinceVal+"&crm_city="+cityVal+"&crm_area="+areaVal+"&crm_town="+townVal+"&crmAddrName="+crmAddrNameVal+"&cityPickerCN="+cityPickerVal+"&latitude="+$('#latitude').val()+"&longitude="+$('#longitude').val()+"&gaodeId="+$('#gaodeId').val()+"&merchantsId="+$('#merchantsId').val(),
    			success:function(rs){
    				if(rs=="success"){
   						window.location.href='${pageContext.request.contextPath}/page/go2SubResult.html?operResult=1&returnURL=${pageContext.request.contextPath}/page/center.html';
    				}else{
    					alert("绑定失败!");
    				}
    			}
    		})
        });
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
   		    	
		    	var address = $("#KHMC").val();
   		  		var url = '${pageContext.request.contextPath}/page/go2Location.html?address='+address;

			     $("#mapPage").attr("src",url);
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
</script>
</body>
</html>