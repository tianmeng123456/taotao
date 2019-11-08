package com.taotao.search.dao;
/**
 * 关键字查询索引库
 * @author 田猛
 *
 */

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.pojo.SearchItem;
import com.taotao.utils.SearchResult;

@Repository
public class SearchDao {
	@Autowired
	public SolrServer solrServer;
	//根据关键字查询索引库
	public SearchResult search(SolrQuery query) throws Exception{
		//根据查询条件查询索引库
		QueryResponse query2 = solrServer.query(query);
		//取查询总记录数
		SolrDocumentList results = query2.getResults();
		int numFound = (int) results.getNumFound();
		//创建一个返回值对象
		SearchResult searchResult = new SearchResult();
		searchResult.setRecourdCount(numFound);
		//创建一个商品列表对象
		List<SearchItem> seaList = new ArrayList<SearchItem>();
		//取商品列表
		//取高亮后的结果
		Map<String, Map<String, List<String>>> highlighting = query2.getHighlighting();
		for (SolrDocument solrDocument : results) {
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String)solrDocument.get("item_category_name"));
			searchItem.setId((String)solrDocument.get("id"));
			searchItem.setImage(((String)solrDocument.get("item_image")));
			searchItem.setSell_point((String)solrDocument.get("item_shell_point"));
			searchItem.setTitle((String)solrDocument.get("item_title"));
			searchItem.setPrice((long)solrDocument.get("item_price"));
			//取高亮结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			//添加商品列表
			seaList.add(searchItem);
		}
		//把列表页添加到返回结果集中。
		searchResult.setItemList(seaList);
		return searchResult;	
	}
}
