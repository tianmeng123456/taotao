package com.taotao.cart.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.pojo.TbUser;
import com.taotao.sso.service.ToKenService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.E3Result;
/**
 * 判断用户是否登录的拦截器
 * @author 田猛
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Value("COOKIE_TOKEN_KEY")
	private String COOKIE_TOKEN_KEY;
	@Autowired
	private ToKenService toKenService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//执行handler之前执行此方法
		//从cookie中去token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//查看token是否为空
		if(!StringUtils.isNotBlank(token)) {
			//是空根据直接方行
			return true;
		}
		//取得token从Redis中查询该用户信息
		E3Result result = toKenService.userByToken(token);
		if(result.getStatus() != 200) {
			return true;
		}
		TbUser tbUser = (TbUser) result.getData();
		//把用户信息放到request中。只需要在Controller中判断request中是否包含user。方行
		request.setAttribute("user", tbUser);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//执行handler之后返回ModelAndView之前
		
			
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后执行
		
	}

}
