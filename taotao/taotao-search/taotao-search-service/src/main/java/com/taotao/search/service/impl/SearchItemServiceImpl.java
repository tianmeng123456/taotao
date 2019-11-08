package com.taotao.search.service.impl;
/**
 * 索引库维护Service
 * @author 田猛
 *
 */

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.SearchItem;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.service.SearchItemService;
import com.taotao.utils.E3Result;
import com.taotao.utils.SearchResult;

@Service
public class SearchItemServiceImpl implements SearchItemService  {
	@Autowired
	private HttpSolrServer solrServer;
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SearchDao searchDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	@Override
	public E3Result importAllItems() {
		try {
			List<SearchItem> itemList = itemMapper.getItemList();
			for (SearchItem searchItem : itemList) {
				// 创建索引库
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", searchItem.getId());
				document.setField("item_title", searchItem.getTitle());
				document.setField("item_sell_point", searchItem.getSell_point());
				document.setField("item_price", searchItem.getPrice());
				document.setField("item_image", searchItem.getImage());
				solrServer.add(document);
			}
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "商品导入失败");
		}
	}

	@Override
	public SearchResult searchQuery(String keyWord, int page, int rows) throws Exception {
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery(keyWord);
		//设置分页条件
		solrQuery.setStart((page-1)*rows);
		//设置rows
		solrQuery.setRows(rows);
		//设置默认搜索域
		solrQuery.set("df", DEFAULT_FIELD);
		//设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		SearchResult searchResult = searchDao.search(solrQuery);
		return searchResult;
	}

	@Override
	public void addDocument(Long itemId) throws Exception {
		SearchItem searchItem = itemMapper.getItemById(itemId);
		// 创建索引库
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", searchItem.getId());
		document.setField("item_title", searchItem.getTitle());
		document.setField("item_sell_point", searchItem.getSell_point());
		document.setField("item_price", searchItem.getPrice());
		document.setField("item_image", searchItem.getImage());
		solrServer.add(document);
		solrServer.commit();
	}

}
