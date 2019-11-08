package com.taotao.search.service;

import com.taotao.utils.E3Result;
/**
 * 索引库接口
 * @author 田猛
 *
 */
import com.taotao.utils.SearchResult;
public interface SearchItemService  {
	/**
	 * 将商品类表注入索引库业务
	 */
	public E3Result importAllItems();
	
	/**
	 * 查询索引库业务网
	 */
	public SearchResult searchQuery(String keyWord,int page,int rows) throws Exception;
	/**
	 * 向索引库插入一条商品业务
	 * @throws Exception 
	 */
	public void addDocument(Long itemId) throws Exception;
}
