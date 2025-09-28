<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <jsp:include page="top.jsp"/>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="apple-mobile-web-capable" content="yes">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="common/css/common.css">
<link rel="stylesheet" type="text/css" href="common/css/style.css">
 <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
<script type="text/javascript" src="common/js/jquery.min.js"></script>
<script type="text/javascript" src="common/js/seach.js"></script>
<script type="text/javascript" src="common/js/scrolls.js"></script>
<script type="text/javascript" src="common/js/addfriends.js"></script>

<style>
::-webkit-scrollbar {
	width: 0px;
}

.bri_bottom_iphone {
	background: url(common/img/voice_icon.jpg);
	background-size: 17px 17px;
	background-repeat: no-repeat;
}

.bri_bottom_iphone:hover {
	background: url(common/img/voice_icon.jpg);
	background-size: 17px 17px;
	background-repeat: no-repeat;
}

.masking {
	width: 100%;
	height: 100%;
	position: fixed;
	display: none;
	top: 0;
	left: 0;
}

.layer {
	position: relative;
	width: 300px;
	height: 300px;
	border: 3px solid #fff;
	background: #ccc;
	left: 40%;
	top: 20%;
}

.close {
	right: 15px;
	top: 15px;
}

.voiceRecord {
	color: #FFF;
	width: 300px;
	margin: 0 auto;
	background: #933;
	height: 30px;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	border: none;
	-webkit-appearance: none;
	cursor: pointer;
}

.bcen_seach {
	width: 213px;
}

.bcen_seach_font {
	width: 188px;
}

.bcen_seach input[type="text"] {
	width: 188px;
}

.msg_list_wx_type_l {
	margin-left: 10px;
	margin-top: 5px;
	background: white;
	border-radius: 5px;
}

.msg_list_wx_type_r {
	margin-right: 10px;
	margin-top: 5px;
	background: white;
	border-radius: 5px;
}
</style>
<script type="text/javascript">
var uuid = "${uuid}";
</script>
</head>


