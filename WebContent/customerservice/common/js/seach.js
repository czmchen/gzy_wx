// JavaScript Document
//搜索框
$(function (){
	$(".bcen_seach_font").click(function (){
		$(this).css("display","none");
		$(this).next().css("display","block");	
		$(this).next().focus();
		$(".bcen_seach").css("background-color","#f5f2f1");
	})
	$(".bcen_seach").find(":text").blur(function (){
		var texts = $(this).val();
		$("#searchInputVal").val(texts);
		initUserMsgList();
	})
	$(".bcen_seach").find(":text").keyup(function (){
		var seachs = $(this).val();
		if(seachs != ""){
			$(".bcen_seach_close").css("display","block");
		}else{
			$(".bcen_seach_close").css("display","none");
		}
		$("#searchInputVal").val(seachs);
		initUserMsgList();
	})
	$("body").on("click",".bcen_seach_close",function(){
		$(".bcen_seach").find(":text").val("");
		$(".bcen_seach").find(":text").css("display","none");
		$(".bcen_seach_font").css("display","block");
		$(".bcen_seach").css("background-color","#e5e3e2");
		$(".bcen_seach_close").css("display","none");
		$(".bcen_seach_but").css("display","block");
		$("#searchInputVal").val('');
		initUserMsgList();
	})
	
	
	//添加好友搜索
	$(".ba_left_seach").find(":text").keyup(function (){
		var seachs = $(this).val();
		if(seachs != ""){
			$(".ba_lefts_close").css("display","block");
			$(".ba_lefts_seach").css("display","none");	
		}else{
			$(".ba_lefts_close").css("display","none");
			$(".ba_lefts_seach").css("display","block");	
		}
	})
	$("body").on("click",".ba_lefts_close",function(){
		$(".ba_left_seach").find(":text").val("");
		$(".ba_lefts_close").css("display","none");
		$(".ba_lefts_seach").css("display","block");
	})
	
})