<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="top.jsp"/>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="common/css/common.css">
<link rel="stylesheet" type="text/css" href="common/css/style.css">
<script type="text/javascript" src="common/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="common/js/seach.js"></script>
<script type="text/javascript" src="common/js/scrolls.js"></script>
<script type="text/javascript" src="common/js/addfriends.js"></script>
<style>
::-webkit-scrollbar{width:0px;}
.bri_deb_one {
    width: 98px;
    height: 22px;
    line-height: 22px;
    font-size: 14px;
    color: #999;
    margin-bottom: 12px;
}
.bri_deb_two {
    width: 400px;
    height: 51px;
    line-height: 22px;
    font-size: 14px;
    margin-left: 12px;
}

.bri_detail_one {
    width: 512px;
    height: 105px;
    margin: auto;
    border-bottom: 1px #e7e7e7 solid;
    margin-top: 96px;
}

.bri_deb_name {
    max-width: 400px;
    font-size: 20px;
    line-height: 28px;
}

.bri_detail_two {
    width: 512px;
    height: 304px;
    margin: auto;
    border-bottom: 1px #e7e7e7 solid;
    margin-top: 32px;
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
</style><script>
	$(function (){
		initBindingAct();
	})
	
	function initBindingAct(){
		res();
		scrolls();
		$(window).resize(function(){
			res();
			scrolls();	
		})
		
		function res(){
			var winds = $(window).height();
			var windw = $(window).width();
			//最左边高度
			$(".buju_left").height(winds);
			//中间高度
			$(".buju_center").height(winds);
			//右边高度
			$(".buju_right").height(winds);
			$(".yinyings").height(winds);
			$(".bcen_box").height(winds - 62);
			$(".buju_right").width(windw - 310);
			$(".bri_bottom").width(windw - 310);
		}
		
		$(".bcen_friends").mouseenter(function (){
			$(this).not(".bcen_background").css("background-color","#ddd");	
		})
		$(".bcen_friends").mouseleave(function (){
			$(this).not(".bcen_background").css("background-color","#e7e8e9");	
		})
		
		$(".bcen_friends").click(function (){
			var realname = $(this).attr("realname");
			var mphone = $(this).attr("mphone");
			var custRemark = $(this).attr("custRemark");
			var add_time = $(this).attr("add_time");
			var head_img = $(this).attr("head_img");
			var area_name = $(this).attr("area_name");
			var custName = $(this).attr("custName");
			var openId = $(this).attr("openId");
			
			$(".bri_deb_input").val(custRemark);
			$("#wxName").html(realname);
			$("#custName").html(custName);
			$("#sysInRemark").html(custRemark);
			$(".bri_deb_name").html(custRemark==null||custRemark==""?(custName==null||custName==""?realname:custName):custRemark);
			$("#phoneNum").html(mphone);
			$("#areaName").html(area_name);
			$("#addTime").html(add_time);
			$("#sendMsgLink").attr("href","${pageContext.request.contextPath }/customerservice/sendNewMsg.html?openId="+openId);
			$("#userHeadImgId").attr("src",head_img);
			$(this).removeAttr("style");
			$(this).addClass("bcen_background");
			$(".bcen_friends").not(this).removeClass("bcen_background");
			if(!$(this).hasClass("bcen_new")){
				$(".bri_detail").css("display","block");
				$(".bri_top").css("display","none");		
			}else{
				$(".bri_detail").css("display","none");	
				$(".bri_top").css("display","block");		
			}	
			/*bcen_new*/
		})
		
	}
</script></head>


<body style="">
	<div class="buju">
          	<!-- 最左侧功能图标 -->
          	<div class="buju_left fl" style="height: 903px;">
                    	<div class="bleft_face fl">
                              	<img src="${customerLoginUserData.head_img }" style="width: 100%; margin-top: 0px;">		
                              </div>
                              <div class="bleft_iconone fl">
                              	<a href="index.jsp"><img src="common/img/chat010.png" width="100%"></a>
                              </div>
                              <!--   <div class="bleft_iconnum">1</div> -->
                              <div class="bleft_iconone fl">
                              	<a href="${pageContext.request.contextPath }/customerservice/go2Friends.html"><img src="common/img/chat021.png" width="100%"></a>
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
				
				$(".bri_deb_two").eq(2).click(function (){
					$(this).removeClass("bri_deb_tip");
					$(this).find("label").css("display","none");
					$(".bri_deb_input").attr("type","text");
					$(".bri_deb_input").focus();
				})
				$(".bri_deb_input").blur(function (){
					var inputs = $(this).val();
					$(".bri_deb_two").eq(2).find("label").css("display","block");
					if(inputs != ""){
						$(".bri_deb_two").eq(2).css("width","fit-content");
						$(".bri_deb_two").eq(2).css("padding-right","2px");
						$(".bri_deb_two").eq(2).find("label").html(inputs);
					}else{
						$(".bri_deb_two").eq(2).find("label").html("点击添加备注");	
					}
					$(".bri_deb_two").eq(2).addClass("bri_deb_tip");
					
					$(".bri_deb_input").attr("type","hidden");
					$(".bri_deb_input").focus();
				})	
			})				
		</script>
                    <!-- 最左侧功能图标 -->
                    <!-- 中间功能列表-->
                    <div class="buju_center fl" style="height: 903px;">
                    	<div class="bcen_seach fl">
                              	<div class="bcen_seach_font fl">搜索</div>
                              	<input type="text" style="display:none;" class="fl" id="searchInputVal">
                                        <div class="bcen_seach_close fr" style="display:none;"><img src="common/img/close.png"></div>
                                        <div class="bcen_seach_but fr">
                                        	<img src="common/img/seach.png" width="100%">
                                        </div>
                              </div>
                              <!-- <a href="javascript:void(0);" onclick="addfris()">
                                        <div class="bcen_add fl">
                                                  <img src="common/img/add001.png">	
                                        </div>
                              </a> -->
                              <div class="bcen_scroll" style="position: absolute; right: 0px; top: 62px; display: none; height: 771px;"></div>
                              <div class="clear"></div>
                              <div class="bcen_box" style="height: 841px;">
                                        <div class="bcen_jilu">
                                                  <div class="bcen_first">新的朋友</div>
                                                  <div class="bcen_friends bcen_background bcen_new" style="border-bottom:1px #dcdbda solid;">
                                                            <div class="bcen_friends_face fl">
                                                                      <img src="common/img/newfriend.jpg" style="width: 100%; margin-top: 0px;">
                                                            </div>
                                                            <div class="bcen_friends_font fl">新的朋友</div>
                                                  </div>
                                                  <div id="userList">
                                                 
                                       			  </div>
                                                  <script>
                                                  
                                                            $(function (){
                                                                      var image_widths=$(".bcen_friends_face").width();
                                                                      var image_heights=$(".bcen_friends_face").height();
                                                                      var image_bis=image_widths/image_heights;
                                                                      $(".bcen_friends_face img").each(function (){
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
                                                            })	
                                                            
                                                            
                                                            function initUserMsgList(){
																$.ajax({
														    		url:'allUserList.html',
														    		type:'get',
														    		data:'wxName='+$("#searchInputVal").val(),
														    		success:function(rs){
														    			var data = eval('('+rs+')');
														    			$("#userList").empty();
														    			if(data!=null){
														    				var templ = "";
														    				$.each(data,function(key,value){
																    				templ += " <div class='bcen_first'>"+key+"</div>";
																    				for(var index in value){
																    					var userObjItem = value[index];
																    					var realname = (userObjItem.realname==null?'':userObjItem.realname);
																    					var mphone = (userObjItem.mphone==null?'':userObjItem.mphone);
																    					var area_name = (userObjItem.area_name==null?'':userObjItem.area_name);
																    					var custRemark = (userObjItem.custRemark==null?'':userObjItem.custRemark);
																    					var custName = (userObjItem.custName==null?'':userObjItem.custName);
																    					var add_time = (userObjItem.add_time==null?'':userObjItem.add_time);
																    					var head_img = (userObjItem.head_img==null?'':userObjItem.head_img);
																    					var showName = (custRemark==null||custRemark==""?(custName==null||custName==""?realname:custName):custRemark);
																    					templ +="     <div class='bcen_friends' openId='"+userObjItem.oppen_id+"' realname='"+realname+"' mphone='"+mphone+"' area_name='"+area_name+"' custRemark='"+custRemark+"' custName='"+custName+"' add_time='"+add_time+"' head_img='"+head_img+"'>"
																    					templ +="               <div class='bcen_friends_face fl'>";
																    					templ +="                               <img src='"+head_img+"' style='width: 100%; margin-top: 0px;'>";
																    					templ +="                      </div>";
															    						templ +="                    <div class='bcen_friends_font fl' style='max-width: 185px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;' title='"+showName+"'>"+showName+"</div>";
													    								templ +="           </div>";
																    				}
																    				templ +="      <div class='bcen_borders'></div>";
													    		            });
														    				$("#userList").html(templ);
														    				initBindingAct();
														    			}
														    		}
														    	})
															}
                                                            
                                                            initUserMsgList();
                                                  </script>	
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
                                                  <div class="bsea_first">群聊</div>
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
                                                  </div>
                                        
                                        </div>
                                        <script>
                                                            $(function (){
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
                                                            })				
                                                  </script>	
                                        
                              </div>
		</div>
		<!-- 中间功能列表-->
                    <!-- 右边好友详情 -->
                    <div class="buju_right fl" style="height: 903px; width: 1610px;">
                    	<div class="bri_top">
                              	<div class="bri_top_font fl">
                                        	新的朋友
                                        </div>
                                        <a href="javascript:void(0);">
                                        	<img src="common/img/more.png" class="bri_top_more fr">
                              	</a>
                                        <div class="clear"></div>
                              </div>
                              <div class="bri_detail" style="display:none;">
                              	<div class="bri_detail_one">
                                        	<div class="bri_deb_left fl">
                                                            <div class="bri_deb_name fl over"></div>
                                                            <div class="bri_deb_sex fl">
                                                                      <img src="common/img/man.jpg">
                                                            </div>
                                                            <div class="bri_deb_sign fl"></div>
                                                            <div class="clear"></div>
                                                  </div>
                                                  <div class="bri_deb_face fr">
                                                  	<img id="userHeadImgId" src="common/img/face005.jpg" style="width: 100%; margin-top: 0px;">
                                                  </div>
                                        </div>	
                                        <div class="bri_detail_two">
                                        		  <div class="bri_deb_one fl">微信名称</div>
                                                  <div class="bri_deb_two fl" id="wxName"></div>
                                                  <div class="clear"></div>
                                                  <div class="bri_deb_one fl">内部系统名称</div>
                                                  <div class="bri_deb_two fl" id="custName"></div>
                                                  <div class="clear"></div>
                                        		  <div class="bri_deb_one fl">内部系统备注</div>
                                                  <div class="bri_deb_two bri_deb_tip fl">
                                                  	<label id="sysInRemark">点击添加备注</label>
                                                  	<input type="hidden" class="bri_deb_input">
												  </div>
                                                  <div class="clear"></div>
                                                  <div class="bri_deb_one fl">电话</div>
                                                  <div class="bri_deb_two fl" id="phoneNum"></div>
                                                  <div class="clear"></div>
                                                  <div class="bri_deb_one fl">地区</div>
                                                  <div class="bri_deb_two fl" id="areaName"></div>
                                                  <div class="clear"></div>
                                                  <div class="bri_deb_one fl">关注时间</div>
                                                  <div class="bri_deb_two fl" id="addTime"></div>
                                        </div>
                                        <a href="#" id="sendMsgLink"><div class="bri_detail_but">发消息</div></a>
                              </div>
                              <script>
				$(function (){
					var img_width = $(".bri_deb_face").width();
					var img_height = $(".bri_deb_face").height();
					var img_bi=img_width/img_height;
					$(".bri_deb_face img").each(function (){
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
				})				
			</script>	
		</div>
                    <!-- 右边好友详情 -->
                    
                    
                    <!-- 添加弹框 -->
                    <div class="yinyings" style="display: none; height: 903px;"></div>
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
	



</body></html>