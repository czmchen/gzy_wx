<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="top.jsp"/>
<!DOCTYPE HTML>
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
<link type="text/css" href="${pageContext.request.contextPath }/components/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/goodsReportManager/js/raydreams.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/bootstrap/js/bootstrap3-typeahead.min.js"></script>
</head>
<body>
	<div style="margin: auto; width: 80%; padding: 2em 0;margin-top: 10px;">
		<div style="">
			<form action="" id="searchForm">
				<div class="form-inline">
				    <div class="input-group">
				      <div class="input-group-addon">质检报告编号</div>
				      <input type="text" class="form-control" style="padding:0px 0px;" name="inspReport" placeholder="">
				    </div>
				     <div class="input-group">
				      <div class="input-group-addon">分解批号</div>
				      <input type="text" class="form-control" style="padding:0px 0px;" name="lotNum" placeholder="">
				    </div>
				</div>
			</form>
			
			<form action="" id="newForm">
				<div style="margin-top: 10px;float:right">
					<input type="button" onclick="lstData();" value="查询" class="btn btn-primary" />
					<input type="button" value="新增" class="btn btn-info" data-toggle="modal" data-target="#myModal" data-target=".bs-example-modal-lg" data-backdrop="static"  />
					
					<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="myModal" aria-labelledby="myLargeModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
				        <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				          <h4 class="modal-title" id="myLargeModalLabel">质检报告新增</h4>
				        </div>
				        <div class="modal-body">
					        <div>
							    <div class="input-group">
							      	<div class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文件上传</div>
							      	<div style="border: 1px solid #ccc;border-radius: 4px;">
								      	<input type="hidden" id="uploadToken" value="">
								      	<input type="file" name="file" id="uploadFileId" class="form-control" multiple="multiple" onchange="uploadFiles();" accept=".pdf" />
								      	<div id="uploadFileListId"></div>
								      	<div style="" id="loadingImg"></div>
								      	<div style="height: 10px;"></div>
								      	<div id="uploadMsgId"></div>
							      	</div>
						        </div>
							</div>
							<div style="border: 1px;">
							<div style="text-align: right;">
								<div style="height: 10px;"></div>
								<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
								<input type="button" onclick="resetForm()" value="重置" class="btn btn-default" />
								<input type="button" onclick="submitForm()" value="提交" class="btn btn-primary" />
							</div>
				        </div>
				      </div><!-- /.modal-content -->
				    </div><!-- /.modal-dialog -->
				  </div>
					
				</div>
			</div>
		</form>
		<div id="dataTable" style="margin-top: 50px;" class="table table-striped" cellspacing="0" width="80%"></div>
		<div id="showAlertDiv"></div>
		</div>
		<img alt="" src="">
		</div>
		
		<input type="hidden" value="预览" id="previewPdf" class="btn btn-info" data-toggle="modal" data-target="#reportModal" data-target=".bs-example-modal-lg"  />
		<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="reportModal" aria-labelledby="myLargeModalLabel" >
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		          <h4 class="modal-title" id="myLargeModalLabel">质检报告预览</h4>
		        </div>
		        <div class="modal-body">
			        <div id="imgContent">
					</div>
					<div style="border: 1px;">
					<div style="text-align: right;">
						<div style="height: 10px;"></div>
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<input type="button" onclick="downloadPdf();" value="下载" class="btn btn-primary" />
					</div>
		        </div>
		      </div><!-- /.modal-content -->
		    </div><!-- /.modal-dialog -->
		  </div>
		</div>
		
		<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="fileNameIsExistModal" data-backdrop="static">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h4 class="modal-title">质检报告编号已存在</h4>
		      </div>
		      <div class="modal-body">
		        <div class="panel panel-default">
				  <!-- Default panel contents -->
				  <div class="panel-heading" style="color: red;"><b>如下质检报告编号已存在，是否替换!</b></div>
					  <table class="table" id="fileExistModalContent">
					  </table>
				</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" onclick="fileIsExistModalClose()">关闭</button>
		        <button type="button" class="btn btn-primary" onclick="fileIsExistModalOverrive()">替换</button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->

	<script type="text/javascript">
		$("#uploadToken").val(uuid());
		var timeInterVal;
		var timeInterCheckFileVal;
		var uploadData = new Map();
		var uploadTimes = 1;
		
		function dispalyMyModal(){
			$('#myModal').modal('show');
		}
		
		
		function resetForm(){
			uploadTimes = 1;
			var newForm = $("#newForm");
			newForm.find("[name='goodsCode']").val('');
			newForm.find("[name='barNum']").val('');
			newForm.find("[name='lotNum']").val('');
			newForm.find("[name='batchNum']").val('');
			newForm.find("[name='inspReport']").val('');
			$("#uploadFileListId").html('');
			$("#uploadMsgId").html('');
		}
		
		function validateForm(){
			var successFalg = true;
			var uploadFileListIdContent = $("#uploadFileListId").html();
			/* var newForm = $("#newForm");
			var goodsCodeEle = newForm.find("[name='goodsCode']");
			var barNumEle = newForm.find("[name='barNum']");
			var lotNumEle = newForm.find("[name='lotNum']");
			var batchNumEle = newForm.find("[name='batchNum']");
			var inspReportEle = newForm.find("[name='inspReport']");
			
			$("#newForm").find("[id^='popover']").remove();
			if(goodsCodeEle.val()==null||goodsCodeEle.val()==''){
				goodsCodeEle.attr("data-content","不能为空");
				goodsCodeEle.popover('show');
				successFalg = false;
			}if(barNumEle.val()==null||barNumEle.val()==''){
				barNumEle.attr("data-content","不能为空");
				barNumEle.popover('show');
				successFalg = false;
			}if(lotNumEle.val()==null||lotNumEle.val()==''){
				lotNumEle.attr("data-content","不能为空");
				lotNumEle.popover('show');
				successFalg = false;
			}if(batchNumEle.val()==null||batchNumEle.val()==''){
				batchNumEle.attr("data-content","不能为空");
				batchNumEle.popover('show');
				successFalg = false;
			}if(inspReportEle.val()==null||inspReportEle.val()==''){
				inspReportEle.attr("data-content","不能为空");
				inspReportEle.popover('show');
				successFalg = false;
			} */
			if(uploadFileListIdContent==null||uploadFileListIdContent==''){
				$("#uploadFileListId").attr("data-content","不能为空");
				$("#uploadFileListId").popover('show');
				successFalg = false;
			}
			/* if(goodsCodeEle.val()!=null&&goodsCodeEle.val()!=''){
				var isExistsGoodsCode = false;
				for(var i =0;i<autoComplateData.length;i++){
					if(goodsCodeEle.val()==autoComplateData[i].id){
						isExistsGoodsCode = true;
						break;
					}
				}
				if(isExistsGoodsCode==false){
					goodsCodeEle.attr("data-content","物料编码错误，请核对！");
					goodsCodeEle.popover('show');
					successFalg = false;
				}
			} */
			
			var fileOrgNameArray =[];
	        $("input[name='fileOrgName']").each(function(){
	        	fileOrgNameArray.push($(this).val());
	        })
			if(isRepeat(fileOrgNameArray)){
				$("#uploadFileListId").attr("data-content","存在重复的质检报告文件，请删除重复的质检报告再上传");
				$("#uploadFileListId").popover('show');
				successFalg = false;
			}
			
			if(successFalg==false){
				$("#newForm").find("[id^='popover']").css("width","150px");
				$('#newForm').find("[id^='popover']:eq(5)").css("margin-top","20px");
			}
			return successFalg;
		}
		
		function isRepeat(arr){     
		    var obj = {}; 
		    for(var i in arr) { 
		        //存在重复值
		        if(obj[arr[i]])  return true; 
		        obj[arr[i]] = true; 
		    } 
		    //不重复
		    return false; 
		} 
		
		function deleteById(id){
			$.ajax({
				url : '${pageContext.request.contextPath}/goodsReport/deleteById.html',
				data: 'id='+id,
				type : "POST",
				success:function(data) {
					if(data==1){
						showAlert("success","删除成功","","#showAlertDiv");
						lstData();
					}else{
		 				showAlert("danger","删除失败","","#showAlertDiv");
		 			}
				}
			});
 		}
		
		function submitForm(){
			if(validateForm()==false){
				return ;
			}
			$.ajax({
				url : '${pageContext.request.contextPath}/goodsReport/submitForm.html',
				data : $('#newForm').serialize(),
				type : "POST",
				dataType: "json",
				async:false,
				success:function(data) {
					if(data==1){
						showAlert("success","提交成功","","#showAlertDiv");
						lstData();
						$('#myModal').modal('hide');
						resetForm();
					}else{
		 				showAlert("danger","提交失败","","#showAlertDiv");
		 			}
				}
			});
 		}
		
		//四个alert .alert-success、.alert-info、.alert-warning、.alert-danger
		//alertClass传success/info/warning/danger
		//title一般为 成功、提示、警告、失败
		//appender默认为html
		//delay默认2000，2秒
		function showAlert(alertClass, title, msg, appender, delay) {
			//先将其他alert删除
			$("div").remove(".alert");
			var msg = msg || "";
			var alertTmpl = "<div class='alert alert-"+alertClass+" alertDiv ' >"
					+ "<a href='#' class='close' data-dismiss='alert'>×</a>"
					+ "<strong>" + title + "！</strong>" + msg + "</div>";
			$(appender || "html").append(alertTmpl);
			$(".alert").delay(delay || 2000).fadeOut("slow");
		}
		
		// 读取文件上传进度并通过进度条展示
        function getProcess() {
            $.ajax({
                type: "get",
                url: "${pageContext.request.contextPath }/uploadFile?uploadToken="+$("#uploadToken").val(),
                success: function(data) {
                	if(data!=null&&data!="null"){
	                	var obj = eval('(' + data + ')');
	                	for(var i = 0;i<obj.lstUploadDetailVo.length;i++){
	                		var objUploadDetailVo = obj.lstUploadDetailVo[i];
	                		if(objUploadDetailVo.succFlag){
		                		var divId = objUploadDetailVo.filedeskName.substring(0,objUploadDetailVo.filedeskName.indexOf("."));
	                			if(!uploadData.get(divId)){
			                		$("#uploadFileListId").append("<div id='"+divId+"'><a href='${pageContext.request.contextPath }/goodsReport/downloadFileByDeskFullName.html?fileName="+objUploadDetailVo.filedeskName+"&fileOrgName="+encodeURI(objUploadDetailVo.fileOrgName)+"'>"+objUploadDetailVo.fileOrgName+"</a><input type='hidden' name='fileOrgName' value='"+objUploadDetailVo.fileOrgName+"'><input type='hidden' id='filedeskName' name='filedeskName' value='"+objUploadDetailVo.filedeskName+"'><input type='hidden' name='pdf2jpgDataArray' value='"+objUploadDetailVo.pdf2jpgData+"'>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' class='glyphicon glyphicon-remove' onclick=\042removeUploadDiv('"+divId+"')\042></a><div>");
	                			}
		                		uploadData.set(divId,true);
	                		}
	                	}
	                	if(obj.uploadFinish==true){
	                		clearInterval(timeInterVal);//停止
	                		$("#uploadToken").val(uuid());
	                		$("#loadingImg").html('');
	                		$("#uploadFileId").show();
	                		var uploadSuccAmount = 0;
	                		var uploadFalAmount = 0;
	                		var errorMsgContent = "";
	                		for(var i = 0;i<obj.lstUploadDetailVo.length;i++){
		                		var objUploadDetailVo = obj.lstUploadDetailVo[i];
		                		if(objUploadDetailVo.succFlag){
		                			uploadSuccAmount++;
		                		}else{
		                			uploadFalAmount++;
		                			errorMsgContent = "附件名称:" +objUploadDetailVo.fileOrgName +",错误信息:"+objUploadDetailVo.errorMsg+"<br>";
		                		}
	                		}
	                		$("#uploadMsgId").append("第"+uploadTimes+"批次上传，成功"+uploadSuccAmount+"个，失败"+uploadFalAmount+"个;<br>"+errorMsgContent);
	                		uploadTimes++;
	                	}
                	}
                }
            });
        }
		
		function removeUploadDiv(id){
			$("#"+id).remove();
		}
		
		function previewPdf(pdf2jpgData){
			var obj = eval('(' + pdf2jpgData + ')');
			var htmlContent = "";
			for (var item = 0; item < obj.length; item++) {
				htmlContent += "<div><img alt='' src='${pageContext.request.contextPath}/goodsReport/getPreviewData.html?fileDeskName="
						+ encodeURIComponent(obj[item])
						+ "' style='width:884px;'><div>";
			}
			downloadTempId = clickId;
			$("#imgContent").html(htmlContent);
		}
		
		function checkFileExist(fileNames){
			$.ajax({
				url : "${pageContext.request.contextPath }/goodsReport/checkExistFile.html?inspReportStr="+fileNames,
				async: false,
				success:function(data) {
					if(data!=null&&data!="null"&&data !="[]"){
						console.log(data);
						var fileExistContent = "<tr>";
						fileExistContent += "<th>质检报告编号</th>";
						fileExistContent += "<th>商品名称</th>";
						fileExistContent += "<th>物料编码</th>";
						fileExistContent += "<th>物料类型</th>";
						fileExistContent += "<th>操作时间</th>";
						fileExistContent += "</tr>";
						
						var obj = eval('(' + data + ')');
						for(var i = 0;i<obj.length;i++){
							fileExistContent += "<tr onclick='previewPdf('"+obj[i].pdf2jpgData+"')'>";
							fileExistContent += "<td>"+obj[i].inspReport+"</td>";
							fileExistContent += "<td>"+obj[i].goodsName+"</td>";
							fileExistContent += "<td>"+obj[i].goodsCode+"</td>";
							fileExistContent += "<td>"+obj[i].goodsSpe+"</td>";
							fileExistContent += "<td>"+obj[i].modifyTime+"</td>";
							fileExistContent += " </tr>";
	                	}
						$('#fileExistModalContent').html(fileExistContent);
						$('#fileNameIsExistModal').modal('show');
						checkFileExistName = false;
					}else{
						execUploadFile();
					}
				}
			});
		}
		
		function fileIsExistModalClose(){
			$('#fileNameIsExistModal').modal('hide');
			$("#uploadFileId").val('');
		}
		
		function fileIsExistModalOverrive(){
			$('#fileNameIsExistModal').modal('hide');
			execUploadFile();
		}

		function uploadFiles() {
			var files = $("#uploadFileId");
			var canRunOver = true;
			var uploadFileNames = "";
			for (var i = 0; i < files[0].files.length; i++) {
				var fileName = files[0].files[i].name;
				var fileFullName = fileName.substring(0, fileName.indexOf("."));
				var splitFile = fileFullName.split("-");
				console.log("fileName:" + fileName + "||fileFullName:"
						+ fileFullName + "||splitFile" + splitFile.length);
				if (splitFile.length != 2) {
					canRunOver = false;
					break;
				}if (splitFile[1].length<3) {
					canRunOver = false;
					break;
				}
				uploadFileNames += fileName;
				if (i < (files[0].files.length - 1)) {
					uploadFileNames += ",";
				}
			}
			if (canRunOver == false) {
				alert("文件格式不正确，请按照正确的格式命名，正确命名规则为:分解批号-分解批次分解条码.pdf(例：20200918-07A.pdf)");
				return;
			}
			checkFileExist(uploadFileNames);
		}
		
		function execUploadFile(){
			var files = $("#uploadFileId");
			var imgHtml = "<img src='${pageContext.request.contextPath }/goodsReportManager/img/loading.gif' alt=''>";
			$("#loadingImg").html(imgHtml);
			$("#uploadFileId").hide();
			//$("#uploadForm").submit();

			timeInterVal = setInterval(function() {
				getProcess()
			}, 1000);
			var formData = new FormData();
			for (var i = 0; i < files[0].files.length; i++) {
				formData.append("file", files[0].files[i]);
			}
			formData.append("uploadToken", $("#uploadToken").val());
			$.ajax({
				url : "${pageContext.request.contextPath }/uploadFile",
				type : "POST",
				data : formData,
				contentType : false,
				processData : false,
				error : function() {
					alert("上传失败！");
				}
			});
			$("#uploadFileId").val('');
		}

		function uuid() {
			var s = [];
			var hexDigits = "0123456789abcdef";
			for (var i = 0; i < 36; i++) {
				s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
			}
			s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
			s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
			s[8] = s[13] = s[18] = s[23] = "-";
			var uuid = s.join("");
			return uuid;
		}
		var dataTable = null;

		jQuery(document).ready(function() {
			dataTable = jQuery("#dataTable").raytable({
				datasource : {
					data : [],
					keyfield : 'id'
				},
				columns : [ {
					field : "inspReport",
					title : "质检报告编号",
					sort : true
				}, {
					field : "lotNum",
					title : "分解批号",
					sort : true
				}, {
					field : "modifyTime",
					title : "操作时间",
					sort : true
				}, {
					title : "操作",
					icons : [ {
						glyph : "glyphicon-trash",
						handler : iconAction,
						data : "id"
					} ]
				} ],
				pagesize : 10,
				maxPageButtons : 5,
				rowNumbers : true,
				rowClickHandler : rowAction
			});
			jQuery(".glyphicon").css('cursor', 'pointer');
			columnAutoWidth();
			lstData();
		});

		var lstAllData;
		function lstData() {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/goodsReport/list.html',
						data : $('#searchForm').serialize(),
						type : "POST",
						dataType : "json",
						async : false,
						success : function(data) {
							if (data != null && data != "null") {
								lstAllData = data;
								dataTable.data(data, 'id');
							}
						}
					});
		}

		function columnAutoWidth() {
			$('#dataTable').find("th:eq(8)").css("width", "160px");
		}

		function doLoad(sender) {
			dataTable.data(myData, 'id');
		}

		function iconAction(event) {
			var data = jQuery(event.target).data('ray-data');
			deleteById(data);
		}

		function downloadPdf() {
			location.href = "${pageContext.request.contextPath}/goodsReport/download.html?id="
					+ downloadTempId;
		}
		var downloadTempId;
		function rowAction(event) {
			var clickId = event.data.id;
			for (var i = 0; i < lstAllData.length; i++) {
				var id = lstAllData[i].id;
				if (clickId == id) {
					var obj = eval('(' + lstAllData[i].pdf2jpgData + ')');
					var htmlContent = "";
					for (var item = 0; item < obj.length; item++) {
						htmlContent += "<div><img alt='' src='${pageContext.request.contextPath}/goodsReport/getPreviewData.html?fileDeskName="
								+ encodeURIComponent(obj[item])
								+ "' style='width:884px;'><div>";
					}
					downloadTempId = clickId;
					$("#imgContent").html(htmlContent);
				}
			}

			$("#previewPdf").trigger("click");
			//location.href="${pageContext.request.contextPath}/goodsReport/download.html?id="+event.data.id;
		}
		function isManager(item) {
			return (item.grade > 4);
		}
		function parseDate(item) {
			var d = new Date(item.birthDate);
			return d.toDateString();
		}

		var autoComplateData = [];
		function initAutoCompleteData() {
			$
					.ajax({
						dataType : "json",
						type : "POST",
						url : "${pageContext.request.contextPath}/goodsReport/listGoods.html",
						success : function(data) {
							//如果数据有长度,就交给typeaheader显示列表
							if (data != null && data.length) {
								var jsonData = [];
								for (var i = 0; i < data.length; i++) {
									var item = data[i];
									autoComplateData.push({
										"id" : item.goods_code,
										"content" : item.goods_code + "   "
												+ item.goods_name
									});
								}
							}
						}
					});
		}
		initAutoCompleteData();
		//自动补全
		$("#autocomplate").typeahead({
			//配置minLength
			items : 10,//最多显示的下拉列表内容
			//1、先配置数据源(可以先不配置数据源，先配置其他东西)
			source : function(query, process) {//第一个为正在查询的值，第二个参数为函数(该函数)
				//使用Ajax加载数据源
				//process为获得数据之后用来调用的方法(方法之后,下拉列表的内容就可以呈现了)
				process(autoComplateData);
			},
			displayText : function(item) {
				return item.content;
			},
			updater : function(item) { //后续处理(在选中数据后的操作，这里的返回值代表了输入框的值)
				return item.id;
			}
		});

		function openAutoComplate() {
			$("#autocomplate").typeahead('open');
		}
	</script>
</body>
</html>