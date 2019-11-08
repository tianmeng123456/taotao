package com.taotao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import com.taotao.utils.E3Result;
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/list")
	@ResponseBody
	/*
	 * 分页查询商品
	 */
	public EasyUIDataGridResult queryItemPageList(Integer page,Integer rows) {
		EasyUIDataGridResult list = itemService.queryItemPageList(page, rows);
		return list;
	}
	/**
	 * 保存商品
	 * @param item 商品
	 * @param desc 留言
	 * @return
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result saveItem(TbItem item,String desc) {
		E3Result e3 = itemService.saveItem(item,desc);
		return e3;
	}
	
	/**
	 * 回显商品描述
	 * @param ids 商品id
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result queryItemDescById(Model model,@PathVariable Long id) {
		TbItemDesc itemDesc = itemService.getItemDescById(id);
		E3Result e3 = new E3Result();
		E3Result ok = e3.ok(itemDesc);
		return ok;
	}
	/**
	 * 回显商品描述
	 * @param ids 商品id
	 * @return
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public TbItem queryItemById(@PathVariable Long id) {
		 TbItem item = itemService.getItemById(id);
		return item;
	}
	/**
	 * 修改商品方法
	 * @param item 商品
	 * @param desc 商品描述
	 * @return
	 */
	@RequestMapping(value = "/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result saveItemAndDesc(TbItem item,TbItemDesc desc) {
		E3Result e3 = itemService.updateItemAndDesc(item, desc);
		return e3;
	}
	
	/**
	 * 删除商品业务
	 * @param ids 商品id
	 * @return
	 */
	@RequestMapping(value = "/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItemById(Long ids) {
		//调用删除商品业务
		E3Result e3 = itemService. deleteItemById(ids);
		//返回R3Result
		return e3;
	}
	/**
	 * 下架商品
	 * @param ids 商品id
	 * @return
	 */
	@RequestMapping(value = "/rest/item/instock",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateInstockByID(Long ids) {
		//调用下架商品业务
		E3Result e3 = itemService.updateInstockByID(ids);
		//返回R3Result
		return e3;
	}
	
	/**
	 * 上架商品
	 * @param ids  商品id
	 * @return
	 */
	@RequestMapping(value = "/rest/item/reshelf",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateReshelfByID(Long ids) {
		//调用下架商品业务
		E3Result e3 = itemService.updateReshelfByID(ids);
		//返回R3Result
		return e3;
	}
}

