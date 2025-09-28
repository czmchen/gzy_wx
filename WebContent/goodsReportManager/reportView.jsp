<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>质检报告</title>
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

<link rel="stylesheet" href="${pageContext.request.contextPath }/components/jquery-weui/css/weui.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/components/jquery-weui/css/jquery-weui.min.css">

<style type="text/css">
</style>
</head>
<body>
	<div class="weui-cells weui-cells_form">
	    <div class="weui-cell">
	        <div class="weui-cell__hd"><label class="weui-label">批号</label></div>
	        <div class="weui-cell__bd">
	            <input class="weui-input" pattern="[0-9]*" placeholder="请输入批号" id="lotNum" type="number">
	        </div>
	    </div>
	</div>
	
	<div class="weui-btn-area">
	    <a class="weui-btn weui-btn_primary" href="javascript:" id="btn" onclick="lstData()">查询</a>
	</div>

	<div style="display: none;" id="searchResultViewId">
		<div class="weui-cells__title" style="margin-top: 30px;">查询结果如下:</div>
		<div class="weui-cells" id="searchResultId">
			<a class="weui-cell weui-cell_access" href="javascript:;">
				<div class="weui-cell__bd">
					<p>cell standard</p>
				</div>
				<div class="weui-cell__ft"></div>
			</a> 
			<a class="weui-cell weui-cell_access" href="javascript:;">
				<div class="weui-cell__bd">
					<p>cell standard</p>
				</div>
				<div class="weui-cell__ft"></div>
			</a>
		</div>
	</div>
	
	
	<div id="pdfViewPopupId" class="weui-popup__container">
	  <div class="weui-popup__overlay"></div>
	  <div class="weui-popup__modal" id="viewPdfContent">
	    你的内容放在这里...
	  </div>
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-weui/js/jquery-weui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-weui/js/swiper.min.js"></script>
	
<script type="text/javascript">
	var autoComplateData = [];
	function initAutoCompleteData(){
		$.ajax({
			dataType:"json",
			type:"POST",
			async:false,
			url:"${pageContext.request.contextPath}/goodsReport/listGoods.html",
			success:function(data){
				//如果数据有长度,就交给typeaheader显示列表
				if(data!=null && data.length){
					var jsonData = [];
					var maxLength = 0;
					
					for(var i = 0; i < data.length; i++){
						var item = data[i];
						var name = item.goods_spe+"甘竹压榨一级花生油";
						autoComplateData.push({"title":name+"   "+item.nbox+"罐/件","value":item.goods_code});
				    }
				}
			}
		});
	}
	
	initAutoCompleteData();
	
	var resultData;
	function lstData(){
		var lotNumVal = $("#lotNum").val();
		var goodsCodeVal = $("#goodsCode").attr("data-values");
		if(goodsCodeVal==null||goodsCodeVal=='undefined'||goodsCodeVal==''){
			goodsCodeVal = '';
		}
		if((lotNumVal==null||lotNumVal=='undefined'||lotNumVal=='')&&(goodsCodeVal==null||goodsCodeVal=='undefined'||goodsCodeVal=='')){
			$.alert("批号或者物料类型不能为空!");
			return ;
		}
		$.ajax({
			url : '${pageContext.request.contextPath}/goodsReport/list.html',
			data : 'lotNum=' + lotNumVal + '&goodsCode='+ goodsCodeVal,
			type : "POST",
			dataType: "json",
			async : false,
			success : function(data) {
				if (data != null && data != "null") {
					resultData = data;
					$("#searchResultViewId").css("display","");
					var htmlData = "";
					for(var i = 0; i < data.length; i++){
						var item = data[i];
						var tempView = '<a class="weui-cell weui-cell_access" id="openPdfView_'+item.id+'" href="javascript:;" onclick="viewPdf('+item.id+')">'+
											'<div class="weui-cell__bd">'+
												'<p>'+item.inspReport+'</p>'+
											'</div>'+
											'<div class="weui-cell__ft"></div>'+
										'</a>';
						htmlData += tempView;
				    }
					$("#searchResultId").html(htmlData);
				}
			}
		});
	}
	
	function viewPdf(id){
		for(var i = 0; i < resultData.length; i++){
			var itar = resultData[i];
			if(id==itar.id){
				if(itar.pdf2jpgData!=null&&itar.pdf2jpgData!=''){
					var obj = eval('(' + itar.pdf2jpgData + ')');
					var htmlContent = "";
					for(var item =0;item<obj.length;item++){
						htmlContent += "<div><img alt='' src='${pageContext.request.contextPath}/goodsReport/getPreviewData.html?fileDeskName="+encodeURIComponent(obj[item])+"' style='width:"+screenWidth+"px;'></div>";
					}
					htmlContent += '<div style="height:20px;"></div>';
					htmlContent += '<div><a href="${pageContext.request.contextPath}/goodsReport/download.html?id='+id+'" target=_blank class="weui-btn weui-btn_primary">下载</a></div>';
					htmlContent += '<div style="height:10px;"></div>';
					htmlContent += '<div><a href="javascript:;" class="weui-btn weui-btn_default close-popup">关闭</a></div>';
					htmlContent += '<div style="height:20px;"></div>';
					$("#viewPdfContent").html(htmlContent);
					$("#pdfViewPopupId").popup();
				}
				break;
			}
		}
	}
	
	function downloadPdf(id){
		location.href="${pageContext.request.contextPath}/goodsReport/download.html?id="+id;
	}

	var screenWidth = $(window).width();
	$("#goodsCode").select({
		title : "选择物料",
		items : autoComplateData
	});

	$("#goodsCode").click(function() {
		$(".weui-cells_radio").find("label").css("font-size", "16px");
	});
</script>
</body>
</html>