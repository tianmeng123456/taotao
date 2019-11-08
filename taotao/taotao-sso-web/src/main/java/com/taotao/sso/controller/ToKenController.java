package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.sso.service.ToKenService;
import com.taotao.utils.E3Result;

@Controller
public class ToKenController {
	@Autowired
	private ToKenService toKenService;
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public E3Result getUserByToken(@PathVariable String token) {
		E3Result result = toKenService.userByToken(token);
		return result;
	}
}
