package com.taotao.utils;

import java.io.Serializable;
import java.util.List;

import com.taotao.pojo.SearchItem;

public class SearchResult implements Serializable{
	private int totalPages;
	private List<SearchItem>ItemList;
	private int recourdCount;
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return ItemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		ItemList = itemList;
	}
	public int getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(int recourdCount) {
		this.recourdCount = recourdCount;
	}
	
}
