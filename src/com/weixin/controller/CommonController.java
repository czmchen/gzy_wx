package com.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yq.entity.User;
import com.yq.service.UserService;

@Controller
@RequestMapping
public class CommonController {
	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "page/code2Order.html")
	public ModelAndView bindingDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		Object openId = session.getAttribute("oppen_id");
		if (openId != null) {
			User user = userService.getUserByOpenId((String)openId);
			if(user!=null) {
				ml.addObject("isWXFocus", user.getIsWXFocus());
			}
		}
		ml.setViewName("page/welcome");
		return ml;
	}
}