<body style="">
	<div class="buju">
          	<!-- 最左侧功能图标 -->
          	<div class="buju_left fl" style="height: 872px;">
                    	<div class="bleft_face fl">
                              	<img src="${customerLoginUserData.head_img }" style="width: 100%; margin-top: 0px;">		
                              </div>
                              <div class="bleft_iconone fl">
                              	<a href="index.jsp"><img src="common/img/chat011.png" width="100%"></a>
                              </div>
                            <!--   <div class="bleft_iconnum">1</div> -->
                              <div class="bleft_iconone fl">
                              	<a href="${pageContext.request.contextPath }/customerservice/go2Friends.html"><img src="common/img/chat020.png" width="100%"></a>
                              </div>
                    </div>
                    <script>
			$(function (){
				var image_width=$(".bleft_face").width();
				var image_height=$(".bleft_face").height();
				var image_bi=image_width/image_height;
				$(".bleft_face img").each(function (){
					var images_width=$(this).width();
					var images_height=$(this).height();
					var images_bi=images_width/images_height;
					if(image_bi<images_bi){
						$(this).css("height",image_height);
						var imgwidth=image_height * images_bi;
						var widths= 0 - (imgwidth - image_width) / 2;
						$(this).css("margin-left",widths);
					}else{
						$(this).css("width","100%");	
						var imgheight=image_width / images_bi;	
						var heights=0 - (imgheight - image_height) / 2;
						$(this).css("margin-top",heights);
					}
				})	
			})				
		</script>
                    <!-- 最左侧功能图标 -->
                    <!-- 中间消息列表-->
                    <div class="buju_center fl" style="height: 872px;">
                    	<div class="bcen_seach fl">
                              	<div class="bcen_seach_font fl">搜索</div>
                              	<input type="text" style="display:none;" class="fl" id="searchInputVal">
                                        <div class="bcen_seach_close fr" style="display:none;"><img src="common/img/close.png"></div>
                                        <div class="bcen_seach_but fr">
                                        	<img src="common/img/seach.png" width="100%">
                                        </div>
                              </div>
                             <!--  <a href="javascript:void(0);" onclick="addfris()">
                                        <div class="bcen_add fl">
                                                  <img src="common/img/add001.png">	
                                        </div>
                              </a> -->
                              <div class="bcen_scroll" style="position: absolute; right: 0px; top: 62px; display: none; height: 603px;"></div>
                              <div class="clear"></div>
                              <div class="bcen_box" style="height: 810px;">
                                        <div class="bcen_jilu" id="userMsgList">
                                                 
                                        </div>
                                        
                                        <div class="bcen_jilu" style="display:none;">
                                        	<div class="bsea_first">联系人</div>
                                                  <div class="bcen_block">
                                                            <div class="bcen_block_face fl">
                                                                      <img src="common/img/face003.jpg" style="width: 100%;">
                                                            </div>	
                                                            <div class="bcen_block_font fl">
                                                                      <div class="bcen_block_title" style="line-height:40px;">冰雪</div>
                                                                      <!--<div class="bcen_block_fonts over">出去玩吗？</div>-->	
                                                            </div>
                                                  </div>
                                                  <div class="bcen_block">
                                                            <div class="bcen_block_face fl">
                                                                      <img src="common/img/face003.jpg" style="width: 100%;">
                                                            </div>	
                                                            <div class="bcen_block_font fl">
                                                                      <div class="bcen_block_title">冰雪</div>
                                                                      <div class="bcen_block_fonts over">昵称：劳拉·克劳馥</div>	
                                                            </div>
                                                  </div>
                                                  <!-- <div class="bsea_first">群聊</div>
                                                  <div class="bcen_block">
                                                            <div class="bcen_block_face fl">
                                                                      <img src="common/img/face003.jpg" style="width: 100%;">
                                                            </div>	
                                                            <div class="bcen_block_font fl">
                                                                      <div class="bcen_block_title">家庭一群</div>
                                                                      <div class="bcen_block_fonts over">包含：阿姐</div>	
                                                            </div>
                                                  </div>
                                                  <div class="bcen_block">
                                                            <div class="bcen_block_face fl">
                                                                      <img src="common/img/face003.jpg" style="width: 100%;">
                                                            </div>	
                                                            <div class="bcen_block_font fl">
                                                                      <div class="bcen_block_title">单机游戏交流群</div>
                                                                      <div class="bcen_block_fonts over">包含：哥哥（阿T）</div>	
                                                            </div>
                                                  </div> -->
                                        
                                        </div>
                                        
                              </div>
		</div>       
                    <!-- 中间消息列表-->
                    <!-- 右边对话框 -->
                    <div class="buju_right fl" style="height: 872px; width: 1610px;">
                    	<div class="bri_top">
                              	<div class="bri_top_font fl" id="userMsgDetailTitle">
                                        </div>
                                        <div class="bri_top_more fr"></div>
                                        <div class="clear"></div>
                              </div>
                              <div class="bri_center" style="height: 667px;">
                              	<div class="bri_center_scroll" style="display: none; height: 488px;"></div>
                                        <div class="bri_center_block">
                                        	<div class="brce_block_box" style="width: 1610px; height: 667px;overflow:auto">
                               			<div id="userMsgDetailList">
                              	
                                   		</div>
                                   		<div><a id="msg_end" name="1" href="#1">&nbsp;</a></div>
                                                  </div>
                                        </div>
                              </div>
                              <script>
				$(function (){
					img();
				})
				function img(){
					var img_width = $(".brce_left_face").width();
					var img_height = $(".brce_left_face").height();
					var img_bi=img_width/img_height;
					$(".brce_left_face img").each(function (){
						var imgs_width=$(this).width();
						var imgs_height=$(this).height();
						var imgs_bi=imgs_width/imgs_height;	
						if(img_bi < imgs_bi){
							$(this).css("height",img_height);
							var imgswidth = img_height * imgs_bi;
							var wid= 0 - (imgswidth - img_width) / 2;
							$(this).css("margin-left",wid);
						}else{
							$(this).css("width","100%");	
							var imgsheight=img_width / img_bi;
							var hei=0 - (imgsheight - img_height) / 2;
							$(this).css("margin-top",hei);
						}
					})
				}				
			</script>	
                    	<div class="bri_bottom" style="width: 1610px; background-color: rgb(245, 245, 245);">
                              	<div class="bri_bottom_face fl" title="表情"></div>
                                        <div class="bri_bottom_look" style="display:none;">
                                        	<div class="bri_blook_center">

										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/100.gif" title="微笑" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/101.gif" title="伤心">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/102.gif" title="美女">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/103.gif" title="发呆">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/104.gif" title="墨镜">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/105.gif" title="哭">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/106.gif" title="羞">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/107.gif" title="哑">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/108.gif" title="睡">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/109.gif" title="哭">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/110.gif" title="囧">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/111.gif" title="怒">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/112.gif" title="调皮">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/113.gif" title="笑">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/114.gif" title="惊讶">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/115.gif" title="难过">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/116.gif" title="酷">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/117.gif" title="汗">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/118.gif" title="抓狂">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/119.gif" title="吐">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/120.gif" title="笑">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/121.gif" title="快乐">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/122.gif" title="奇">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/123.gif" title="傲">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/124.gif" title="饿">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/125.gif" title="累">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/126.gif" title="吓">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/127.gif" title="汗">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/128.gif" title="高兴">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/129.gif" title="闲">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/130.gif" title="努力">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/131.gif" title="骂">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/132.gif" title="疑问">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/133.gif" title="秘密">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/134.gif" title="乱">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/135.gif" title="疯">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/136.gif" title="哀">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/137.gif" title="鬼">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/138.gif" title="打击">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/139.gif" title="bye">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/140.gif" title="汗">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/141.gif" title="抠">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/142.gif" title="鼓掌">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/143.gif" title="糟糕">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/144.gif" title="恶搞">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/145.gif" title="什么">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/146.gif" title="什么">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/147.gif" title="累">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/148.gif" title="看">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/149.gif" title="难过">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/150.gif" title="难过">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/151.gif" title="坏">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/152.gif" title="亲">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/153.gif" title="吓">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/154.gif" title="可怜">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/155.gif" title="刀">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/156.gif" title="水果">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/157.gif" title="酒">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/158.gif" title="篮球">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/159.gif" title="乒乓">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/160.gif" title="咖啡">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/161.gif" title="美食">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/162.gif" title="动物">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/163.gif" title="鲜花">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/164.gif" title="枯">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/165.gif" title="唇">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/166.gif" title="爱">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/167.gif" title="分手">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/168.gif" title="生日">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/169.gif" title="电">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/170.gif" title="炸弹">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/171.gif" title="刀子">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/172.gif" title="足球">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/173.gif" title="瓢虫">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/174.gif" title="翔">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/175.gif" title="月亮">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/176.gif" title="太阳">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/177.gif" title="礼物">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/178.gif" title="抱抱">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/179.gif" title="拇指">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/180.gif" title="贬低">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/181.gif" title="握手">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/182.gif" title="剪刀手">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/183.gif" title="抱拳">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/184.gif" title="勾引">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/185.gif" title="拳头">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/186.gif" title="小拇指">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/187.gif" title="拇指八">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/188.gif" title="食指">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/189.gif" title="ok">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/190.gif" title="情侣">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/191.gif" title="爱心">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/192.gif" title="蹦哒">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/193.gif" title="颤抖">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/194.gif" title="怄气">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/195.gif" title="跳舞">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/196.gif" title="发呆">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/197.gif" title="背着">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/198.gif" title="伸手">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/199.gif" title="耍帅">
				
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/200_firecracker.png" title="爆竹" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Fireworks.png" title="烟花" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Blessing.png" title="福" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/202_Packet.png" title="红包" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/203_Party.png" title="庆祝" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/204_Rich.png" title="發" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/214.png" title="合十" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/215_Broken.png" title="裂开" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Hurt.png" title="苦涩" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Sigh.png" title="叹气" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/LetMeSee.png" title="让我看看" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Awesome.png" title="666" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Boring.png" title="翻白眼" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Wow.png" title="哇" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/MyBad.png" title="打脸" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/NoProb.png" title="好的" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Doge.png" title="旺柴" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Respect.png" title="社会社会" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Emm.png" title="Emm" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/OMG.png" title="天啊" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Sweats.png" title="汗" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/GoForIt.png"  title="加油" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Onlooker.png"  title="吃瓜" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Yeah.png"  title="耶" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Concerned.png"  title="邹眉" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Smart.png"  title="机智" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/GoForIt.png"  title="加油" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Onlooker.png"  title="吃瓜" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Yeah.png"  title="耶" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Concerned.png"  title="邹眉" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Smart.png"  title="机智" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Smirk.png"  title="奸笑" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Facepalm.png"  title="捂脸" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Hey.png"  title="嘿哈" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Duh.png"  title="无语" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/LetDown.png"  title="失望" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Terror.png"  title="恐惧" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Lol.png"  title="破涕为笑" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Flushed.png"  title="脸红" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Sick.png"  title="生病" width="24">
										</div>
										<div class="fl">
											<img class="wechat-emoji" src="common/img/wxface/Happy.png"  title="笑脸" width="24">
										</div>
					</div>  
                                        </div>
                                        <div class="bri_bottom_file fl" title="发送文件"></div>
                                        <input type="file" class="bri_bottom_files" id="imgUploadId" style="display:none;" onchange="uploadImg();" accept=".jpg,.png,.gif,.jpeg,.mp4">
                                        <div class="bri_bottom_iphone fr" title="发送语音" id="sendVoiceMsgId"></div>
                                        <div class="clear"></div>
                                        <!-- <textarea style="width: 1560px;"></textarea> -->
										<div contentEditable="true" id="msgWriteContent" onblur="lastMsgRang();" style="width: 98%; height: 100px; line-height: 27px; margin-top: 13px; max-height: 300px; margin: 0 auto; margin-top: 11px; outline: 0; border-radius: 4px; font-size: 16px; word-wrap: break-word; overflow-x: hidden; overflow-y: auto;-webkit-user-modify: read-write-plaintext-only;"></div>
										<div class="bri_bottom_but fr" id="sendMsgId" openId="" style="margin-top: 5px;">发送</div>
                                        <div class="bri_bottom_empty" style="display:none;">不能发送空白信息</div>
                              </div>
                    </div>
                    <!-- 右边对话框 -->
                    <!-- 最右设置框 -->
                    <div class="buju_install fr" style="display: none; height: 811px;">
                    	
                    	<!-- 单聊设置 -->
                    	<!--<div class="bin_one">
                              	<div class="bin_one_face">
                                        	<img src="common/img/face003.jpg"/>
                                        </div>
                                        <div class="bin_one_name over">冰雪</div>
                              </div>
                              <div class="bin_two">
                              	<div class="bin_two_font">消息免打扰</div>
                                        <div class="bin_two_but">
                                        	<img src="common/img/butclose.png"/>
                                        </div>
                              </div>-->
                              <!-- 单聊设置 -->
                              <!-- 群聊设置 -->
                              <div class="bin_four_box">
                              	<div>
                                        <div class="bin_three">
                                                  <input type="text" placeholder="搜索群成员" class="fl">
                                                  <img src="common/img/seach.png" class="bin_three_seach fr">
                                                  <img src="common/img/close.png" class="bin_three_close fr" style="display:none">
                                        </div>
                              	<div class="bin_four">
                                                            <div class="fl" onclick="addfris()">
                                                                      <div class="bin_four_add"></div>
                                                                      <div class="bin_four_name over">添加</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face003.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">冰雪</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face001.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">小圣贤庄张三</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face010.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">卖兜的小麦兜</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face016.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">斯塔克</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face017.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">明教教主</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face012.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">BVS</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face005.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">阿卡姆骑士</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face006.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">A.呵呵呵</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face015.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">戴安娜</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face009.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">阿姐</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face004.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">首席铲屎官</div>
                                                            </div>
                                                            
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face016.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">斯塔克</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face017.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">明教教主</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face012.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">BVS</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face005.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">阿卡姆骑士</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face006.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">A.呵呵呵</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face015.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">戴安娜</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face009.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">阿姐</div>
                                                            </div>
                                                            <div class="fl">
                                                                      <div class="bin_one_face">
                                                                                <img src="common/img/face004.jpg" style="width: 100%;">
                                                                      </div>
                                                                      <div class="bin_four_name over">首席铲屎官</div>
                                                            </div>
                                                            <div class="clear"></div>
                                                            <div class="bin_four_more"><label>查看更多群成员</label> <img src="common/img/down001.png"></div>
                                                            <div class="bin_four_border"></div>
                              		</div>
                                                  <!-- 群聊设置 -->
                                                  <script>
                                                            $(function (){
                                                                      var iw = $(".bin_one_face").width();
                                                                      var ih = $(".bin_one_face").height();
                                                                      var ib = iw/ih;
                                                                      $(".bin_one_face img").each(function (){
                                                                                var isw=$(this).width();
                                                                                var ish=$(this).height();
                                                                                var isb=isw/ish;
                                                                                if(ib<isb){
                                                                                          $(this).css("height",ih);
                                                                                          var iwi=ih * isb;
                                                                                          var ws= 0 - (iwi - iw) / 2;
                                                                                          $(this).css("margin-left",ws);
                                                                                }else{
                                                                                          $(this).css("width","100%");	
                                                                                          var ihe=iw / isb;	
                                                                                          var hs=0 - (ihe - ih) / 2;
                                                                                          $(this).css("margin-top",hs);
                                                                                }
                                                                                          
                                                                      })
                                                            })
                                                  </script>
                                                  <div class="bin_five">
                                                  	<p>群名</p>
                                                            <div class="bin_five_text fl">单机游戏交流群</div>
                                                            <input type="hidden" value="单机游戏交流群" class="bin_five_name">
                                                            <img src="common/img/edit.jpg" class="fl" style="display:none;">
                                                            <div class="clear"></div>
                                                            <p>群公告</p>
                                                            <div class="bin_five_note over3">别忘了今天下午交流群见面会</div>
                                                            <p>我在本群的昵称</p>
                                                            <div class="bin_five_text fl">小圣贤庄张三</div>
                                                            <input type="hidden" value="小圣贤庄张三" class="bin_five_name">
                                                            <img src="common/img/edit.jpg" class="fl" style="display:none;">
                                                            <div class="clear"></div>
                                                            <p>显示群成员昵称</p>
                                                            <img class="bin_five_butname" src="common/img/butopen.png">
                                                            <p>消息免打扰</p>
                                                            <img class="bin_five_but" src="common/img/butclose.png">
                                                  </div>
                                                  <a href="index.jsp"><div class="bin_six">删除并退出</div></a>
                                                  <div class="bin_note" style="display:none;">
                                                  	<div class="bin_note_title fl">群公告</div>
                                                            <div class="bin_note_close fr" title="关闭"></div>
                                                            <div class="clear"></div>
                                                            <div class="bin_note_detail">
                                                            	<div class="bin_nd_face fl">
                                                                      	<img src="common/img/face003.jpg" style="width: 100%;">
                                                                      </div>	
                                                                      <div class="bin_nd_font fl">
                                                                      	<p>冰雪</p>
                                                                                <p>昨天</p>
                                                                      </div>
                                                                      <div class="clear"></div>
                                                                      <div class="bin_nd_detail">别忘了今天下午交流群见面会</div>
                                                                      <div class="bin_nd_warn">————— 仅群主可编辑 —————</div>
                                                            </div>
                                                  </div>
                                                  <script>
                                                            $(function (){
                                                                      var iw = $(".bin_nd_face").width();
                                                                      var ih = $(".bin_nd_face").height();
                                                                      var ib = iw/ih;
                                                                      $(".bin_nd_face img").each(function (){
                                                                                var isw=$(this).width();
                                                                                var ish=$(this).height();
                                                                                var isb=isw/ish;
                                                                                if(ib<isb){
                                                                                          $(this).css("height",ih);
                                                                                          var iwi=ih * isb;
                                                                                          var ws= 0 - (iwi - iw) / 2;
                                                                                          $(this).css("margin-left",ws);
                                                                                }else{
                                                                                          $(this).css("width","100%");	
                                                                                          var ihe=iw / isb;	
                                                                                          var hs=0 - (ihe - ih) / 2;
                                                                                          $(this).css("margin-top",hs);
                                                                                }
                                                                                          
                                                                      })
                                                            })
                                                  </script>
				</div>
			</div>
                    </div>
                    <!-- 最右设置框 -->
                    <!-- 添加弹框 -->
                    <div class="yinyings" style="display: none; height: 872px;"></div>
                    <div class="buju_add" style="display:none;">
                    	<div class="ba_left fl">
                              	<div class="ba_left_seach">
                                        	<input type="text" class="fl" placeholder="搜索">
                                                  <img class="ba_lefts_seach fr" src="common/img/seach.png">
                                                  <img class="ba_lefts_close fr" style="display:none;" src="common/img/close.png">
                                        </div>
                                        <div class="ba_left_fri">
                                        	<div class="ba_leftf_scroll" style="position:absolute;right:0px;display:none;"></div>	
                                                  <div class="ba_leftf_box">
                                                  	<div>
                                                            	<div class="ba_leftf_first">A</div>
                                                            	<div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face006.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">A.呵呵呵</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face007.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">A...</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face008.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">Angle</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face009.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">阿姐</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face005.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">阿卡姆骑士</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face010.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">安安</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face011.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">安好</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_first">B</div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face012.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">BVS</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                                      <div class="ba_leftf_block">
                                                                      	<div class="ba_lfb_face fl">
                                                                                	<img src="common/img/face013.jpg">
                                                                                </div>
                                                                                <div class="ba_lfb_font fl">本宝宝</div>
                                                                                <div class="ba_lfb_circle fr"><img src="common/img/circle010.png"></div>
                                                                      </div>
                                                            </div>
                                                  </div>
                                                  
                                        </div>			
                              </div>	
                              <div class="ba_right fl">
                              	<div class="buju_add_close" onclick="addclose()"></div>
                              	<div class="ba_right_top">请勾选需要添加的联系人</div>
                                        <div class="ba_right_fri">
                                        	<div class="ba_rif_scroll" style="position:absolute;right:0px;display:none;"></div>
                                                  <div class="ba_rif_box">
                                                  	<div></div>
                                                  </div>	
                                        </div>
                                        <div class="ba_ri_exit fr" onclick="addclose()">取消</div>
                                        <div class="ba_ri_but fr">确定</div>
                                        
                                        
                              </div>
                              
                              <script>
				function friface(){
					var image_width=$(".ba_lfb_face").width();
					var image_height=$(".ba_lfb_face").height();
					var image_bi=image_width/image_height;
					$(".ba_lfb_face img").each(function (){
						images_width=$(this).width();
						var images_height=$(this).height();
						var images_bi=images_width/images_height;
						if(image_bi<images_bi){
							$(this).css("height",image_height);
							var imgwidth=image_height * images_bi;
							var widths= 0 - (imgwidth - image_width) / 2;
							$(this).css("margin-left",widths);
						}else{
							$(this).css("width","100%");	
							var imgheight=image_width / images_bi;	
							var heights=0 - (imgheight - image_height) / 2;
							$(this).css("margin-top",heights);
						}
					})	
				}				
			</script>
                    </div>
                    <!-- 添加弹框 -->
	</div>

								<div class="masking">
								    <div class="layer" id="voiceLayer">
								        <div><img src="common/img/voicePrepar.jpg" alt="" width="300" height="300" id="voiceImgId"></div>
								        <div style="height: 10px;"></div>
								        <button id="btn-start-recording" class="voiceRecord">开始录音</button>
        								<button id="btn-stop-recording" class="voiceRecord" style="display: none; background: #129611;">停止录音</button>
        								<button id="btn-sendRecord" class="close voiceRecord" style="display: none;background: red;">发送录音</button>
								        <div style="height: 10px;"></div>
								        <div><input type="button" value="取消" class="close voiceRecord" id="btn-cancleRecord" style="background: #818181;"></div>
								        <div><audio controls autoplay></audio></div>
								    </div>
								</div>
								
								<input type="hidden" id="pageNum" value="1"/>
