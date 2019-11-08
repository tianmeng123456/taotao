package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	/*
	 * 展示首页
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	/*
	 * 展示其他页面
	 */
	@RequestMapping("/{page}")
	public String showItemList(@PathVariable String page) {
		return page;
	}
	/*
	 * 跳转到商品修改页面
	 */
	@RequestMapping("/rest/page/{page}")
	public String showEditPage(@PathVariable String page) {
		return page;
	}
}
