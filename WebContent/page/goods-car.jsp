<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title>自助下单</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc_v2.1.css">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/showTip.js"></script>
<link rel="stylesheet prefetch" href="css/photoswipe.css">
<link rel="stylesheet prefetch" href="css/default-skin.css">
<style type="text/css">
.gwc-ul1 li .gwc-del{ margin-top:opx !important;}
.gwc-ft button {    width: 76px !important;}
.gwc-ft p { margin-top: 3px;}
</style>
</head>

<body id="wrap">
    <ul class="gwc-ul1">
    <form action="${pageContext.request.contextPath}/page/prepareOrder.html" method="post" id="calForm">
    <c:forEach items="${goods}" var="list" varStatus="s">
    	<li>
        	<div class="hwc-tu f-l my-gallery">
				<a href="${pageContext.request.contextPath }/page/images/produce/hd/${list.goods_code}.jpg" data-size="1024x1024" data-med="${pageContext.request.contextPath }/page/images/produce/hd/${list.goods_code}.jpg" data-med-size="2048x2048" data-author="Michael Hull">
				          <img style="width: 68px" src="${pageContext.request.contextPath }/page/images/produce/thumbnail/${list.goods_code}.png" alt="">
        	</div>
            <div class="gwc-md f-l">
            	<h3><a href="javascript:;">${list.goods_name}</a></h3>
                <p class="gwc-p1" style="line-height: 30px;margin-top:-3px"><span style="font-size: 12px;">规格：${list.goods_spe}&nbsp;&nbsp;&nbsp;&nbsp;罐/件：${list.nbox}</span></p>
                <p class="gwc-p1"><span style="color: #f60;font-size: 16px;">￥<fmt:formatNumber type="number" value="${(custId!=''&&custId!=null)?list.goods_price*list.nbox:list.goods_price}" pattern="0.00" maxFractionDigits="2" /></span></p>
            </div>
            <%-- <c:if test="${list.goods_price!=0}"> --%>
            <div class="f-r" style="height: 68px;">
            	<c:if test="${custId!=null&&custId!='' }">
		            <div style="height: 34px;text-align: center;" align="center">
			            <div style="font-size: 18px; text-align: left;">件</div>
			            <div>
				            <a href="javascript:;" onclick="plusPiece('${list.goods_id}','${list.goods_price}','${list.nbox}')" class="f-r"><img src="images/11.png" style="width: 18px;height: 18px"></a>
				            <a href="javascript:;" class="f-r" id="nbox${list.goods_id}" style="width: 30px;margin: -3px auto;">0</a>
				            <a href="javascript:;" onclick="minPiece('${list.goods_id}','${list.goods_price}','${list.nbox}')" class="f-r"><img src="images/22.png" style="width: 18px;height: 18px"></a>
			            </div>
		           	</div>
	           	</c:if>
	           	<c:if test="${custId==''||custId==null }">
	           	<div style="height: 34px;text-align: center;" align="center">
			       <div style="font-size: 18px; text-align: left;">罐</div>
			       <div>
			       		<a href="javascript:;" onclick="plusPot('${list.goods_id}','${list.goods_price}','${list.nbox}')" class="f-r"><img src="images/11.png" style="width: 18px;height: 18px"></a>
			            <a href="javascript:;" class="f-r" id="goods_num${list.goods_id}" idProperties="${list.goods_id}" style="width: 30px;margin: -3px auto;">0</a>
			            <a href="javascript:;" onclick="minPot('${list.goods_id}','${list.goods_price}','${list.nbox}')" class="f-r"><img src="images/22.png" style="width: 18px;height: 18px"></a>
	          		</div> 
	          	</div> 
	          	</c:if>
          	</div>
            <div style="clear:both;"></div>
        </li>
        <input type="hidden" name="id" value="${list.goods_id}">
        <input type="hidden" name="goodsName" value="${list.goods_name}">
        <input type="hidden" name="goodsImg" value="${list.goods_img}">
        <input type="hidden" name="nbox" value="${list.nbox}">
        <input type="hidden" name="price" id="price${list.goods_id}" value="${list.goods_price}">
        <input type="hidden" name="goodsNbox" id="goodsNbox${list.goods_id}" value="0" idProperties="${list.goods_id}">
        <input type="hidden" name="goodsNum" id="goodsNum${list.goods_id}" value="0" idProperties="${list.goods_id}">
        <input type="hidden" name="goodsWeight" id="goodsWeight${list.goods_weight}" value="${list.goods_weight}">
        <input type="hidden" name="goodsCode" id="goodsCode${list.goods_id}" value="${list.goods_code}">
       </c:forEach> 
        <input type="hidden" name="goodsNboxAmount" id="goodsNboxAmount" value="">
        <input type="hidden" name="goodsAmount" id="goodsAmount" value="">
        <input type="hidden" name="calTotalAmount" id="calTotalAmount" value="">
      </form>
        <div style="height: 15px;"></div>
    </ul>
    <input type="hidden" value="${tnum}" id='tnum1'>
    <input type="hidden" value="${tprice}" id='tprice1'>
    <div class="gwc-ft">
    	<p id="tnum">共<span id="calNum">0</span>${(custId==''||custId==null)?"罐":"件" }商品，总计：<span style="color: #f60" id="calAmount">￥0.00</span></p>
    	<c:if test="${tnum==0}">
        <button onclick="showTip('请添加商品到购物车！')" style="margin-top: 2px;">厂家直购</button>
        </c:if>
        <c:if test="${tnum!=0}">
        <button onclick="submitCalData();" style="margin-top: 2px;">厂家直购</button>
        </c:if>
        <button onclick="nearBuy();" style="margin-top: 2px;">线下商店</button>
        <div style="clear:both;"></div>
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

    <jsp:include page="footer3.jsp"></jsp:include>
    <script src="js/photoswipe.min.js"></script>
	<script src="js/photoswipe-ui-default.min.js"></script>
    <script type="text/javascript">
    function plusPiece(goods_id,goods_price,nbox){
    	var nboxNum=$('#nbox'+goods_id).text();
    	nboxNum = parseInt(nboxNum);
    	var goodsNum=$('#goodsNum'+goods_id).val();
    	nbox = parseInt(nbox);
    	goodsNum = parseInt(goodsNum);
    	$('#nbox'+goods_id).text(nboxNum+1);
    	//$('#goods_num'+goods_id).text(goodsNum+nbox);
    	
    	$('#goodsNbox'+goods_id).val(nboxNum+1);
    	$('#goodsNum'+goods_id).val(goodsNum+nbox);
    	
    	calResult();
    }
    
    function plusPot(goods_id,goods_price,nbox){
    	var goodsNum=$('#goods_num'+goods_id).text();
    	nbox = parseInt(nbox);
    	goodsNum = parseInt(goodsNum);
    	//$('#nbox'+goods_id).text(parseInt((goodsNum+1)/nbox));
    	$('#goods_num'+goods_id).text(goodsNum+1);
    	
    	$('#goodsNbox'+goods_id).val(parseInt((goodsNum+1)/nbox));
    	$('#goodsNum'+goods_id).val(goodsNum+1);
    	calResult();
    }
    
    function minPiece(goods_id,goods_price,nbox){
    	var nboxNum=$('#nbox'+goods_id).text();//选择的件数值
    	nboxNum = parseInt(nboxNum);
    	if((nboxNum-1)<0){
    		return ;
    	}
    	var goodsNum=$('#goodsNum'+goods_id).val();//灌数
    	nbox = parseInt(nbox);
    	goodsNum = parseInt(goodsNum);
    	$('#nbox'+goods_id).text(nboxNum-1);
    	//$('#goods_num'+goods_id).text(goodsNum-nbox);
    	
    	$('#goodsNbox'+goods_id).val(nboxNum-1);
    	$('#goodsNum'+goods_id).val(goodsNum-nbox);
    	calResult();
    }
    
    function minPot(goods_id,goods_price,nbox){
    	var goodsNum=$('#goods_num'+goods_id).text();
    	goodsNum = parseInt(goodsNum);
    	if((goodsNum-1)<0){
    		return ;
    	}
    	nbox = parseInt(nbox);
    	//$('#nbox'+goods_id).text(parseInt((goodsNum-1)/nbox));
    	$('#goods_num'+goods_id).text(goodsNum-1);
    	
    	$('#goodsNbox'+goods_id).val(parseInt((goodsNum-1)/nbox));
    	$('#goodsNum'+goods_id).val(goodsNum-1);
    	calResult();
    }
    
    function calResult(){
    	var calNum = 0;
    	var calAmount = 0.00;
    	var goodsNbox = 0;
    	var unit = "${custId!=null&&custId!=''?'1':'2' }";//1为件，2为罐
    	$("input[id^='goodsNum']").each(function(i){  
    		 var goodsNum = $(this).val();
    		 var id = $(this).attr('idProperties');
    		 var priceVal = $('#price'+id).val();
    		 var goodsNbox_ = $('#goodsNbox'+id).val();
    		 goodsNum = parseFloat(goodsNum);
    		 goodsNbox_ = parseFloat(goodsNbox_);
    		 priceVal = parseFloat(priceVal);
    		 
    		 calNum += goodsNum;
    		 calAmount += goodsNum*priceVal;
    		 goodsNbox += goodsNbox_;
    	});
    	
    	$('#calNum').text(unit==1?goodsNbox:calNum);
    	$('#calAmount').text("￥"+toDecimal2(calAmount));
    	
    	$('#goodsNboxAmount').val(goodsNbox);
    	$('#goodsAmount').val(calNum);
    	$('#calTotalAmount').val(toDecimal2(calAmount));
    }
    
    function nearBuy(){
    	window.location.href="${pageContext.request.contextPath}/page/go2NearbyStore.html?a=1";
    }
    
    function submitCalData(){
    	var goodsNumAmount = 0;
    	$("input[id^='goodsNum']").each(function(i){  
   		 	var goodsNum = $(this).val();
   			goodsNum = parseFloat(goodsNum);
   			goodsNumAmount += goodsNum;
    	});
    	if(goodsNumAmount!=0){
    		$('#calForm').submit();
    	}else{
    		alert("请添加需要的商品再提交!");
    	}
		/* $.ajax({
			url:'prepareOrder.html',
			data:$('#calForm').serialize(),
			type : "POST",
			dataType: "json",
			async:false,
			success:function(rs){
				window.location.href = "${pageContext.request.contextPath}/page/preparedOrder";
			}
		}) */
	}
    
    function toDecimal2(x) { 
	  	 var f = parseFloat(x);
	  	 if (isNaN(f)) {
	  		return false;
	  	 }
	  	 var f = Math.round(x*100)/100;
	  	 var s = f.toString();
	  	 var rs = s.indexOf('.');
	  	 if (rs < 0) {
	  	 rs = s.length;
	  	 	s += '.';
	  	 }
	  	 while (s.length <= rs + 2) {
	  	 	s += '0';
	  	 }
	  	 return s;
  	 } 
    
    function plus(goods_id,goods_price,sort){
    	var goods_num1=$('#goods_num'+sort).text();
    	var goods_num=parseInt(goods_num1)+1;
    	var goods_total  = goods_num*goods_price;
    	var tnum1 = $('#tnum1').val();
    	var tprice1 = $('#tprice1').val();
    	var tnum = parseInt(tnum1)+1;
    	cart_num = parseInt(cart_num)+1;
    	var tprice = (parseFloat(tprice1)+parseFloat(goods_price)).toFixed(1);
    	$.ajax({
    		url:'cartUpdate.html',
    		type:'post',
    		data:'goods_id='+goods_id+'&goods_price='+goods_price+'&goods_num='+goods_num+'&s=1',
    		success:function(rs){
    				var data = eval('('+rs+')');
    				if(data.rs_code==1){
    				$('#cart_num').text(data.cart_num);
    				
    				$('#tnum1').val(tnum);
    		    	$('#tprice1').val(tprice);
    		    	$('#goods_num'+sort).text(goods_num);
    		    	$('#tnum').html("共"+tnum+"件商品，总计：<span style='color: #f60'>￥"+tprice+"</span>");
    			}else if(data.rs_code==1005){
					showTip("登录已失效，重新登录中，请稍后...");
					setTimeout('window.location.href=history.go(-1)',2000);
				}
    		}
    	})
    }
    function min(goods_id,goods_price,sort){
    	var goods_num1=$('#goods_num'+sort).text();
    	if(goods_num1==1||goods_num1<1){
//    		var sign=0;
 //           new $.flavr(
//				{
//					content : '确定删除此商品吗?',
//					dialog : 'confirm',
//					onConfirm : function() {
//						sign=1;
//						alert(sign);
//					},
//					onCancel : function() {
						
//					}
//			});
    		var  b = confirm('确定删除此商品吗？');
    		if(!b){
    		return ;
    		}
    	}
    	var goods_num=parseInt(goods_num1)-1;
    	var goods_total  = goods_num*goods_price;
    	var tnum1 = $('#tnum1').val();
    	var tprice1 = $('#tprice1').val();
    	var tnum = parseInt(tnum1)-1;
    	var tprice = (parseFloat(tprice1)-parseFloat(goods_price)).toFixed(1);
    	$('#tnum1').val(tnum);
    	$('#tprice1').val(tprice);
    	$('#goods_num'+sort).text(goods_num);
    	$('#tnum').html("共"+tnum+"件商品，总计：<span>￥"+tprice+"</span>");
    	cart_num = parseInt(cart_num)-1;
    	$.ajax({
    		url:'cartUpdate.html',
    		type:'post',
    		data:'goods_id='+goods_id+'&goods_price='+goods_price+'&goods_num='+goods_num+'&cart_num='+cart_num+'&s=0',
    		success:function(rs){
    			var data = eval('('+rs+')');
				if(data.rs_code==1){
				$('#cart_num').text(data.cart_num);
    				if(goods_num<1){
    					location.reload();
    				}
    				$('#tnum1').val(tnum);
    		    	$('#tprice1').val(tprice);
    		    	$('#goods_num'+sort).text(goods_num);
    		    	$('#tnum').html("共"+tnum+"件商品，总计：<span>￥"+tprice+"</span>");
    			}else if(data.rs_code==1005){
					showTip("登录已失效，重新登录中，请稍后...");
					setTimeout('window.location.href=history.go(-1)',2000);
				}
    		}
    	})
    }
    
    function del(goods_id){
    	cart_num = parseInt(cart_num)-1;
    	$.ajax({
    		url:'cartDel.html',
    		type:'post',
    		data:'goods_id='+goods_id,
    		success:function(rs){
    			var data = eval('('+rs+')');
				if(data.rs_code==1){
				$('#cart_num').text(data.cart_num);
    				location.reload();
    			}
				else if(data.rs_code==1005){
					showTip("登录已失效，重新登录中，请稍后...");
					setTimeout('window.location.href=history.go(-1)',2000);
				}else{
    				showTip('系统故障');
    			}
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
