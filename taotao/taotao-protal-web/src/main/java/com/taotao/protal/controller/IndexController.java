package com.taotao.protal.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.jedis.JedisClient;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import com.taotao.utils.JsonUtils;

/**
 * 首页展示Contorller
 * 
 * @author 田猛
 *
 */
@Controller
public class IndexController {
	// 首页轮播图的id
	@Value("${ADRotation}")
	private Long ADRotation;
	
	//内容的Service
	@Autowired
	private ContentService contentService;
	
	/**
	 * 展示首页
	 */
	@RequestMapping("/index")
	public String showIndex(Model model) {
		// 查询后台前台需要显示的广告图片
		List<TbContent> list = contentService.getContentListByIds(ADRotation);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
