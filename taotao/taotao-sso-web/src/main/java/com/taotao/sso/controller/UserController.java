package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbUser;
import com.taotao.sso.service.RegisterService;
import com.taotao.utils.E3Result;
/**
 * 用户功能处理表现层
 * @author 田猛
 *
 */
@Controller
public class UserController {
	@Autowired
	private RegisterService registerServer;
	
	/**
	 * 校验用户信息
	 * @param param 用户的某个信息
	 * @param type 1是用户名，2是手机号，3是邮箱
	 * @return E3Result
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result changeDate(@PathVariable("param") String param,@PathVariable("type") Integer type) {
		System.out.println(param+type);
		E3Result e3Result = registerServer.changeUserMassage(param, type);
		return e3Result;
	}
	
	/**
	 * 用户注册功能
	 * @param tbUser 用户实体类
	 * @return E3Result
	 */
	@RequestMapping(value = "/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser tbUser) {
		E3Result e3 = registerServer.register(tbUser);
		return e3;
	}
}
