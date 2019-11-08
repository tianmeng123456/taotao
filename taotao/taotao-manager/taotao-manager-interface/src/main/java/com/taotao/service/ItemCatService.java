package com.taotao.service;

import java.util.List;

import com.taotao.pojo.EasyUITreeNode;

public interface ItemCatService {
	/*
	 * 根据prentid 查询商量类型
	 */
	public List<EasyUITreeNode> queryItemcat(Long prentId);
	
}
