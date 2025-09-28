<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>联系人明细</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
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
</head>
<body>
 <div class="page">
  <div class="weui-form">
    <div class="weui-cell">
         <div class="weui-cell__hd"><label class="weui-label">收货人</label></div>
         <div class="weui-cell__bd">
             <input class="weui-input" type="text" placeholder="请输入收货人" value="${data.addr_user }" id="addr_user"/>
         </div>
     </div>
    <div class="weui-cell">
         	<div class="weui-cell__hd"><label class="weui-label">手机号码</label></div>
         	<div class="weui-cell__bd">
             <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号" value="${data.addr_tel }" id="idPhone" onkeyup="phoneValidate();"/>
         	</div>
         	<div class="weui-cell__ft weui-cell_warn">
       		<i class="weui-icon-warn" id="phoneValidateId"></i>
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
        <div class="weui-cell__hd"><label class="weui-label">详细地址</label></div>
        <div class="weui-cell__bd">
        	<textarea class="weui-textarea" placeholder="街道、楼牌号等" rows="2" cols="27" id="addr_name">${data.addr_name }</textarea>
        </div>
     </div>
  </div>
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
    <div class="weui-form__opr-area" id="confimTextId">
      <a class="weui-btn weui-btn_primary" href="javascript:" id="saveDataBut">保存</a>
    </div>
    <c:if test="${data.addr_id!=null&&data.addr_id!=0}">
	    <div class="weui-form__opr-area" id="confimTextId">
	      <a class="weui-btn weui-btn_warn" href="javascript:" id="delDataBut">删除</a>
	    </div>
	</c:if>

<!-- body 最后 -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-weui.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath }/components/city-pickers/city-picker-four.js" charset="utf-8"></script>

<script type="text/javascript" src="js/wechat_validate.js"></script>


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
	
	function del(){
		var  b = confirm('确定删除？');
		if(!b){
		return ;
		}
		$.ajax({
			url:'addrDel.html',
			type:'post',
			data:'addr_id=${data.addr_id}',
			success:function(rs){
				if(rs==1){
					window.location.href='${pageContext.request.contextPath}/page/go2SubResult.html?operResult=1&returnURL=${pageContext.request.contextPath}/page/addressList.html';
				}else{
					alert("失败！");
				}
			}
		})
	}
	
    $(function(){
    	
    	phoneValidate();
    	
    	$('#delDataBut').on('click', function(){
    		del();
        });
    	
        $('#saveDataBut').on('click', function(){
        	var phoneVal = $("#idPhone").val();
        	var addr_user = $("#addr_user").val();
        	var addr_name = $("#addr_name").val();
        	var cityPickerVal = $("#Addr").val();
    		var provinceVal = $("#province .active").attr("data-code");
    		var cityVal = $("#city .active").attr("data-code");
    		var areaVal = $("#area .active").attr("data-code");
    		var townVal = $("#town .active").attr("data-code");
    		
        	if($.wxregex.mobileTest(phoneVal)==false){
	        	$.toptip("请输入正确的手机号码!");
	        	return ;
        	}
        	if(addr_user==null||addr_user==""){
        		$.toptip("请输入收货人!");
    			return ;
    		}if(cityPickerVal==null||cityPickerVal==""){
        		$.toptip("请选择正确的省市区镇!");
    			return ;
    		}if(addr_name==null||addr_name==""){
        		$.toptip("请选择正确的详细地址，街道、楼牌号等!");
    			return ;
    		}if(provinceVal=="undefined"||provinceVal==undefined){
    			provinceVal = "${data.province }";
    			cityVal = "${data.city }";
    			areaVal = "${data.area }";
    			townVal = "${data.town }";
    		}
        	
        	$.ajax({
    			url:'${pageContext.request.contextPath}/page/saveAddr.html',
    			type:'post',
    			data:'mphone='+phoneVal+"&addr_user="+addr_user+"&addr_name="+addr_name+"&addr_id=${data.addr_id}&province="+provinceVal+"&city="+cityVal+"&area="+areaVal+"&town="+townVal+"&cityPickerCN="+cityPickerVal,
    			success:function(rs){
    				if(rs=="1"){
    					var returnURL = "${returnURL}";
    					if(returnURL!=null&&returnURL!=""){
    						returnURL = encodeURIComponent("${pageContext.request.contextPath}/page/prepareOrder.html?addr_id=${addr_id}&cps_id=${cps_id}");
    					}else{
    						returnURL = "${pageContext.request.contextPath}/page/addressList.html";
    					}
    					window.location.href='${pageContext.request.contextPath}/page/go2SubResult.html?operResult=1&returnURL='+returnURL;
    				}else{
    					alert("操作失败!");
    				}
    			}
    		})
        });
    });
</script>
</body>
</html>