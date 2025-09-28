<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Excel上传</title>
<link type="text/css" href="${pageContext.request.contextPath }/components/bootstrap/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
    <div class="container" align="center">
        <form action="${pageContext.request.contextPath }/uploadExcel" method="post" enctype="multipart/form-data">
            <table class="table" style="width: 50%">
                <tr id="msg" style="display: none;">
                    <th colspan="2" style="text-align: center;">
                        <font color="#00CD00">文件上传成功，共用时${time }ms</font>
                    </th>
                </tr>
                <tr>
                    <th>上传文件1:</th>
                    <td><input type="file" name="file" multiple="multiple"/></td>
                </tr>
                <tr>
                    <th>上传文件2:</th>
                    <td><input type="file" name="file"/></td>
                </tr>
                <tr>
                    <th>上传人:</th>
                    <td><input type="text" name="user"/></td>
                </tr>
                <tr>
                    <th> </th>
                    <td>
                        <button id="submit" type="submit" class="btn btn-default">上传</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <!-- 文件上传模态框 -->
    <div id="progressModal" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static"
        data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">文件上传进度</h4>
                </div>
                <div class="modal-body">
                    <div id="progress" class="progress">
                        <div id="progress_rate" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
                            class="progress-bar progress-bar-success progress-bar-striped active"
                            role="progressbar" style="width: 0%">
                                <span id="percent">0%</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/components/bootstrap/js/bootstrap.min.js"></script>

    <script type="text/javascript">
     
        var process;// 定时对象
     
        $(function() {
             
            var time = "${time}";
            if (time != null && time != "") {
                $("#msg").css("display", "block");
            }
             
            $("#submit").bind("click", function(){
                 
                process = setInterval("getProcess()", 100);// 每0.1s读取一次进度
                $("#progressModal").modal("show");// 打开模态框
                 
            });
             
        });
         
        // 读取文件上传进度并通过进度条展示
        function getProcess() {
            $.ajax({
                type: "get",
                url: "${pageContext.request.contextPath }/uploadExcel",
                success: function(data) {
                    var rate = data * 100;
                    rate = rate.toFixed(2);
                    console.log(rate);
                    $("#progress_rate").css("width", rate + "%");
                    $("#percent").text(rate + "%");
                    if (rate >= 100) {
                        clearInterval(process);
                        $("#percent").text("文件上传成功！");
                    }
                }
            });
        }
         
    </script>
</body>
</html>