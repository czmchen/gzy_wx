<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	<title>设置商户位置</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="apple-mobile-web-app-capable" content="no">
	<meta name="mobile-web-app-capable" content="no">
	<meta name="renderer" content="webkit">
	<meta name="format-detection" content="telephone=no">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <style type="text/css">
        html,body,#container{
            height:100%;
        }
        .input-card {
		  display: flex;
		  flex-direction: column;
		  min-width: 0;
		  word-wrap: break-word;
		  background-color: #fff;
		  background-clip: border-box;
		  border-radius: .25rem;
		  width: 20rem;
		  height: 15rem;
		  border-width: 0;
		  border-radius: 0.4rem;
		  /* box-shadow: 0 2px 6px 0 rgba(114, 124, 245, .5);
		  position: fixed; */
		  bottom: 1rem;
		  right: 1rem;
		  -ms-flex: 1 1 auto;
		  flex: 1 1 auto;
		 /*  padding: 0.75rem 1.25rem; */
		}
		.amap-info-close {
	    	position: fixed;
	    	font-size: 33px;
    	    margin-right: 10px;
	    }.amap-info-content {
		    padding: 0px;
    	}.amap-info-contentContainer.bottom-center, .amap-info-contentContainer.bottom-left, .amap-info-contentContainer.bottom-right {
		    margin-top: -15px;
		}
		#myPageTop {
		    position: absolute;
		    top: -8px;
		    right: 10px;
		    font-family: "Microsoft Yahei", ΢���ź�, Pinghei;
		    font-size: 14px;
		    background: none 0px 0px repeat scroll rgb(255, 255, 255);
		    border-width: 1px;
		    border-style: solid;
		    border-color: rgb(204, 204, 204);
		    border-image: initial;
		    margin: 10px auto;
		    padding: 6px;
		}
    </style>
</head>
<body>
<div id="container"></div>
<div id="myPageTop">
    <table>
        <tr>
            <td>
                <label>请输入关键字：</label>
            </td>
        </tr>
        <tr>
            <td>
                <input id="tipinput"/>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
  window._AMapSecurityConfig = {
	  /* serviceHost:'http://127.0.0.1:8000/_AMapService' */
	  securityJsCode:'c4b3ed22a89083edbd3b6d994e4814e8',
  }
</script>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=7d87e02d3cab51a672c63df409cd8bae"></script>

<script type="text/javascript">
	var map = new AMap.Map("container", {
	    resizeEnable: true,
	    zoom: 17
	});
</script>

<input type="hidden" id="latitude" value=""/>
<input type="hidden" id="longitude" value=""/>

<input type="hidden" id="cityAdcode" value=""/>

<input type="hidden" id="townCode" value=""/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/components/jquery/viewer/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/components/jquery/viewer/css/viewer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/components/jquery/viewer/css/main.css">

<script src="${pageContext.request.contextPath}/components/jquery/jquery-3.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/components/jquery/viewer/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/components/jquery/viewer/js/viewer.js"></script>
<script src="${pageContext.request.contextPath}/components/jquery/viewer/js/jquery-viewer.js"></script>

