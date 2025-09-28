<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="fn"
	uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc_v2.1.css">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/woxiangyao.js"></script>
<style type="text/css">
	.drdd-info2 {
		border-bottom: 0px;
	}
</style>
</head>

<body>
    <div style="width:100%; margin-top:5px;">
    	<div class="my-info">
        	<div class="my-k1">
        		<ul class="my-p1">
                	<li class="my-spl f-l">下单方式：${data.realname}</li>
                    <div style="clear:both;"></div>
                </ul>
            	<ul class="my-p1">
                	<li class="my-spl f-l">订单号：${data.order_id}</li>
                	<li class="my-spr f-r">${data.add_time}</li>
                	
                    <div style="clear:both;"></div>
                </ul>
    
   
                <c:forEach items="${data.orderDetail}" var="ordList">
                <dl class="my-dl1">
                	<dt><a href="#"><img src="${ordList.goods_img}" style="width:70px"></a></dt>
                    <dd>
                    	<h3><a href="#">${ordList.goods_name}</a></h3>
                        <p class="my-dp1">价格：<span>￥${ordList.goods_price}</span></p>
                        <div class="my-jdt">
                        	<p class="jdt-p1 f-l">数量：</p>
                           
                            <p class="jdt-shuzi f-l">${ordList.goods_num}</p>
                    		<div style="clear:both;"></div>
                        </div>
                    </dd>
                    <div style="clear:both;"></div>
                </dl>
                </c:forEach>
    <div class="drdd-info2">
    	<p class="p1 f-l">地址：<span >${data.addr_name}</span></p>
        <div style="clear:both;"></div>
    </div> 
    <div class="drdd-info2">
    <p class="p1 f-l">配送方式：<span>
    	<c:if test="${data.receive==''}">快递</c:if>
    	<c:if test="${data.receive!=''}">自提点：${data.receive}</c:if></span></p>
        <div style="clear:both;"></div>
    </div>
    <c:if test="${!empty data.note}">
    <div class="drdd-info2">
    	<p class="p1 f-l">备注：<span >${data.note}</span></p>
        <div style="clear:both;"></div>
    </div> 
    </c:if>
                <div class="my-p2">
                	<span class="my-sp3 f-l">共${data.goods_total_num}个商品</span>
                   <p class="my-sp3 f-r">总计：￥${data.goods_total}</p>
                    <div style="clear:both;"></div>
                </div>
            </div>
        </div>
        <c:if test="${data.objKdniaoVo!=null}">
        <div class="my-info">
        	<div class="my-k1">
         		<ul class="my-p1">
                	<li class="my-spl f-l">物流信息：中通快递</li>
                    <div style="clear:both;"></div>
                </ul>
                <ul class="my-p1">
                	<li class="my-spl f-l">运输状态：${data.objKdniaoVo.state==0?"无轨迹":(data.objKdniaoVo.state==1?"已揽收":(data.objKdniaoVo.state==2?"在途中":(data.objKdniaoVo.state==3?"已签收":(data.objKdniaoVo.state==4?"问题件":""))))}</li>
                    <div style="clear:both;"></div>
                </ul>
                <ul style="    font-size: 12px;border-bottom: 1px solid #D6D6D6;color: #979797;">
               		<c:forEach items="${data.objKdniaoVo.traces}" var="ordList">
               			<li>${ordList.acceptTime}&nbsp;&nbsp;${ordList.acceptStation}<br></li>
               		</c:forEach>
                    <div style="clear:both;"></div>
                </ul>
         	</div>
         </div>
         </c:if>
         <c:if test="${data.goods_total>0}">
       		<button class="drdd-btn" onclick="go2UnionPay()" style="margin: 25px auto;">银联支付</button>
       		<button class="drdd-btn" onclick="callpay()" style="margin: 25px auto;">微信支付</button>
        </c:if>
    </div>
    	<script type="text/javascript">
    	function go2UnionPay(){
    		window.location.href='${pageContext.request.contextPath}/main/go2UnionPay.html?payAmount=${data.goods_total}&orderId=${data.order_id}';
    	}
    	
    	function onBridgeReady() {
  			WeixinJSBridge.invoke('getBrandWCPayRequest',
  					{
  					 "appId" : "${payDataAttr['appId']}",
  					 "timeStamp" : "${payDataAttr['timeStamp']}",
  					 "nonceStr" : "${payDataAttr['nonceStr']}",
  					 "package" : "${payDataAttr['package']}",
  					 "signType" : "${payDataAttr['signType']}",
  					 "paySign" : "${payDataAttr['paySign']}" 
  		   			},function(res){
  						WeixinJSBridge.log(res.err_msg);
//  		 				alert(res.err_code + res.err_desc + res.err_msg);
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
    	
  		function callpay(){
  			if (typeof WeixinJSBridge == "undefined") {
  	  		    if (document.addEventListener) {
  	  		        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
  	  		    } else if (document.attachEvent) {
  	  		        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
  	  		        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
  	  		    }
  	  		} else {
  	  		    onBridgeReady();
  	  		}
		}
  </script>
</body>
</html>
