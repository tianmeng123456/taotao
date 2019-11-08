package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 登录功能
 * @author 田猛
 *
 */
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.sso.service.LoginService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.E3Result;
@Controller
public class LoginController {
	@Autowired
	private LoginService loginServer;
	@Value("COOKIE_TOKEN_KEY")
	private String COOKIE_TOKEN_KEY;
	/**
	 * 展示登录页面
	 * @param pathString
	 * @return
	 */
	@RequestMapping("/page/{path}")
	public String showLogin(@PathVariable("path") String pathString) {
		return pathString;
	}
	@RequestMapping(value = "/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		//调用查询方法查询
		E3Result login = loginServer.login(username, password);
		//返回结果中取出token，写入cookie，cookie要跨域
		String token = login.getData().toString();
		CookieUtils.setCookie(request, response,COOKIE_TOKEN_KEY , token);
		return login;
	}
}