<script type="text/javascript">
	var msgNoteMaxTime = 1;
	var searchMaxTimes = 1;
	var allMarkerData = new Map();
	var infoWindow;
	
	var map = new AMap.Map("container", {
	    resizeEnable: true,
	    zoom: 17
	});
	
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
		        //markerOptions:{clickable:true}
	    });
	    map.addControl(geolocation);

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
	    }, 5000); // 每秒钟更新一次定位点位置 
	});
	
	
	 //输入提示
    var autoOptions = {
        input: "tipinput"
    };

    AMap.plugin(['AMap.AutoComplete'], function(){
        var auto = new AMap.AutoComplete(autoOptions);
        auto.setCity($('#cityAdcode').val());
        auto.setCityLimit(true);//设置是否强制限制城市
        //构造地点查询类
        auto.on("select", select);//注册监听，当选中某条记录时会触发
        function select(e) {
        	removeCenterMarker();
        	mapSearch(e.poi.name);
        }
    });
	
    function mapSearch(searchKey){
    	AMap.plugin(['AMap.PlaceSearch'], function(){
   	    	var placeSearch = new AMap.PlaceSearch({extensions:'all'});
   	 		var latitude = $('#latitude').val();
   	 		var longitude = $('#longitude').val();
	    	 
			placeSearch.setPageSize(50);
	     	var cpoint = [longitude,latitude]; //中心点坐标[113.12553,22.990779]
	     	placeSearch.searchNearBy(searchKey, cpoint, 5000, function(status, result) {debugger
		     	if(result.poiList==undefined){
		      	 	return ;
	      		}
				var pois = result.poiList.pois;
	         	for(var i = 0; i < pois.length; i++){
	              var poi = pois[i];
	              
	              if(allMarkerData.get("Find_"+poi.id)!=null){
	            	  continue;
	              }
	              
	              var photosChar = "";
	  			  var photosData = poi.photos;
	  			  for(var j = 0;j<photosData.length;j++){
	  				var itemPhoto = photosData[j];
	  				photosChar += itemPhoto.url;
	  				if(j<(photosData.length-1)){
	  					photosChar += "(|)";
	  				}
	  			  }
		  			 
	              var marker = new AMap.Marker({
	  	           // icon: '${pageContext.request.contextPath}/resource/images/map/marker/find_32.png',
	  	            title: poi.name,
	  	            position: poi.location, // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
	  	            offset: new AMap.Pixel(-13, -30),
	  	            extData: {
	                      	"custName": poi.name,
	  	      				"addressDetail": poi.address,
	     					"tel":poi.tel,
	     					"id":poi.id,
	     					"par":{
	     						adcode:poi.adcode,
	     						address:poi.address,
	     						adname:poi.adname,
	     						citycode:poi.citycode,
	     						cityname:poi.cityname,
	     						discount:poi.discount,
	     						distance:poi.distance,
	     						email:poi.email,
	     						entr_location:'',
	     						exit_location:'',
	     						groupbuy:poi.groupbuy,
	     						mapId:poi.id,
	     						indoor_map:poi.indoor_map,
	     						lng:poi.location.lng,
	     						lat:poi.location.lat,
	     						name:poi.name,
	     						pcode:poi.pcode,
	     						orgPhotos:poi.photos,
	     						photos:photosChar,
	     						pname:poi.pname,
	     						postcode:poi.postcode,
	     						rating:poi.rating,
	     						shopinfo:poi.shopinfo,
	     						tel:poi.tel,
	     						type:poi.type,
	     						status:0,
	     						website:poi.website,
	     						markAdCode:$('#townCode').val()
	     					}
	                       }
	  	        });
	  	        marker.setMap(map);
	  	        map.setFitView();
	  	        
	  	        //marker 点击时打开
	  	        marker.on('click', function(e) {
	  	        	openFindInfo(e,true);
	  	        });
	  	        
	  	      	allMarkerData.set("Find_"+poi.id, marker);
	          }
	     	});
    	 });
    }
    
  	//移除centerMarker事件
 	function removeCenterMarker() {
      if(allMarkerData.size>0){
  	    for (var [key, value] of allMarkerData){  
  		  value.setMap(null);
  		  value = null;
  	  	}
  		allMarkerData = new Map();
  	  }
  	}
	
	var searchTimes = 0;
	 //解析定位结果
	function onComplete(data) {
	   $('#latitude').val(data.position.lat);
	   $('#longitude').val(data.position.lng);
	   $('#cityAdcode').val(data.addressComponent.adcode);
	   pagTownCode(data);
	   if(searchTimes<searchMaxTimes){
		   map.setCenter(data.position);
		   map.setFitView();
		   searchTimes++;
		   initMerchants();
		   searchData();
		}
	}
	 
	function pagTownCode(data) {
		   $.ajax({//插入轨迹路线
				url:'${pageContext.request.contextPath }/components/city-pickers/town/'+data.addressComponent.adcode+'.json',
				type: 'get',
				async: false,
			   	success:function(rs){
					if(rs!=null){
						var currentTownCode = getPropertyByKeyValue(rs, data.addressComponent.township);
				    	$('#townCode').val(currentTownCode);
					}else{
						alert("系统出错，请刷新页面重试！");
					}
				}
			})
	}
	 
	function getPropertyByKeyValue(obj, value) {
  	  for (var prop in obj) {
  	    if (obj.hasOwnProperty(prop)) {
  	      if (obj[prop] === value) {
  	        return prop;
  	      } else if (typeof obj[prop] === "object") {
  	        var result = getPropertyByKeyValue(obj[prop], value);
  	        if (result) {
  	          return prop + "." + result;
  	        }
  	      }
  	    }
  	  }
  	}
		
	 
	//解析定位错误信息
	var msgNoteTime = 0;
	function onError(data) {
		if(msgNoteTime<msgNoteMaxTime){
		   msgNoteTime++;
	 	   alert("定位超时，请打开定位服务！");
		}
	}
	
	function searchData(){
	   	var latitude = $('#latitude').val();
	   	var longitude = $('#longitude').val();
	   	//var searchKey = '${address}';
	   	var searchKey = '${khmc}';
	   	if(latitude==null||latitude==''){
	   		return ;
	   	}debugger
	   	mapSearch(searchKey);
	}
	
	function initMerchants(){
		var distributionMerchantsJson = '${distributionMerchants}';
		if(distributionMerchantsJson!='null'&&distributionMerchantsJson!=null&&distributionMerchantsJson!=undefined&&distributionMerchantsJson!='undefined'&&distributionMerchantsJson!=''){
			var distributionMerchants = eval('('+distributionMerchantsJson+')');
			if(distributionMerchants.mapId!='null'&&distributionMerchants.mapId!=undefined&&distributionMerchants.mapId!='undefined'&&distributionMerchants.mapId!=''&&distributionMerchants.mapId!=null){
			   var storeImgDataArray = [];
		   	   if(distributionMerchants.photos!='null'&&distributionMerchants.photos!=null&&distributionMerchants.photos!=''&&distributionMerchants.photos!=undefined){
		   	 	 var storeImgData = distributionMerchants.photos.split("(|)");
		   		 for(var storeImgIndex in storeImgData){
		    	   var storeImgItem = storeImgData[storeImgIndex];
		    	   var urlData = {url : storeImgItem};
		    	   storeImgDataArray.push(urlData);
			  	 }
		   	   }
			  	
				var marker = new AMap.Marker({
		  	            icon: '${pageContext.request.contextPath}/page/distribution/images/mark_32.png',
		  	            title: distributionMerchants.name,
		  	            position: [distributionMerchants.lng,distributionMerchants.lat], // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
		  	            offset: new AMap.Pixel(-13, -30),
		  	            extData: {
		                      	"custName": distributionMerchants.name,
		  	      				"addressDetail": distributionMerchants.address,
		     					"tel":distributionMerchants.tel,
		     					"id":distributionMerchants.id,
		     					"par":{
		     						adcode:distributionMerchants.adcode,
		     						address:distributionMerchants.address,
		     						adname:distributionMerchants.adname,
		     						citycode:distributionMerchants.citycode,
		     						cityname:distributionMerchants.cityname,
		     						discount:distributionMerchants.discount,
		     						distance:distributionMerchants.distance,
		     						email:distributionMerchants.email,
		     						entr_location:'',
		     						exit_location:'',
		     						groupbuy:distributionMerchants.groupbuy,
		     						mapId:distributionMerchants.mapId,
		     						indoor_map:distributionMerchants.indoor_map,
		     						lng:distributionMerchants.lng,
		     						lat:distributionMerchants.lat,
		     						name:distributionMerchants.name,
		     						pcode:distributionMerchants.pcode,
		     						orgPhotos:storeImgDataArray,
		     						photos:distributionMerchants.photos,
		     						pname:distributionMerchants.pname,
		     						postcode:distributionMerchants.postcode,
		     						rating:distributionMerchants.rating,
		     						shopinfo:distributionMerchants.shopinfo,
		     						tel:distributionMerchants.tel,
		     						type:distributionMerchants.type,
		     						status:0,
		     						website:distributionMerchants.website,
		     						markAdCode:$('#townCode').val()
		     					}
		                       }
		  	        });
	  	        marker.setMap(map);
	  	        map.setFitView();
	  	        
	  	        //marker 点击时打开
	  	        marker.on('click', function(e) {
	  	        	openFindInfo(e,false);
	  	        });
	  	        
	  	      	allMarkerData.set("Find_"+distributionMerchants.mapId, marker);
			}
		}
	}
	
	function sleep(ms) {
	    for (var t = Date.now(); Date.now() - t < ms;){}
	}
    
  //在指定位置打开信息窗体
    function openFindInfo(e,showMarkFlag) {
    	var htmlContent = "";
   	  	if(e.target.getExtData().par.orgPhotos!=null&&e.target.getExtData().par.orgPhotos!=''&&e.target.getExtData().par.orgPhotos!=undefined){
	   		 var storeImgData = e.target.getExtData().par.orgPhotos;
	   		 <!-- Content -->
	   		  htmlContent = "<div class='container'>"
	   		             + "<div class='row'>"    
	   		             + "<div class='col-sm-8 col-md-6' style='padding-left: 0px;'>"       
	   		             + "<div class='docs-galley mb-3'>"
	   		             + "<ul class='docs-pictures clearfix' style='list-style: none;padding: 0;'>"; 
	   		  for(var storeImgIndex in storeImgData){
	   	    	  var storeImgItem = storeImgData[storeImgIndex];
	   	    	  var imgSrc = storeImgItem.url;
	   	    	  //storeImg += "<img width='200' height='200' alt='点击查看原图' onclick='alert(\042"+imgSrc+"\042)' src='"+imgSrc+"'>&nbsp;&nbsp;&nbsp;";
	   	    	  htmlContent += " <li><img width='70%' height='70%' data-original='"+imgSrc+"' src='"+imgSrc+"' alt='点击查看原图' ></li>";
	   		  }
	   		  htmlContent += "</ul>"
				              + "</div>"
				              + "</div>"     
				              + "</div>"
				              + "</div>";
	   	}
    	var contentVal = "<div class='input-card'>"
    				  + "<div style='text-align: left;display: flex;'><div style='width: 77px;'>名称: </div><div>"+e.target.getExtData().custName+"</div></div>"
    				  + "<div style='text-align: left;display: flex;'><div style='width: 55px;'>地址: </div><div>"+e.target.getExtData().addressDetail+"</div></div>"
    				  + "<div style='text-align: left;display: flex;'><div style='width: 42px;'>电话: </div><div>"+e.target.getExtData().tel+"</div></div>"
    				  + "</br><div style='text-align: right;font-size: 10px;'>";
    				  if(showMarkFlag){
    					  contentVal += "<span><a href='javascript:void(0);' style='margin-right: 10px;' onclick='settingMerchantLocation(&apos;"+e.target.getExtData().id+"&apos;);'>设置<img alt='mark' src='${pageContext.request.contextPath}/page/distribution/images/mark_32.png'></a></span><span width='10px'></span>";
    				  }
    	contentVal += "</div></br>"
    		 		+ htmlContent
    				+ "<div>";
        //构建信息窗体中显示的内容
        infoWindow = new AMap.InfoWindow({
            content: contentVal  //使用默认信息窗体框样式，显示信息内容
        });
        infoWindow.open(map, e.target.getPosition());
        $('.docs-pictures').viewer();
    }
  
  
    function settingMerchantLocation(mapIdVal){
    	window.parent.$.confirm({
			  title: '位置选取服务',
			  text: '是否确认选取当前位置为精准送货地址',
			  onOK: function () {
				  var orgMarker = allMarkerData.get("Find_"+mapIdVal);
				  var dataRowId = "${distributionId}";
					orgMarker.getExtData().par.custOpenId = "${custOpenId}";
					orgMarker.getExtData().par.custAddress = "${address}";
					orgMarker.getExtData().par.custId = "${custId}";
					orgMarker.getExtData().par.distributionId = dataRowId;
					$.ajax({
						url:'${pageContext.request.contextPath}/distribution/distributionMerchants.html',
					    type: 'POST',
					    data: orgMarker.getExtData().par,
						success:function(rs){
							infoWindow.close();
							if(rs=='1'){
								$('#nowLocationImgId_'+dataRowId,parent.document).attr("src","${pageContext.request.contextPath}/page/distribution/images/nowLocation.png");
								$('#gaodeNavigation_'+dataRowId,parent.document).attr("latitudeContent",orgMarker.getExtData().par.lat);
								$('#gaodeNavigation_'+dataRowId,parent.document).attr("longitudeContent",orgMarker.getExtData().par.lng);
								$("#floatDiv",parent.document).hide();
								window.parent.$.closePopup();
								window.parent.$.alert("设置成功！");
							}else{
								window.parent.$.alert("设置失败，请重试！");
							}
						}
					})
			  },
			  onCancel: function () {
			  }
			});
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

</body>
</html>