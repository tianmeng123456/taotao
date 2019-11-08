package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EasyUITreeNode;
import com.taotao.portal.service.ContentService;
import com.taotao.utils.E3Result;

/*
 * 内容分类管理的Controller
 */
@Controller
public class ContentCategoryController {
	@Autowired
	private ContentService contentService;

	/**
	 * 查询内容分类管理
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> queryContextList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		List<EasyUITreeNode> list = contentService.queryContentList(parentId);
		return list;
	}

	/**
	 * 新增内容分类管理
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContent(Long parentId, String name) {
		E3Result e3 = contentService.addContent(parentId, name);
		return e3;
	}
	/**
	 * 修改内容分类名称
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result editContent(Long id, String name) {
		E3Result e3 = contentService.updateContentNodeById(id, name);
		return e3;
	}

	/**
	 * 删除内容分类管理
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContent(Long id) {
		E3Result e3 = contentService.deleteContentNodeById(id);
		return e3;
	}

}
