<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<title>订单确认</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/shoujisc_v2.1.css">
<link rel="stylesheet prefetch" href="css/photoswipe.css">
<link rel="stylesheet prefetch" href="css/default-skin.css">
<script type="text/javascript" src="js/jquery.js"></script>

<link rel="stylesheet" type="text/css" href="css/showTip.css">
<script type="text/javascript" src="js/showTip.js"></script>
<script type="text/javascript" src="js/area.js"></script>
<script src="${pageContext.request.contextPath}/page/distribution/js/loading.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/page/distribution/css/loading.css">

<style type="text/css">
.goodContent {
	font-weight: normal;
	font-size: 11px;
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
}

.goodCal {
	font-size: 12px;
	color: #f60;
	line-height: 45px;
}
</style>

</head>

<body id="wrap">
	<form action="" id="createOrderForm">
     <input type="hidden" value="${addr_id}" id='addr_id'>
       
    <dl class="drdd-info6" onclick="window.location.href='addrList.html?cps_id=${cps_id}&addr_id=${addr_id}'">
     <c:forEach items="${addr}" var="addr" begin="0" end="0">
     <input type="hidden" value="${addr.addr_user }" id='addr_user'>
     <input type="hidden" value="${addr.addr_tel}" id='addr_tel'>
     <input type="hidden" value="${addr.addr_name}" id='addr_name'>
    	<dt>
        	<p>
            	<span class="f-l">收货人：${addr.addr_user }</span>
            	<span class="f-r">联系电话：${addr.addr_tel }</span>
                <div style="clear:both;"></div>
            </p>
    		<p>
    			<span class="f-l">收货地址：</span><span class="f-l" style="width: 78%;" id="addressContent">${addr.cityPickerCN }&nbsp;${addr.addr_name }</span></br>
    		</p>
        </dt>
       </c:forEach> 
       <c:if test="${empty addr}">
		<dt style="padding-top:15px;margin-left:10px">
        	
         <span class="f-l" >点击添加收货地址</span>
            	
              
        </dt>
		</c:if>
        <dd><a>></a></dd>
        <div style="clear:both;"></div>
    </dl>
    
    
    <input type="hidden" value="<fmt:formatNumber type="number" value="${calDataVo.goodsAmount}" pattern="0" maxFractionDigits="0" />" id='tnum'>
    <input type="hidden" value="${tprice}" id='tprice'>
      <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E">
  	 商品信息 
  	 <c:forEach items="${calDataVo.id}" var="list" varStatus="s">
    	<c:if test="${calDataVo.goodsNum[s.index]!=null&&calDataVo.goodsNum[s.index]!=0 }">
    		<input type="hidden" value="${calDataVo.id[s.index]}" name='goods_id'>
		    <input type="hidden" value="${calDataVo.goodsName[s.index]}" name='goods_name'>
		    <input type="hidden" value="${calDataVo.goodsImg[s.index]}" name='goods_img'>
		    <input type="hidden" value="<fmt:formatNumber type="number" value="${calDataVo.goodsNum[s.index]}" pattern="0" maxFractionDigits="0" />" name='goods_num'>
		    <input type="hidden" value="${calDataVo.price[s.index]}" name='goods_price'>
		    
		   
		    <div class="drdd-info3">
		    	<div class="my-gallery drdd-if3tu f-l">
		        	 <a href="${pageContext.request.contextPath }/page/images/produce/hd/${calDataVo.goodsCode[s.index]}.jpg" data-size="1024x1024" data-med="${pageContext.request.contextPath }/page/images/produce/hd/${calDataVo.goodsCode[s.index]}.jpg" data-med-size="2048x2048" data-author="Michael Hull">
				          <img style="width: 43px" src="${pageContext.request.contextPath }/page/images/produce/thumbnail/${calDataVo.goodsCode[s.index]}.png" alt="">
				        </a>
		        </div>
		        <h3 class="goodContent f-l">
		        <span><a href="#">${calDataVo.goodsName[s.index]}</a></span><br>
		        <span style="height: 2px;"></span><span style="color: #3A3636;">数量:<fmt:formatNumber type="number" value="${calDataVo.goodsNum[s.index]}" pattern="0" maxFractionDigits="0" />罐（<fmt:formatNumber type="number" value="${calDataVo.goodsNum[s.index]/calDataVo.nbox[s.index]}" pattern="0.00" maxFractionDigits="2" />件）</span><br>
		        <span style="height: 2px;"></span><span style="color: #3A3636;">单价:<span style="color: #f60;">￥<fmt:formatNumber type="number" value="${calDataVo.price[s.index]}" pattern="0.00" maxFractionDigits="2" />${calDataVo.preference1[s.index]!=null?'&nbsp;组合优惠':'' }</span></span>
		        </h3>
		        <p class="goodCal f-r">￥<fmt:formatNumber type="number" value="${calDataVo.goodsNum[s.index]*calDataVo.price[s.index]}" pattern="0.00" maxFractionDigits="2" /></p>
		        <div style="clear:both;"></div>
		    </div>
    	</c:if>
    </c:forEach>
    </div>
   
    <c:if test="${tprice!=0}">
     <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E">
  	  优惠券
    </div>
    <div class="drdd-info4" onclick="window.location.href='cartCoupons.html?addr_id=${addr_id}'" >
    	<p>优惠券</p>
        <a >
        <c:forEach items="${cps}" var="cps">
        <input type="hidden" value="${cps.cps_id}" id='cps_id'>
        <input type="hidden" value="${cps.cps_name}" id='cps_name' >
        <input type="hidden" value="${cps.cps_price}" id='cps_price'>
        ${cps.cps_name} ￥${cps.cps_price}
        </c:forEach>
        <c:if test="${empty cps}">
        	${cpsCount}张可使用优惠券
        </c:if>
        
         <span> ></span></a>
        <div style="clear:both;"></div>
    </div>
    </c:if>
     <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E">
  	  配送方式
    </div>
    <div class="drdd-info4">
    	<p>配送方式</p>
    	<a href="#">
    	<select id="fgt_price" onchange="fgt()" style="border: 0">
    	<option value="-2">请选择</option>
    	<option value="0" ${custId==null||custId==''?"selected":""}>快递：
	    	<c:if test="${fgt_price==0}">免运费</c:if>
	    	<c:if test="${fgt_price!=0}">${fgt_price}元</c:if>
    	 </option>
    	<option value="1">自提点(联系客服协议运费)</option>
    	${custId==null||custId==''?"":"<option value=\"2\" selected>厂家配送"}
    	</option>
    	</select>
        <span>></span></a>
        <div style="clear:both;"></div>
    </div>
    
     <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E;display:none;" id="zitidian-str">
  	  联系客服
    </div>
    <div class="drdd-info4" style="display:none;font-size: 14px;color: #333;" id="zitidian-choose" >
    	客服联系电话：0757-27686666<a href="tel:0757-27686666"><img alt="打电话" src="${pageContext.request.contextPath}/page/distribution/images/call.png"/></a>
    </div>
    
    <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E">
  	 备注
    </div>
    <div class="drdd-info4">
    	<p>备注：</p>
      
        <input type="text" placeholder="选填，填写您对卖家的要求" id='note' style="width:80%;border:0px">
     
        <div style="clear:both;"></div>
    </div>
     <div style="font-size: 12px;padding-left:5px; margin-top:13px;color: #A09E9E">
  	  订单价格
    </div>
    <div class="drdd-info2">
    
    	<p class="p1 f-l">商品总价</p>
    	<p class="p2 f-r"><span id="">￥<fmt:formatNumber type="number" value="${calDataVo.calTotalAmount}" pattern="0.00" maxFractionDigits="2" /></span></p>
        <br>
       
        <p class="p1 f-l">物流费用</p>
    	<p class="p2 f-r"><span id="wuliu">￥<fmt:formatNumber type="number" value="${fgt_price}" pattern="0.00" maxFractionDigits="2" /></span></p>
        <br>
        
        <c:if test="${calDataVo.preference1TotalAmount!=null&&calDataVo.preference1TotalAmount!=0 }">
        	<p class="p1 f-l">组合优惠</p>
	    	<p class="p2 f-r"><span id="">-￥<fmt:formatNumber type="number" value="${calDataVo.preference1TotalAmount}" pattern="0.00" maxFractionDigits="2" /></span></p>
	        <br>
        </c:if>       
        
        
        <div style="border-bottom:1px solid #DED9D9;">
         <c:forEach items="${cps}" var="cps">
        <p class="p1 f-l">优惠券抵扣</p>
    	<p class="p2 f-r" ><span id="">￥${cps.cps_price}</span></p>
        <br ></c:forEach>
        </div>
        <p class="p1 f-l">共<span id="tnumStr"><fmt:formatNumber type="number" value="${custId==null||custId==''?calDataVo.goodsAmount:calDataVo.goodsNboxAmount}" pattern="0" maxFractionDigits="0" /></span>${custId==null||custId==''?"罐":"件"}商品</p>
    	<p class="p2 f-r">总计：<span id="tpriceStr" style="color: #f60">￥<fmt:formatNumber type="number" value="${tprice}" pattern="0.00" maxFractionDigits="2" /></span></p>
        <div style="clear:both;"></div>
    </div>
    </form>
    <button class="drdd-btn" onclick="add()" id="submitOrder">提交订单</button>
    
    
    
    
   
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
    
    function fgt(){
    	var wuliu ='${fgt_price}';//后台的物流费用

    	var tprice= '${tprice}';//总费用
    	var fgt_price= $('#fgt_price').val();//选择的物流操作方式
    	if(fgt_price==1){ //选择自提点
    		$('#zitidian-str').show();
    		$('#zitidian-choose').show();
    		$('#wuliu').text('￥'+parseFloat(wuliu).toFixed(2));
    	}if(fgt_price==0){//物流配送
    		$('#zitidian-str').hide();
    		$('#zitidian-choose').hide();
    		$('#wuliu').text('￥'+parseFloat(wuliu).toFixed(2));
    	}if(fgt_price==2){//厂家配送
    		$('#zitidian-str').hide();
    		$('#zitidian-choose').hide();
    		$('#wuliu').text('￥0.00');
    		tprice = (parseFloat(tprice)-parseFloat(wuliu));
    	}
    	
   		$('#tpriceStr').text('￥'+parseFloat(tprice).toFixed(2));
   		$('#tprice').val(tprice);
    }
    
    fgt();
    </script>
    <script type="text/javascript">
    function choose_area(){
    	var area =$('#area_area').val();
    	
    	$.ajax({
    		url:'areaJson.html',
    		type:'post',
    		data:'level='+area,
    		success :function(rs){
    			$('#area_addr').html("");
    			$('#area_addr').append('<option value="-2">请选择自提点</option>');
    			var data = eval(rs);
    			$.each(data,function(i,item){
    				$('#area_addr').append('<option value='+data[i].area_name+'>'+data[i].area_name+'</option>');
    			})
    		}
    	});
    }
    
    var submitType = 0;
    function add(){
    	if(submitType==1){
    		return ;
    	}
    	var goods_id="";
    	var goods_name="";
    	var goods_img="";
    	var goods_price="";
    	var goods_num="";
    	var custId = "${custId}";
    	var fgt_price= $('#fgt_price').val();
    	if(fgt_price==-2){
    		showTip("请选择配送方式！");return;
    	}
    	
    	var goods_ids=$("input[name='goods_id']");
    	for (var i = 0; i < goods_ids.length; i++) {
			if (i == 0) {
				goods_id += goods_ids[i].value;
			} else {
				goods_id += ",-=" + goods_ids[i].value;
			}
		}
    	
    	var goods_names=$("input[name='goods_name']") ;
    	for (var i = 0; i < goods_names.length; i++) {
			if (i == 0) {
				goods_name += goods_names[i].value;
			} else {
				goods_name += ",-=" + goods_names[i].value;
			}
		}
    	var goods_imgs =$("input[name='goods_img']");
    	for (var i = 0; i < goods_imgs.length; i++) {
			if (i == 0) {
				goods_img += goods_imgs[i].value;
			} else {
				goods_img += ",-=" + goods_imgs[i].value;
			}
		}
    	var goods_prices=$("input[name='goods_price']") ;
    	for (var i = 0; i < goods_prices.length; i++) {
			if (i == 0) {
				goods_price += goods_prices[i].value;
			} else {
				goods_price += ",-=" + goods_prices[i].value;
			}
		}
    	var goods_nums =$("input[name='goods_num']");
    	for (var i = 0; i < goods_nums.length; i++) {
			if (i == 0) {
				goods_num += goods_nums[i].value;
			} else {
				goods_num += ",-=" + goods_nums[i].value;
			}
		}
    	var goods_total= $('#tprice').val();
    	var goods_total_num= $('#tnum').val();
    	
    	var cps_id= $('#cps_id').val();
    	var cps_name= $('#cps_name').val();
    	var cps_price= $('#cps_price').val();
    	if(typeof(cps_name)=='undefined'){
    		cps_id= 0;
    		cps_name= '';
        	cps_price=0;
    	}
    	var receive ="";
    	var addr_user=$('#addr_user').val();
    	var addr_id=$('#addr_id').val();
    	var addr_tel=$('#addr_tel').val();
    	var addr_name=$('#addr_name').val();
    	
    	if(typeof(addr_user)=='undefined'){
    		addr_user='';
    	}
    	if(typeof(addr_tel)=='undefined'){
    		addr_tel='';
    	}
    	if(typeof(addr_name)=='undefined'){
    		addr_name='';
    	}
    	if((custId==null||custId=="")&&(addr_user==''||addr_tel==''||addr_name=='')){
    		showTip('请填写送货地址!');
    		return ;
    	}
    	
    	if(fgt_price==1){
    		receive=$('#zitidian-choose').html();
    	}
		
    	if(fgt_price==0){
    		if(addr_user==''||addr_tel==''||addr_name==''){
	    		showTip('请填写有效的收货地址');
    			return ;
			}
    	}
    	addr_name=$('#addressContent').text(); 
    	
		var note= $('#note').val();
    	submitType = 1;
    	 $('#submitOrder').attr("disabled","disabled");
    	 
    	 $('body').loading({
 			loadingWidth:240,
 			title:'订单通知信息',
 			name:'loadingSubmit',
 			discription:'正在提交订单，请稍等...',
 			direction:'column',
 			type:'origin',
 			originDivWidth:40,
 			originDivHeight:40,
 			originWidth:6,
 			originHeight:6,
 			smallLoading:false,
 			loadingMaskBg:'rgba(0,0,0,0.2)'
 	    });
    	 
    	$.ajax({
			url:'orderInsert.html',
			type:'post',
			data:'goods_id='+goods_id
			+'&goods_name='+encodeURI(goods_name)
			+'&goods_img='+goods_img
			+'&goods_price='+goods_price
			+'&goods_num='+goods_num
			+'&goods_total='+goods_total
			+'&goods_total_num='+goods_total_num
			+'&cps_id='+cps_id
			+'&cps_name='+encodeURI(cps_name)
			+'&cps_price='+cps_price
			+'&addr_name='+encodeURI(addr_name)
			+'&receive='+encodeURI(receive)+'&note='+encodeURI(note)
			+'&receiveType='+fgt_price
			+'&addr_tel='+addr_tel
			+'&addr_id='+addr_id
			+'&addr_user='+encodeURI(addr_user)
			+'&salesOne='+"${salesOne}",
			success:function(rs){
				submitType = 0;
				var re = /^[0-9]+.?[0-9]*$/;    
				if(re.test(rs)&&rs!=0){
					window.location.href='${pageContext.request.contextPath}/page/orderSucc.html?orderNum='+rs+'&returnURL=${pageContext.request.contextPath}/page/orderList.html';
				}else{
					alert("失败！");
				}
				removeLoading('loadingSubmit');
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
