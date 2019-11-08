package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import com.taotao.utils.E3Result;

/**
 * 内容的Contorller
 * @author 田猛
 *
 */
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	/**
	 * 分页查询内容对象
	 * @return 分页对象
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult queryContentList() {
		EasyUIDataGridResult eg = contentService.selectContentList();
		return eg;
	}
	/**
	 * 保存内容方法
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result saveContextByContent(TbContent tbContent) {
		E3Result eg = contentService.saveContentByContent(tbContent);
		return eg;
	}
	
	
	/**
	 * 修改内容的方法
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3Result editSaveContextByContent(TbContent tbContent) {
		E3Result eg = contentService.editSaveContentByContent(tbContent);
		return eg;
	}
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result editSaveContextByContent(Long ids) {
		E3Result eg = contentService.deleteContentByContent(ids);
		return eg;
	}
}