<script type="text/javascript">

	$(".bri_blook_center img").each(function(index){
		$(this).click(function(){
			insertHtmlAtCursor(this);
		});
	});


		var msgRange;
		function insertHtmlAtCursor(html) {
			var htmlChart = "<img src='"+html.src+"'/>";
		    var node;
		    if (window.getSelection && window.getSelection().getRangeAt) {
		        node = msgRange.createContextualFragment(htmlChart);
		        msgRange.insertNode(node);
		    } else if (document.selection && document.selection.createRange) {
		        document.selection.createRange().pasteHTML(htmlChart);
		    }
		}
		
		function lastMsgRang(){
			msgRange = window.getSelection().getRangeAt(0);
		}

		function updateNewMsg2Read(){
			if(currentMsgOpenId!=null&&currentMsgOpenId!='undefined'){
				$.ajax({
					url:'updateNewMsg2Read.html',
					type:'post',
					data:'openId='+currentMsgOpenId,
				})
			}
		}
		
		function sendMsg(openId,msgContent){
			$.ajax({
				url:'sendMsg.html',
				type:'post',
				data:'openId='+openId+"&textContet="+msgContent,
			})
		}

			function initUserMsgList(){
				$.ajax({
		    		url:'getUserMsgList.html',
		    		type:'get',
		    		data:'wxName='+$("#searchInputVal").val()+"&page="+$("#pageNum").val(),
		    		success:function(rs){
		    			var data = eval('('+rs+')');
		    			$("#userMsgList").empty();
		    			if(data!=null&&data.length>0){
		    				var templ = "";
		    				for(var index in data){
		    					var item = data[index];
			    				templ += "<div class='bcen_block' id='"+item.openId+"' index='"+index+"' onclick='go2MsgDetail(\042"+item.openId+"\042);'> "
				    			+"       <div class='bcen_block_face fl'>                                                  "
				    			+"                 <img id='head_img_"+item.openId+"' src='"+item.head_img+"' style='width: 100%; margin-top: 0px;'>"
				    			+"           </div>	                                                                    "
				    			+"			 <div class='"+(item.isNewMsg==1?"bcen_block_dian":"")+"' id='msgNew_"+item.openId+"'></div>"         
				    			+"           <div class='bcen_block_font fl'>                                              "
				    			+"                     <div class='bcen_block_title' id='wxName_"+item.openId+"' title='"+item.wxName+"'>"+item.wxName+"</div>                            "
				    			+"                     <div class='bcen_block_fonts over' id='content_"+item.openId+"'>"+(item.lastMsgContext==null?'':item.lastMsgContext)+"</div>	                "
				    			+"           </div>                                                                        "
				    			+"           <div class='bcen_block_right fr'>                                             "
				    			+"                     <div class='bcen_block_time'>"+pagDate(item.lastMsgDateTime)+"</div>                             "
				    			+"           </div> "
	    						+" </div>";
		    				}
		    				$("#userMsgList").append(templ);
					    	initUserDataAct();
					    	defaultCheckUserOpenId();
		    			}
		    		}
		    	})
			}
			
			// numberMillis 毫秒
			function sleep(numberMillis) {
			   var now = new Date();
			   var exitTime = now.getTime() + numberMillis;
			   while (true) {
			      now = new Date();
			      if (now.getTime() > exitTime)
			      　　return;
			      }
			   }
			
			var displayMsgOpenId = "";
			var isMsgNew_ = 0;
			var dataLength_ = 0;
			function getUserMsgDetailList(){
				if(currentMsgOpenId!=null&&currentMsgOpenId!='undefined'){
					$.ajax({
			    		url:'getUserMsgDetail.html',
			    		type:'post',
						data:'openId='+currentMsgOpenId,
			    		success:function(rs){
			    			var data = eval('('+rs+')');
			    			if(data!=null&&data.length>0){
			    				var templ = pagUserMsg(data);
			    				$("#userMsgDetailList").empty();
			    				$("#userMsgDetailList").append(templ);
			    				res();
			    				scrolls();
			    				chatscr();
			    				$(window).resize(function(){
			    					res();
			    					scrolls();
			    					chatscr();	
			    				})
			    				if(displayMsgOpenId!=currentMsgOpenId||isMsgNew_==1){
			    					displayMsgOpenId = currentMsgOpenId;
			    					isMsgNew_=0;
				    				document.getElementById("msg_end").scrollIntoView();
			    				}
			    			}
			    		}
			    	})
				}
			}
			
			function quartzGetUserMsgDetailList(){
				if(currentMsgOpenId!=null&&currentMsgOpenId!='undefined'){
					$.ajax({
			    		url:'getUserNewMsgDetail.html',
			    		type:'post',
						data:'openId='+currentMsgOpenId,
			    		success:function(rs){
			    			var data = eval('('+rs+')');
			    			if(data!=null&&data.length>0){
			    				var templ = pagUserMsg(data);
			    				$("#userMsgDetailList").append(templ);
			    				res();
			    				scrolls();
			    				chatscr();
			    				$(window).resize(function(){
			    					res();
			    					scrolls();
			    					chatscr();	
			    				})
			    				if(displayMsgOpenId!=currentMsgOpenId||isMsgNew_==1){
			    					displayMsgOpenId = currentMsgOpenId;
			    					isMsgNew_=0;
			    				}
			    				document.getElementById("msg_end").scrollIntoView();
			    			}
			    		}
			    	})
				}
			}
			
			function pagUserMsg(data){
				var templ = "";
				for(var index in data){
					var item = data[index];
					if(dataLength_!=data.length){
						isMsgNew_ = 1;
						dataLength_=data.length;
					}
					if(item.msgSendOrReceive==1){
	    				templ += "<div class='brce_time'>"+item.createTime+"</div><div class='brce_left'>                                                      "
	    					+ "<div class='brce_left_face fl'>                                                "
	    					+ "	<img src='"+item.fromHead_img+"' style='width: 100%; margin-top: 0px;'>       "
	    					+ "</div>                                                                         "
	    					+ "<div class='brce_left_talk fl'>                                                "
	    					+ "<div class='brce_lt_name'>"+item.fromName+"</div>                              "
	    					+ "        <div class='brce_lt_jiao fl'></div>                                    "
	    					+"            <div class='";
    						if(item.messageType!="voice"&&item.messageType!="video"&&item.messageType!="image"){
    							templ +="brce_lt_block";
    						}else{
    							templ +="msg_list_wx_type_l";
    						}
    						templ +=" fl'><div>";
    						if(item.messageType=="image"){
    							if(item.isDownload==-1){
    								templ +="<img style='cursor: pointer;border-radius: 5px;' id='"+item.mediaId+"' formatType='"+item.format+"' onclick='imgReDownLoad($(this))' style='cursor: pointer;' src='common/img/imgDownFail.jpg' title='点击重新下载图片'>";
    							}else{
	    							templ +="<img class='pic' onclick='displayOrgImg($(this))' id='"+item.mediaId+"' style='cursor: pointer;border-radius: 5px;'  src='${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"_abbr&fileType=images&suffix="+item.format+"' imgorgsrc='${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"&fileType=images&suffix="+item.format+"' title='点击查看原图'>";
    							}
    						}else if(item.messageType=="voice"){
    							templ +="<a href=\042javascript:;\042 onclick=\042playVoice('"+item.mediaId+"')\042><img src='${pageContext.request.contextPath}/customerservice/common/img/voiceF.jpg'></a>";
    						}else if(item.messageType=="video"){
    							templ +="<video  controls=\042controls\042 width=\042320\042 height=\042100%\042 poster=\042${pageContext.request.contextPath}/cloud/download.html?fileName="+item.thumbMediaId+"&fileType=video&suffix=jpg\042 >"
    								  + "  <source src=\042${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"&fileType=video&suffix=mp4\042 type=\042video/mp4; codecs=\042avc1.4D401E, mp4a.40.2\042>"
    								  + "</video>";
    						}else{
    							templ +=item.content;
    						}
    						templ +="</div></div>	                                                         "
    						+"  </div>                                                                        "
	    					+ "  	<div class='clear'></div>                                                  "
	    					+ "</div>                                                                         ";
					}else{
						templ +="<div class='brce_time'>"+item.createTime+"</div><div class='brce_right'>                                                       "
						+"  <div class='brce_left_face fr'>                                              "
						+"  	<img src='"+item.head_img+"' style='width: 100%; margin-top: 0px;'> "
						+"  </div>                                                                       "
						+"  <div class='brce_left_talk fr'>                                              "
						+"            <div class='brce_rt_jiao fr'></div>                                "
						+"            <div class='";
						if(item.messageType!="voice"&&item.messageType!="video"&&item.messageType!="image"){
							templ +="brce_rt_block";
						}else{
							templ +="msg_list_wx_type_r";
						}
						templ +=" fr'><div>";
						if(item.messageType=="image"){
							if(item.isDownload==-1){
								templ +="<img style='cursor: pointer;' id='"+item.mediaId+"' formatType='"+item.format+"' onclick='imgReDownLoad($(this))' style='cursor: pointer;' src='common/img/imgDownFail.jpg' title='点击重新下载图片'>";
							}else{
    							templ +="<img class='pic' onclick='displayOrgImg($(this))' id='"+item.mediaId+"' style='cursor: pointer;'  src='${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"_abbr&fileType=images&suffix=jpg' imgorgsrc='${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"&fileType=images&suffix=jpg' title='点击查看原图'>";
							}
						}else if(item.messageType=="voice"){
							templ +="<a href=\042javascript:;\042 onclick=\042playVoice('"+item.mediaId+"')\042><img src='${pageContext.request.contextPath}/customerservice/common/img/voiceT.jpg'></a>";
						}else if(item.messageType=="video"){
							templ +="<video  controls=\042controls\042 width=\042100%\042 height=\042100%\042 poster=\042${pageContext.request.contextPath}/cloud/download.html?fileName="+item.thumbMediaId+"&fileType=video&suffix=jpg\042 >"
							  + "  <source src=\042${pageContext.request.contextPath}/cloud/download.html?fileName="+item.mediaId+"&fileType=video&suffix=mp4\042 type=\042video/mp4; codecs=\042avc1.4D401E, mp4a.40.2\042>"
							  + "</video>";
						}else{
							templ +=item.content;
						}
						templ +="</div></div>	                                                         "
						+"  </div>                                                                    "
						+"  <div class='clear'></div>                                                    "
						+"</div>                                                                         ";
					}
				}
				
				return templ;
			}
			var audio= new Audio();
			
			function playVoice(mediaId){
				$.ajax({
					url:'playVoice.html',
					type:'get',
					data:'mediaId='+mediaId,
					success:function(rs){
						if(rs=="10001"){
							alert("语音已超过3天未下载，已超时！请及时下载！");
							return ;
						}if(rs=="10002"){
							alert("语音转换失败，请重试！");
							return ;
						}
						audio.pause();
						audio= new Audio("${pageContext.request.contextPath}/cloud/download.html?fileName="+rs+"&fileType=voice&suffix=mp3");//这里的路径写上mp3文件在项目中的绝对路径
						audio.play();//播放
					}
				})
			}
			function pagDate(lastMsgDate){
				var date = new Date();
		        var currentDate;
				var year = date.getFullYear()+"-";//获取完整的年月日
				currentDate = year;
				var month=date.getMonth()+1;
				var day=date.getDate();
			    if(month >= 10) {
			    	currentDate += (month + "-");
		        }else {
		        	currentDate += ("0" + month + "-");
		        }
		        if(day >= 10) {
		        	currentDate += day;
		        }else {
		        	currentDate += ("0" + day);
		        }
				if(lastMsgDate.indexOf(currentDate)!=-1){//当天
					return dateFormat("HH:MM",new Date(lastMsgDate));
				}else{
					return dateFormat("YY/mm/dd",new Date(lastMsgDate)).substr(2);
				}
			}
			
			function dateFormat(fmt, date) {
			    let ret;
			    const opt = {
			        "Y+": date.getFullYear().toString(),        // 年
			        "m+": (date.getMonth() + 1).toString(),     // 月
			        "d+": date.getDate().toString(),            // 日
			        "H+": date.getHours().toString(),           // 时
			        "M+": date.getMinutes().toString(),         // 分
			        "S+": date.getSeconds().toString()          // 秒
			        // 有其他格式化字符需求可以继续添加，必须转化成字符串
			    };
			    for (let k in opt) {
			        ret = new RegExp("(" + k + ")").exec(fmt);
			        if (ret) {
			            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
			        };
			    };
			    return fmt;
			}
			
			initUserMsgList();
			
			function quartzUserMsgList(){
				$.ajax({
		    		url:'getUserMsgList.html',
		    		type:'get',
		    		data:'wxName='+$("#searchInputVal").val()+"&page="+$("#pageNum").val(),
		    		success:function(rs){
		    			var data = eval('('+rs+')');
		    			if(data!=null&&data.length>0){//如果存在的，则直接移除，取上一个后插入
		    				for(var index in data){
		    					var item = data[index];
		    					var openIdVal = $("#"+item.openId);
		    					if(openIdVal.attr("id")==null){
			    					var templ = "<div class='bcen_block' id='"+item.openId+"' index='"+index+"' onclick='go2MsgDetail(\042"+item.openId+"\042);'> "
					    			+"       <div class='bcen_block_face fl'>                                                  "
					    			+"                 <img id='head_img_"+item.openId+"' src='"+item.head_img+"' style='width: 100%; margin-top: 0px;'>"
					    			+"           </div>	                                                                    "
					    			+"			 <div class='"+(item.isNewMsg==1?"bcen_block_dian":"")+"' id='msgNew_"+item.openId+"'></div>"     
					    			+"           <div class='bcen_block_font fl'>                                              "
					    			+"                     <div class='bcen_block_title' id='wxName_"+item.openId+"' title='"+item.wxName+"'>"+item.wxName+"</div>                            "
					    			+"                     <div class='bcen_block_fonts over' id='content_"+item.openId+"'>"+(item.lastMsgContext==null?'':item.lastMsgContext)+"</div>	                "
					    			+"           </div>                                                                        "
					    			+"           <div class='bcen_block_right fr'>                                             "
					    			+"                     <div class='bcen_block_time'>"+pagDate(item.lastMsgDateTime)+"</div>                             "
					    			+"           </div> "
		    						+" </div>";
				    				$("#userMsgList").append(templ);
		    					}else{
		    						$("#content_"+item.openId).html(item.lastMsgContext);
		    						if(item.isNewMsg==1){
		    							if($("#msgNew_"+item.openId).hasClass("bcen_block_dian")){
			    							$("#msgNew_"+item.openId).css('display',''); 
		    							}else{
			    							$("#msgNew_"+item.openId).addClass("bcen_block_dian"); // 追加样式
		    							}
		    						}
		    						
		    						openIdVal.attr("index",index);
		    					}
		    				}
		    				
		    				initUserDataAct();
		    				var $trs = $('#userMsgList .bcen_block');
		    				$trs.sort(function(a,b){
		    				    var valveNumOfa = $(a).attr("index");
		    				    var valveNumOfb = $(b).attr("index");
		    				    if(parseInt(valveNumOfa) > parseInt(valveNumOfb)) return 1;
		    				    else return -1;
		    				});
		    				$trs.detach().appendTo('#userMsgList');
		    			}
	    			 	userMsgListLoading = false;
				    	$("#bcenBoxLoading").remove();
		    		}
		    	})
			}
			
			var userMsgDetailInterval;
			var currentMsgOpenId;
			function initUserDataAct(){
				$(".bcen_block").mouseenter(function (){
					$(this).not(".bcen_background").css("background-color","#ddd");	
				})
				$(".bcen_block").mouseleave(function (){
					$(this).not(".bcen_background").css("background-color","#e7e8e9");	
				})
				
			}
			function go2MsgDetail(openId){
				$("#"+openId).removeAttr("style");
				$("#"+openId).addClass("bcen_background");
				$(".bcen_block").not("#"+openId).removeClass("bcen_background");
				$("#"+openId).find(".bcen_block_dian").css("display","none");
				$("#"+openId).find(".bcen_block_nums").css("display","none");
				var openId = $("#"+openId).attr("id");
				$("#sendMsgId").attr("openId",openId);
				$("#userMsgDetailTitle").html($("#wxName_"+openId).html());
				currentMsgOpenId = openId;
				getUserMsgDetailList();
				updateNewMsg2Read();
				$("#msgWriteContent").focus();
			}
			window.setInterval(quartzUserMsgList,5000);
			window.setInterval(quartzGetUserMsgDetailList,3000);
			</script>
			
			 <script>
                      initUserListSyle();	
                      
                      function initUserListSyle(){
                       var image_widths=$(".bcen_block_face").width();
                           var image_heights=$(".bcen_block_face").height();
                           var image_bis=image_widths/image_heights;
                           $(".bcen_block_face img").each(function (){
                                     var images_widths=$(this).width();
                                     var images_heights=$(this).height();
                                     var images_bis=images_widths/images_heights;
                                     if(image_bis<images_bis){
                                               $(this).css("height",image_heights);
                                               var imgwidths=image_heights * images_bis;
                                               var widthss= 0 - (imgwidths - image_widths) / 2;
                                               $(this).css("margin-left",widthss);
                                     }else{
                                               $(this).css("width","100%");	
                                               var imgheights=image_widths / images_bis;	
                                               var heightss=0 - (imgheights - image_heights) / 2;
                                               $(this).css("margin-top",heightss);
                                     }
                           })
                      }
                      
                      
                      
            </script>	

  <div id="outerdiv" style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:2;width:100%;height:100%;display:none;">
  <div id="innerdiv" style="position:absolute;">
    <img id="bigimg" style="border:5px solid #fff;" src="" />
  </div>    
<script type="text/javascript" src="common/js/adapter-latest.js"></script>
<script type="text/javascript" src="common/js/AudioRecorder.js"></script>
<script>
function res(){
	var winds = $(window).height();
	var windw = $(window).width();
	//最左边高度
	$(".buju_left").height(winds);
	//中间高度
	$(".buju_center").height(winds-5);
	//右边高度
	$(".buju_right").height(winds-50);
	$(".yinyings").height(winds-5);
	$(".buju_install").height(winds - 61);
	$(".bcen_box").height(winds - 62);
	$(".buju_right").width(windw - 310);
	$(".bri_bottom").width(windw - 310);
	$(".brce_block_box").width(windw - 310);
	$(".buju_right textarea").width(windw - 360);
	$(".bri_center").height(winds - 205);
	$(".brce_block_box").height(winds - 235);
}

	$(function (){
		res();
		scrolls();
		chatscr();
		$(window).resize(function(){
			res();
			scrolls();
			chatscr();	
		})

		initUserDataAct();
		
		$(".bri_bottom textarea").focus(function (){
			$(".bri_bottom").css("background-color","#fff");
		})
		$(".bri_bottom textarea").blur(function (){
			$(".bri_bottom").css("background-color","#f5f5f5");
		})
		
		$(".bri_bottom_face").click(function (){	
			$(".bri_bottom_look").css("display","block");	
		})
		
		$(document).bind("click",function(e){
			//id为menu的是菜单，id为open的是打开菜单的按钮        
			if($(e.target).closest(".bri_bottom_look").length == 0 && $(e.target).closest(".bri_bottom_face").length == 0){
				//点击id为menu之外且id不是不是open，则触发
				$(".bri_bottom_look").css("display","none");
			}
        		})
		
		$(".bri_bottom_file").click(function (){
			$(".bri_bottom_files").click();	
		})
		
		$(".bri_bottom_but").click(function (){
			var chats = $("#msgWriteContent").html();
			console.log(chats);
			var apps_one = "<div class='brce_right'><div class='brce_left_face fr'><img src='common/img/face001.jpg'/></div><div class='brce_left_talk fr'><div class='brce_rt_jiao fr'></div><div class='brce_rt_block fr'><div>";
			var apps_two = "</div></div></div><div class='clear'></div></div>";
			if(chats == ""){
				$(".bri_bottom_empty").css("display","block");
				setTimeout(function () {$(".bri_bottom_empty").hide();}, 2000)
			}else{
				sendMsg($("#sendMsgId").attr("openId"),chats);
				$(".bri_bottom textarea").val("");
			}
			$("#msgWriteContent").html("");
		})
		
		$(".bri_top_more").click(function (){
			var bl = $(".buju_install").css("display");
			if(bl == "block"){
				$(".buju_install").css("display","none");	
			}else if(bl == "none"){
				$(".buju_install").css("display","block");	
			}	
		})
		
		$(".bin_two_but").click(function (){
			var imgs = $(this).find("img").attr("src");
			imgs = imgs.replace("common/img/","");
			imgs = imgs.replace(".png","");
			if(imgs == "butclose"){
				$(this).find("img").attr("src","common/img/butopen.png");		
			}else if(imgs == "butopen"){
				$(this).find("img").attr("src","common/img/butclose.png");			
			}	
		})
		
		$(".bin_three input").focus(function (){
			$(".bin_three").css("background-color","#fff");
		})
		
		$(".bin_three input").keyup(function (){
			var val = $(this).val();
			if(val == ""){
				$(".bin_three_close").css("display","none");
				$(".bin_three_seach").css("display","block");
			}else if(val != ""){
				$(".bin_three_close").css("display","block");
				$(".bin_three_seach").css("display","none");
			}
		})
		
		$(".bin_three_close").click(function (){
			$(".bin_three input").val("");
			$(".bin_three_close").css("display","none");
			$(".bin_three_seach").css("display","block");
			$(".bin_three").css("background-color","#f5f5f5");
		})
		
		$(".bin_four_more").click(function (){
			var hts = $(this).find("label").html();	
			if(hts == "查看更多群成员"){
				$(this).find("label").html("收起群成员");	
				$(this).find("img").attr("src","common/img/up001.png");	
			}else if(hts == "收起群成员"){
				$(this).find("label").html("查看更多群成员");	
				$(this).find("img").attr("src","common/img/down001.png");	
			}
		})
		
		$(".bin_five_text").mouseenter(function (){
			$(this).next("img").css("display","block");	
		})
		
		$(".bin_five_text").mouseleave(function (){
			$(this).next("img").css("display","none");	
		})
		
		$(".bin_five_text").click(function (){
			$(this).next("input").attr("type","text");
			$(this).css("display","none");
			$(this).next("input").focus();
		})
		
		$(".bin_five_text").next("input").blur(function (){
			var bin = $(this).val();
			if(bin != ""){
				$(this).prev(".bin_five_text").html(bin);
			}
			$(this).prev(".bin_five_text").css("display","block");
			$(this).attr("type","hidden");
		})
		
		$(".bin_five_text").eq(0).next("input").blur(function (){
			var bin = $(this).val();
			$(".bri_top_font").html(bin);
		})
		
		$(".bin_five_butname").click(function (){
			var imgs = $(this).attr("src");
			imgs = imgs.replace("common/img/","");
			imgs = imgs.replace(".png","");
			if(imgs == "butclose"){
				$(this).attr("src","common/img/butopen.png");
				$(".brce_lt_name").css("display","block");		
			}else if(imgs == "butopen"){
				$(this).attr("src","common/img/butclose.png");
				$(".brce_lt_name").css("display","none");			
			}	
		})
		
		$(".bin_five_but").click(function (){
			var imgs = $(this).attr("src");
			imgs = imgs.replace("common/img/","");
			imgs = imgs.replace(".png","");
			if(imgs == "butclose"){
				$(this).attr("src","common/img/butopen.png");	
			}else if(imgs == "butopen"){
				$(this).attr("src","common/img/butclose.png");		
			}	
		})
		
		$(".bin_five_note").click(function (){
			$(".bin_note").css("display","block");	
		})
		
		$(".bin_note_close").click(function (){
			$(".bin_note").css("display","none");	
		})
		$('.bcen_box').scroll(function(){
			if (!userMsgListLoading){
			    var msg_list = $('.bcen_box');
		        if (msg_list.height() + msg_list[0].scrollTop >= msg_list[0].scrollHeight - 60 ) {
		        	msg_list.append('<div id="bcenBoxLoading">数据正在加载中...</div>');
		        	var pageNum = $("#pageNum").val();
		        	pageNum = parseInt(pageNum);
		        	pageNum++;
					$("#pageNum").val(pageNum);
					userMsgListLoading=true;
		        }
	        }
	    })
	})
	
	
	  $(function() {
		  $("#sendVoiceMsgId").click(function() {
	          $(".masking").fadeIn();
	          resetSendVoice();
	      })
	      $(".close").click(function(event) {
	          event.preventDefault;
	          $(".masking").hide();
	      });
	      $(".masking").click(function(event) {
	          //event.preventDefault;
	          //$(this).hide();
	      });
	      $("#btn-start-recording").click(function() {
	    	  startVoiceRecord();
	      });
	      $("#btn-stop-recording").click(function() {
	    	  stopVoiceRecord();
	      });
	      $("#btn-cancleRecord").click(function() {
	    	  $("#btn-stop-recording").click();
	    	  resetSendVoice();
	    	  cancleVoice();
	      });
	      $("#btn-sendRecord").click(function() {
	    	  resetSendVoice();
	      })
	  })
	  
    function startVoiceRecord(){
		$("#voiceImgId").attr("src","common/img/voiceRecord.gif");
		$("#btn-start-recording").css("display","none");
		$("#btn-stop-recording").css("display","");
		$("#btn-sendRecord").css("display","none");
	}
	
	function stopVoiceRecord(){
		$("#voiceImgId").attr("src","common/img/timg.jpg");
		$("#btn-start-recording").css("display","none");
		$("#btn-stop-recording").css("display","none");
		$("#btn-sendRecord").css("display","");
	}
	
	function sendVoiceRecord(){
		$("#voiceImgId").attr("src","common/img/voicePrepar.jpg");
		$("#btn-start-recording").css("display","");
		$("#btn-stop-recording").css("display","none");
		$("#btn-sendRecord").css("display","none");
	}
	
	function resetSendVoice(){
		$("#voiceImgId").attr("src","common/img/voicePrepar.jpg");
		$("#btn-start-recording").css("display","");
		$("#btn-stop-recording").css("display","none");
		$("#btn-sendRecord").css("display","none");
		
		var script = document.createElement("script");//重新加载
		script.src = "common/js/AudioRecorder.js";
		document.body.appendChild(script);
	}
	
	function cancleVoice(){
		recorder.stop;
		var script = document.createElement("script");//重新加载
		script.src = "common/js/AudioRecorder.js";
		document.body.appendChild(script);
	}
	
	
	var userMsgListLoading = false;
	
	function searchWxUser(){
		
	}
	
	function defaultCheckUserOpenId(){
		var default_check_user_openId = "${default_check_user_openId}";
		if(default_check_user_openId!=null&&default_check_user_openId!=''){
			$("#"+default_check_user_openId).addClass('bcen_background');
			go2MsgDetail(default_check_user_openId);
		}
	}
	
	
</script>

<script type="text/javascript">

	function imgReDownLoad(_this){
		$.ajax({
    		url:'reDownloadImg.html',
    		type:'get',
    		data:'imgName='+_this.attr("id"),
    		success:function(rs){
    			if(rs!=null&&rs==1){
    				_this.attr("src", "${pageContext.request.contextPath}/cloud/download.html?fileName="+_this.attr("id")+"_abbr&fileType=images&suffix="+_this.attr("formatType"));
    				_this.attr("imgorgsrc", "${pageContext.request.contextPath}/cloud/download.html?fileName="+_this.attr("id")+"&fileType=images&suffix="+_this.attr("formatType"));
    				_this.click(function(){
    					displayOrgImg($(this));
   					});
    			}
    		}
    	})
	}
	
  function displayOrgImg(_this){
	  imgShow("#outerdiv", "#innerdiv", "#bigimg", _this); 
  } 
  function imgShow(outerdiv, innerdiv, bigimg, _this){
    var src = _this.attr("imgorgsrc");//获取当前点击的pimg元素中的src属性 
    $(bigimg).attr("src", src);//设置#bigimg元素的src属性 
      /*获取当前点击图片的真实大小，并显示弹出层及大图*/ 
    $("<img/>").attr("src", src).load(function(){ 
      var windowW = $(window).width();//获取当前窗口宽度 
      var windowH = $(window).height();//获取当前窗口高度 
      var realWidth = this.width;//获取图片真实宽度 
      var realHeight = this.height;//获取图片真实高度 
      var imgWidth, imgHeight; 
      var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放 
      if(realHeight>windowH*scale) {//判断图片高度 
        imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放 
        imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度 
        if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度 
          imgWidth = windowW*scale;//再对宽度进行缩放 
        } 
      } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度 
        imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放 
              imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度 
      } else {//如果图片真实高度和宽度都符合要求，高宽不变 
        imgWidth = realWidth; 
        imgHeight = realHeight; 
      } 
          $(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放 
      var w = (windowW-imgWidth)/2;//计算图片与窗口左边距 
      var h = (windowH-imgHeight)/2;//计算图片与窗口上边距 
      $(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性 
      $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg 
    }); 
    $(outerdiv).click(function(){//再次点击淡出消失弹出层 
      $(this).fadeOut("fast"); 
    }); 
  }
  
  
  function uploadImg(){
	  var fileData = $(".bri_bottom_files")[0].files[0];
	  var fileSuff = $(".bri_bottom_files").attr("accept").replaceAll(".","").split(",");
	  var fileTrue = false;
	  for (const elem of fileSuff) {
		  if(fileData.type.indexOf(elem)!=-1){
			  fileTrue = true;
			  break;
		  }
	  }
	  if(fileTrue==false){
		  alert("上传中断，上传的文件不合法，请重新按照格式上传!");
		  return ;
	  }
	  var formData = new FormData();    
		formData.append("file",fileData);
		formData.append("openId",currentMsgOpenId);
		formData.append("uuid",uuid);
		$.ajax({
			url:'/customerservice/uploadImg.html',
			type:'post',
			data:formData,
			processData: false,  
			contentType: false
		})
　　　　 $(".bri_bottom_files").val("");
  }
</script>


</body></html>