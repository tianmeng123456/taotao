package com.taotao.service;
/*
 * 商品的业务接口
 */

import java.util.Map;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.utils.E3Result;

public interface ItemService extends BaseService<TbItem> {
	/*
	 * 分页查询商品方法
	 */
	public EasyUIDataGridResult queryItemPageList(Integer page,Integer rows);
	/**
	 * 保存商品业务
	 * @param item 商品实体类
	 * @param desc 商品描述实体类
	 * @return
	 */
	public E3Result saveItem(TbItem item, String desc);
	/**
	 * 根据id查询商品和商品描述业务
	 * @param ids 商品id
	 * @return
	 */
	public Map getItemandDescById(long ids);
	/**
	 * 修改商品和商品描述方法
	 * @param item 商品实体类
	 * @param desc 商品描述实体类
	 * @return
	 */
	public E3Result updateItemAndDesc(TbItem item, TbItemDesc desc);
	/**
	 * 查询商品描述方法
	 * @param id  商品id
	 * @return  商品描述实体类
	 */
	public TbItemDesc getItemDescById(Long id);
	/**
	 * 查询商品方法
	 * @param id  商品id
	 * @return  商品描述实体类
	 */
	
	public TbItem getItemById(Long id);
	
	/**
	 * 删除商品业务
	 * @param ids 商品id
	 * @return
	 */
	public E3Result deleteItemById(Long ids);
	/**
	 * 下架商品业务
	 * @param ids  商品id
	 * @return
	 */
	public E3Result updateInstockByID(Long ids);
	
	/**
	 * 上架商品业务
	 * @param ids 商品id
	 * @return
	 */
	public E3Result updateReshelfByID(Long ids);
}
