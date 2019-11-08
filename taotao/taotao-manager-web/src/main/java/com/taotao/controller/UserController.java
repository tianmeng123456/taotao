package com.taotao.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.sso.service.ToKenService;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;
/**
 * ajax访问用户信息功能
 * @author 田猛
 *
 */
@Controller
public class UserController {
	@Autowired
	private ToKenService toKenService;
	@RequestMapping(value = "/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) {
		E3Result result = toKenService.userByToken(token);
		//是否为jsonp请求
		if(StringUtils.isNotBlank(callback)) {
			String strResult = callback+"("+JsonUtils.objectToJson(result)+");";
			return strResult;
		}
		return JsonUtils.objectToJson(result);
	}
}
