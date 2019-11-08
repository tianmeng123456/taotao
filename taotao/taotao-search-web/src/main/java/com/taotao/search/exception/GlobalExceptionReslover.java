package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {
	Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// 写日志文件
		logger.error("系统发生异常", ex);
		//发邮件、发送短信
		//Jmail:可以查找相关资料
		//需要在购买短信。调用第三方接口即可
		//展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("massage","系统发生异常");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
