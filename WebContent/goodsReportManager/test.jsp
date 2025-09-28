<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<script type="text/javascript" src="${pageContext.request.contextPath }/components/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div style="margin: auto; width: 80%; padding: 2em 0;margin-top: 10px;">
		<div style="">
			<div class="form-inline">
			    <div class="input-group">
			      <div class="input-group-addon">质检报告编号</div>
			      <input type="text" class="form-control" style="padding:0px 0px;" id="inspReport" placeholder="">
			    </div>
			     <div class="input-group">
			      <div class="input-group-addon">分解批号</div>
			      <input type="text" class="form-control" style="padding:0px 0px;" id="lotNum" placeholder="">
			    </div>
			    <div class="input-group">
			      <div class="input-group-addon">分解条码后三位</div>
			      <input type="text" class="form-control" style="padding:0px 0px;" id="barNum" placeholder="">
			    </div>
			    <div class="input-group">
			      <div class="input-group-addon">物料编码</div>
			      <input type="text" class="form-control" style="padding:0px 0px;" id="goodsCode" placeholder="">
			    </div>
			</div>
		
			<div style="margin-top: 10px;float:right">
				<input type="button" onclick="doLoad(this)" value="查询" class="btn btn-primary" />
				<input type="button" value="新增" class="btn btn-info" data-toggle="modal" data-target=".bs-example-modal-lg" />
				
				<button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">Large modal</button>
				
				<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
				  <div class="modal-dialog modal-lg" role="document">
				    <div class="modal-content">
				    	
						<div >
						    <div class="input-group">
						      <div class="input-group-addon">质检报告编号</div>
						      <input type="text" class="form-control" id="newInspReport" placeholder="">
						    </div>
						     <div class="input-group">
						      <div class="input-group-addon">分解批号</div>
						      <input type="text" class="form-control" id="newILotNum" placeholder="">
						    </div>
						    <div class="input-group">
						      <div class="input-group-addon">分解条码后三位</div>
						      <input type="text" class="form-control" id="newBarNum" placeholder="">
						    </div>
						    <div class="input-group">
						      <div class="input-group-addon">物料编码</div>
						      <input type="text" class="form-control" id="newGoodsCode" placeholder="">
						    </div>
						</div>
							    
				    </div>
				  </div>
				</div>
				
			</div>
		</div>
		<div id="dataTable" style="margin-top: 50px;" class="table table-striped" cellspacing="0" width="80%"></div>
		<div>&copy;2016-2018 by Tag Guillory</div>
	</div>

	<script type="text/javascript">
	
		$('#myModal').on('shown.bs.modal', function () {
		  $('#myInput').focus()
		})
	
		var dataTable = null;
		
		var myData = [{
			id:"15",
			barNum:"080",
			lotNum:"20200806",
			batchNum:"02",
			inspReport:"20200806-02-080.pdf",
			modifyTime:"2020-09-22 15:29:34",
			goodsName:"500ML甘竹压榨一级花生油",
			goodsSpe:"500ML",
			goodsCode:"1117"
		},{
			id:"16",
			barNum:"090",
			lotNum:"20200806",
			batchNum:"02",
			inspReport:"20200806-02-080.pdf",
			modifyTime:"2020-09-22 15:29:34",
			goodsName:"500ML甘竹压榨一级花生油",
			goodsSpe:"500ML",
			goodsCode:"1117"
		}];
		
		
		jQuery(document).ready(function() {
			dataTable = jQuery("#dataTable").raytable({
				datasource : {
					data : [],
					keyfield : 'id'
				},columns: [ {
					field : "inspReport",
					title : "质检报告编号",
					sort : true
				}, {
					field : "lotNum",
					title : "分解批号",
					sort : true
				}, {
					field : "batchNum",
					title : "分解批次"
				}, {
					field : "barNum",
					title : "分解条码后三位",
					sort : true
				}, {
					field : "goodsName",
					title : "商品名称",
					sort : true
				}, {
					field : "goodsCode",
					title : "物料编码",
					sort : true
				}, {
					field : "goodsSpe",
					title : "物料类型",
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
					}]
				} ],
				pagesize : 13,
				maxPageButtons : 5,
				rowNumbers : true,
				rowClickHandler : rowAction
			});
			jQuery(".glyphicon").css('cursor', 'pointer');
		});
		function doLoad(sender) {
			dataTable.data(myData, 'id');
		}
		function iconAction(event) {
			var data = jQuery(event.target).data('ray-data');
			alert('glyph icon data is ' + data);
		}
		function rowAction(event) {
			alert('You clicked row ' + event.data.rowIdx + ' with object ID '
					+ event.data.id);
		}
		function isManager(item) {
			return (item.grade > 4);
		}
		function parseDate(item) {
			var d = new Date(item.birthDate);
			return d.toDateString();
		}
	</script>
</body>
</html>