package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable("itemId") Long itemId,Model model) {
		TbItem itemById = itemService.getItemById(itemId);
		TbItemDesc itemDescById = itemService.getItemDescById(itemId);
		Item item = new Item(itemById);
		model.addAttribute("item",item);
		model.addAttribute("itemDesc", itemDescById);
		return "item";
	}
}
