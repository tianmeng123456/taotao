package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.portal.service.ContentService;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;

@Service
/**
 * 内容业务实现类
 * 
 * @author 田猛
 *
 */
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentCategoryMapper tbMapper;
	@Autowired
	private TbContentMapper tbContentMapper;
	// Redis的接口
	@Autowired
	private JedisClient jedisClient;

	// 保存缓存的类别名称
	@Value("${CONTEN_KEY}")
	private String CONTEN_KEY;

	@Override
	public List<EasyUITreeNode> queryContentList(Long parentId) {
//		1、根据parentId调用dao的条件查询方法
		TbContentCategoryExample tbcce = new TbContentCategoryExample();
		Criteria createCriteria = tbcce.createCriteria();
		createCriteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> selectByExample = tbMapper.selectByExample(tbcce);
//		2、创建List<EasyUITreeNode>
		ArrayList<EasyUITreeNode> list = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : selectByExample) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
//		3、将数据放入List<EasyUITreeNode>
			list.add(node);
		}
//		4、返回List<EasyUITreeNode>
		return list;
	}

	@Override
	public E3Result addContent(Long parentId, String name) {
//		1、创建内容pojo
		TbContentCategory tbcc = new TbContentCategory();
//		2、将内容赋值经对象
		tbcc.setParentId(parentId);
		tbcc.setName(name);
		// 状态。可选值:1(正常),2(删除)
		tbcc.setStatus(1);
		tbcc.setSortOrder(1);
		tbcc.setCreated(new Date());
		tbcc.setUpdated(new Date());
		// 该类目是否为父类目，1为true，0为false
		tbcc.setIsParent(false);
//		3、保存对象
		tbMapper.insert(tbcc);
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = tbMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			// 更新父节点
			tbMapper.updateByPrimaryKey(parentNode);
		}
		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(tbcc);
	}

	@Override
	public E3Result deleteContentNodeById(Long id) {
		// 查询内容分类
		TbContentCategory selectByPrimaryKey = tbMapper.selectByPrimaryKey(id);
		if (selectByPrimaryKey.getIsParent()) {
			return null;
		}
		// 根据id删除内容分类
		tbMapper.deleteByPrimaryKey(id);
		return E3Result.ok();
	}

	@Override
	public E3Result updateContentNodeById(Long id, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setId(id);
		tbContentCategory.setName(name);
		tbMapper.updateByPrimaryKeySelective(tbContentCategory);
		return null;
	}

	@Override
	public EasyUIDataGridResult selectContentList() {
		List<TbContent> selectByExample = tbContentMapper.selectByExample(null);
		EasyUIDataGridResult eu = new EasyUIDataGridResult();
		eu.setTotal(selectByExample.size());
		eu.setRows(selectByExample);
		return eu;
	}

	@Override
	public E3Result saveContentByContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
		//缓存同步
		jedisClient.hdel(CONTEN_KEY, tbContent.getCreated().toString());
		return E3Result.ok();
	}

	@Override
	public E3Result editSaveContentByContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContentMapper.updateByPrimaryKeySelective(tbContent);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentByContent(Long ids) {
		tbContentMapper.deleteByPrimaryKey(ids);
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByIds(Long aDRotation) {
		// 查询缓存
		try {
			String json = jedisClient.hget(CONTEN_KEY, aDRotation + "");
			// 判断是否为空
			if (StringUtils.isNotBlank(json)) {
				// 把json转换成为list
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample tbContentExample = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria createCriteria = tbContentExample.createCriteria();
		createCriteria.andCategoryIdEqualTo(aDRotation);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
		// 添加缓存
		try {
			jedisClient.hset(CONTEN_KEY, aDRotation + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
