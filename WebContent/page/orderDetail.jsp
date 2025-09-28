<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title>订单明细</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc_v2.1.css">
<link rel="stylesheet prefetch" href="css/photoswipe.css">
<link rel="stylesheet prefetch" href="css/default-skin.css">
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
                	<li class="my-spl f-r">${data.statusVal}
                	</li>
                    <div style="clear:both;"></div>
                </ul>
            	<ul class="my-p1">
                	<li class="my-spl f-l">微信订单号：${data.order_id}</li>
                    <div style="clear:both;"></div>
                </ul>
                <ul class="my-p1">
                	<li class="my-spl f-l">内部订单号：${data.orderNO}</li>
                    <div style="clear:both;"></div>
                </ul>
                <ul class="my-p1">
                	<li class="my-spl f-l">下单日期：${data.add_time}</li>
                    <div style="clear:both;"></div>
                </ul>
    
   
                <c:forEach items="${data.objRMOrderDetailVo}" var="ordList">
                <dl class="my-gallery my-dl1">
                	<dt><a href="${pageContext.request.contextPath }/page/images/produce/hd/${ordList.invID}.jpg" data-size="1024x1024" data-med="${pageContext.request.contextPath }/page/images/produce/hd/${ordList.invID}.jpg" data-med-size="2048x2048" data-author="Michael Hull">
				          <img style="width: 68px" src="${pageContext.request.contextPath }/page/images/produce/thumbnail/${ordList.invID}.png" alt="">
				        </a></dt>
                    <dd  style="width: 53%;">
                    	<h3><a href="#">${ordList.invName}</a></h3>
                        <div class="my-jdt">
                        	<p class="jdt-p1 f-l" style="font-size: 12px;">数量：${ordList.qty}罐（${ordList.nQty}件）</p>
                    		<div style="clear:both;"></div>
                        </div>
                        <p class="my-dp1">价格：<span>￥<fmt:formatNumber type="number" value="${ordList.price}" pattern="0.00" maxFractionDigits="2" /></span></p>
                    </dd>
                    <p class="f-r" style="font-size:10px;color: #f60;line-height: 68px;">￥<fmt:formatNumber type="number" value="${ordList.amount}" pattern="0.00" maxFractionDigits="2" /></p>
                    <div style="clear:both;"></div>
                </dl>
                </c:forEach>
                 <c:if test="${data.receiveType=='0'}">
				    <div class="drdd-info2">
				    	<p class="p1 f-l"><span style="width: 15%;float: left;">地址：</span><span style="width: 85%;float: left;">${data.addr_name}</span></p> 
				        <div style="clear:both;"></div>
				    </div> 
    			</c:if>
    <div class="drdd-info2">
    <p class="p1"><span class="f-l">配送方式：
    	<c:if test="${data.receiveType=='0'}">物流运输</c:if>
    	<c:if test="${data.receiveType=='1'}">自提</c:if>
    	<c:if test="${data.receiveType=='2'}">厂家配送</c:if></span>
    	<c:if test="${data.receiveType=='0'}"><span class="f-r">物流费用:￥<fmt:formatNumber type="number" value="${data.expressCost}" pattern="0.00" maxFractionDigits="2" /></span></c:if>
    	</p>
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
                   <p class="my-sp3 f-r">总计：￥<fmt:formatNumber type="number" value="${data.goods_total}" pattern="0.00" maxFractionDigits="2" /></p>
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
         <div>
         	<c:if test="${data.ispd!=1&&data.status>10&&data.status<20&&currentDayPayQuota==false}">
	         	<button class="drdd-btn" onclick="go2UnionPay()" style="margin: 10px auto;">银联支付</button>
	        </c:if>
	     	<c:if test="${data.ispd!=1&&data.status>10&&data.status<20&&currentDayPayQuota==true}">
	       		<button class="drdd-btn" onclick="callpay()" style="margin: 10px auto;">微信支付</button>
	        </c:if>
	        <c:if test="${data.status==14||data.status==24}">
	       		<button class="drdd-btn" onclick="signOrder('${data.order_id}')" style="margin: 10px auto; background-color: #4caf50;">签收</button>
	        </c:if>
	        <c:if test="${data.status==11||data.status==16 }">
	        	<button class="drdd-btn" onclick="cancleOrder('${data.order_id}')" style="margin: 10px auto;background-color: #9e9e9e;">取消</button>
	    	</c:if>
    	</div>
    	<div style="height: 50px;"></div>
    </div>
    
    
     <div id="gallery" class="pswp" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="pswp__bg"></div>
        <div class="pswp__scroll-wrap">
          <div class="pswp__container">
			<div class="pswp__item"></div>
			<div class="pswp__item"></div>
			<div class="pswp__item"></div>
          </div>
          <div class="pswp__ui pswp__ui--hidden">
            <div class="pswp__top-bar">
				<div class="pswp__counter"></div>
				<button class="pswp__button pswp__button--close" title="Close (Esc)"></button>
				<button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>
				<div class="pswp__preloader">
					<div class="pswp__preloader__icn">
					  <div class="pswp__preloader__cut">
					    <div class="pswp__preloader__donut"></div>
					  </div>
					</div>
				</div>
            </div>
            <div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">
	            <div class="pswp__share-tooltip">
	            </div>
	        </div>
            <button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)"></button>
            <button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)"></button>
            <div class="pswp__caption">
              <div class="pswp__caption__center">
              </div>
            </div>
          </div>
        </div>
    </div>
    
    <script src="js/photoswipe.min.js"></script>
	<script src="js/photoswipe-ui-default.min.js"></script>
	<script type="text/javascript">
		function cancleOrder(order_id){
			var  b = confirm('确定取消吗？');
			if(!b){
			return ;
			}
			$.ajax({
				url:'orderCancel.html',
				type:'post',
				data:'order_id='+order_id,
				success:function(rs){
					if(rs==1){
						alert("取消成功！");
						location.reload();
					}else{
						alert("失败，请联系客服！");
					}
				}
			})
		}
		
		function signOrder(order_id){
			var  b = confirm('是否签收该订单?');
			if(!b){
				return ;
			}
			$.ajax({
				url:'orderSign.html',
				type:'post',
				data:'orderId='+order_id,
				success:function(rs){
					if(rs==1){
						alert("签收成功，谢谢您的配合，祝您生活愉快！");
						location.reload();
					}else{
						alert("失败，请联系客服！");
					}
				}
			})
		}
		
    	function go2UnionPay(){
    		window.location.href='${pageContext.request.contextPath}/main/go2UnionPay.html?payAmount=${data.goods_total}&isYLZF=${data.isYLZF}&orderId=${data.order_id}';
    	}
    	
  		function callpay(){
  			if("${currentDayPayQuota}"=="false"){
  				alert("当天限额支付1000元，您已超额，请联系客服0757-27682222");
  				return ;
  			}
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
  		
  		function onBridgeReady() {
  			WeixinJSBridge.invoke('getBrandWCPayRequest',
  					{
  					 "appId" : "${payDataAttr.appId}",
  					 "timeStamp" : "${payDataAttr.timeStamp}",
  					 "nonceStr" : "${payDataAttr.nonceStr}",
  					 "package" : "${payDataAttr.packageVal}",
  					 "signType" : "${payDataAttr.signType}",
  					 "paySign" : "${payDataAttr.paySign}" 
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
 	</script>
 <script type="text/javascript">
    (function() {

		var initPhotoSwipeFromDOM = function(gallerySelector) {

			var parseThumbnailElements = function(el) {
			    var thumbElements = el.childNodes,
			        numNodes = thumbElements.length,
			        items = [],
			        el,
			        childElements,
			        thumbnailEl,
			        size,
			        item;

			    for(var i = 0; i < numNodes; i++) {
			        el = thumbElements[i];

			        // include only element nodes 
			        if(el.nodeType !== 1) {
			          continue;
			        }

			        childElements = el.children;

			        size = el.getAttribute('data-size').split('x');

			        // create slide object
			        item = {
						src: el.getAttribute('href'),
						w: parseInt(size[0], 10),
						h: parseInt(size[1], 10),
						author: el.getAttribute('data-author')
			        };

			        item.el = el; // save link to element for getThumbBoundsFn

			        if(childElements.length > 0) {
			          item.msrc = childElements[0].getAttribute('src'); // thumbnail url
			          if(childElements.length > 1) {
			              item.title = childElements[1].innerHTML; // caption (contents of figure)
			          }
			        }


					var mediumSrc = el.getAttribute('data-med');
		          	if(mediumSrc) {
		            	size = el.getAttribute('data-med-size').split('x');
		            	// "medium-sized" image
		            	item.m = {
		              		src: mediumSrc,
		              		w: parseInt(size[0], 10),
		              		h: parseInt(size[1], 10)
		            	};
		          	}
		          	// original image
		          	item.o = {
		          		src: item.src,
		          		w: item.w,
		          		h: item.h
		          	};

			        items.push(item);
			    }

			    return items;
			};

			// find nearest parent element
			var closest = function closest(el, fn) {
			    return el && ( fn(el) ? el : closest(el.parentNode, fn) );
			};

			var onThumbnailsClick = function(e) {
			    e = e || window.event;
			    e.preventDefault ? e.preventDefault() : e.returnValue = false;

			    var eTarget = e.target || e.srcElement;

			    var clickedListItem = closest(eTarget, function(el) {
			        return el.tagName === 'A';
			    });

			    if(!clickedListItem) {
			        return;
			    }

			    var clickedGallery = clickedListItem.parentNode;

			    var childNodes = clickedListItem.parentNode.childNodes,
			        numChildNodes = childNodes.length,
			        nodeIndex = 0,
			        index;

			    for (var i = 0; i < numChildNodes; i++) {
			        if(childNodes[i].nodeType !== 1) { 
			            continue; 
			        }

			        if(childNodes[i] === clickedListItem) {
			            index = nodeIndex;
			            break;
			        }
			        nodeIndex++;
			    }

			    if(index >= 0) {
			        openPhotoSwipe( index, clickedGallery );
			    }
			    return false;
			};

			var photoswipeParseHash = function() {
				var hash = window.location.hash.substring(1),
			    params = {};

			    if(hash.length < 5) { // pid=1
			        return params;
			    }

			    var vars = hash.split('&');
			    for (var i = 0; i < vars.length; i++) {
			        if(!vars[i]) {
			            continue;
			        }
			        var pair = vars[i].split('=');  
			        if(pair.length < 2) {
			            continue;
			        }           
			        params[pair[0]] = pair[1];
			    }

			    if(params.gid) {
			    	params.gid = parseInt(params.gid, 10);
			    }

			    return params;
			};

			var openPhotoSwipe = function(index, galleryElement, disableAnimation, fromURL) {
			    var pswpElement = document.querySelectorAll('.pswp')[0],
			        gallery,
			        options,
			        items;

				items = parseThumbnailElements(galleryElement);

			    // define options (if needed)
			    options = {

			        galleryUID: galleryElement.getAttribute('data-pswp-uid'),

			        getThumbBoundsFn: function(index) {
			            // See Options->getThumbBoundsFn section of docs for more info
			            var thumbnail = items[index].el.children[0],
			                pageYScroll = window.pageYOffset || document.documentElement.scrollTop,
			                rect = thumbnail.getBoundingClientRect(); 

			            return {x:rect.left, y:rect.top + pageYScroll, w:rect.width};
			        },

			        addCaptionHTMLFn: function(item, captionEl, isFake) {
						if(!item.title) {
							captionEl.children[0].innerText = '';
							return false;
						}
						captionEl.children[0].innerHTML = item.title +  '<br/><small>Photo: ' + item.author + '</small>';
						return true;
			        }
					
			    };


			    if(fromURL) {
			    	if(options.galleryPIDs) {
			    		// parse real index when custom PIDs are used 
			    		// http://photoswipe.com/documentation/faq.html#custom-pid-in-url
			    		for(var j = 0; j < items.length; j++) {
			    			if(items[j].pid == index) {
			    				options.index = j;
			    				break;
			    			}
			    		}
				    } else {
				    	options.index = parseInt(index, 10) - 1;
				    }
			    } else {
			    	options.index = parseInt(index, 10);
			    }

			    // exit if index not found
			    if( isNaN(options.index) ) {
			    	return;
			    }



				var radios = document.getElementsByName('gallery-style');
				for (var i = 0, length = radios.length; i < length; i++) {
				    if (radios[i].checked) {
				        if(radios[i].id == 'radio-all-controls') {

				        } else if(radios[i].id == 'radio-minimal-black') {
				        	options.mainClass = 'pswp--minimal--dark';
					        options.barsSize = {top:0,bottom:0};
							options.captionEl = false;
							options.fullscreenEl = false;
							options.shareEl = false;
							options.bgOpacity = 0.85;
							options.tapToClose = true;
							options.tapToToggleControls = false;
				        }
				        break;
				    }
				}

			    if(disableAnimation) {
			        options.showAnimationDuration = 0;
			    }

			    // Pass data to PhotoSwipe and initialize it
			    gallery = new PhotoSwipe( pswpElement, PhotoSwipeUI_Default, items, options);

			    // see: http://photoswipe.com/documentation/responsive-images.html
				var realViewportWidth,
				    useLargeImages = false,
				    firstResize = true,
				    imageSrcWillChange;

				gallery.listen('beforeResize', function() {

					var dpiRatio = window.devicePixelRatio ? window.devicePixelRatio : 1;
					dpiRatio = Math.min(dpiRatio, 2.5);
				    realViewportWidth = gallery.viewportSize.x * dpiRatio;


				    if(realViewportWidth >= 1200 || (!gallery.likelyTouchDevice && realViewportWidth > 800) || screen.width > 1200 ) {
				    	if(!useLargeImages) {
				    		useLargeImages = true;
				        	imageSrcWillChange = true;
				    	}
				        
				    } else {
				    	if(useLargeImages) {
				    		useLargeImages = false;
				        	imageSrcWillChange = true;
				    	}
				    }

				    if(imageSrcWillChange && !firstResize) {
				        gallery.invalidateCurrItems();
				    }

				    if(firstResize) {
				        firstResize = false;
				    }

				    imageSrcWillChange = false;

				});

				gallery.listen('gettingData', function(index, item) {
				    if( useLargeImages ) {
				        item.src = item.o.src;
				        item.w = item.o.w;
				        item.h = item.o.h;
				    } else {
				        item.src = item.m.src;
				        item.w = item.m.w;
				        item.h = item.m.h;
				    }
				});

			    gallery.init();
			};

			// select all gallery elements
			var galleryElements = document.querySelectorAll( gallerySelector );
			for(var i = 0, l = galleryElements.length; i < l; i++) {
				galleryElements[i].setAttribute('data-pswp-uid', i+1);
				galleryElements[i].onclick = onThumbnailsClick;
			}

			// Parse URL and open gallery if it contains #&pid=3&gid=1
			var hashData = photoswipeParseHash();
			if(hashData.pid && hashData.gid) {
				openPhotoSwipe( hashData.pid,  galleryElements[ hashData.gid - 1 ], true, true );
			}
		};

		initPhotoSwipeFromDOM('.my-gallery');

	})();

	</script>
	
</body>
</html>
