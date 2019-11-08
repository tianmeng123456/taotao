package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.search.service.SearchItemService;
import com.taotao.utils.E3Result;
/**
 * 索引维护的Controller
 * @author 田猛
 *
 */

@Controller
public class SolrController {
	@Autowired
	private SearchItemService searchItemService;
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemIndex() {
		E3Result result = searchItemService.importAllItems();
		return result;
	}
	
}
