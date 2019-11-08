package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import javax.jws.WebParam.Mode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.pojo.SearchItem;
import com.taotao.search.service.SearchItemService;
import com.taotao.utils.SearchResult;
@Controller
public class SearchController {
	@Autowired
	private SearchItemService searchItemService;
	
	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	@RequestMapping("/search")
	public String seach(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		//需要转码
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		//调用索引服务
		SearchResult searchQuery = searchItemService.searchQuery(keyword, page, PAGE_ROWS);
	
		//把结果传递到jsp页面
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchQuery.getTotalPages());
		model.addAttribute("itemList", searchQuery.getItemList());
		model.addAttribute("recourdCount", searchQuery.getRecourdCount());
		model.addAttribute("page", page);
		return "search";
	}

}
