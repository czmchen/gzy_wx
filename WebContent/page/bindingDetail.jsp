<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE-9">
<meta name="apple-mobile-web-app-capable" content="no">
<meta name="mobile-web-app-capable" content="no">
<meta name="renderer" content="webkit">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<!-- Bootstrap -->
<link type="text/css" href="css/weui.css" rel="stylesheet">
<link type="text/css" href="css/weuix.css" rel="stylesheet">
<link rel="stylesheet" href="css/weui.min.css">
<link rel="stylesheet" href="css/jquery-weui.min.css">

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/wechat_validate.js"></script>
<script type="text/javascript" src="js/zepto.min.js"></script>
<script type="text/javascript" src="js/zepto.weui.min.js"></script>
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
  	 <div class="weui_msg">
        <div class="page-bd" id="warnId">
            <i class="weui-icon-success weui-icon_msg"></i>
        </div>
    </div>
    <div class="weui-form__text-area">
      <h2 class="weui-form__title" id="mobileTitleId">手机号码绑定成功</h2>
      <div class="weui-form__desc" id="mobileContentId">绑定成功，若需重新绑定，请联系客服解绑后重新绑定。</div>
    </div>
    <div class="weui-form__control-area">
      <div class="weui-cells__group weui-cells__group_form">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell weui-cell_active">
                <div class="weui-cell__hd"><label class="weui-label">手机号码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号" value="${data.mphone}" id="idPhone" onkeyup="phoneValidate();" readonly="readonly"/>
                </div>
            </div>
        </div>
      </div>
    </div>
  </div>
 </div>
<!-- body 最后 -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-weui.min.js"></script>

<!-- 如果使用了某些拓展插件还需要额外的JS -->
<script src="js/swiper.min.js"></script>
<script src="js/city-picker.min.js"></script>
</body>
</html>