package com.hq.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class TestSessionCtrl {

	@RequestMapping(value="/test/testSessionIndex.html")
	public String index(HttpServletRequest request) {
		request.getSession().setAttribute("name", "zhagnsan");
		System.out.println("-----index------------sessionid" + request.getSession().getId());
		return "index";
	}

	@RequestMapping(value="/test/testSessiongetName.html")
	public String getName(HttpServletRequest request) {
		System.out.println("----getName-------------sessionid======" + request.getSession().getId());

		return (String) request.getSession().getAttribute("name");
	}
}
