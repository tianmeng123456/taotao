package com.taotao.search.mapper;

import java.util.List;

import com.taotao.pojo.SearchItem;

public interface ItemMapper {
	 List<SearchItem> getItemList();
	 SearchItem getItemById(Long itemId);
}
