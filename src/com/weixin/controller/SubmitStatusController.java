package com.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class SubmitStatusController {
	
	@ResponseBody
	@RequestMapping(value = "page/go2SubResult.html")
	public ModelAndView bindingDetail(int operResult,String returnURL,String operType,String errorMsg,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		ml.addObject("operResult", operResult);
		ml.addObject("returnURL", returnURL);
		ml.addObject("operType", operType);
		ml.addObject("errorMsg", errorMsg);
		ml.setViewName("page/resultPage");
		return ml;
	}
}
