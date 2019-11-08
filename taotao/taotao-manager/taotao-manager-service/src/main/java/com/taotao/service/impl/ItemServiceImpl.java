package com.taotao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.service.ItemService;
import com.taotao.utils.E3Result;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

@Service
public class ItemServiceImpl extends BaseServiceImpl<TbItem> implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;

	@Override
	public EasyUIDataGridResult queryItemPageList(Integer page, Integer rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(tbItemExample);
		// 取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		// 创建返回对象
		EasyUIDataGridResult er = new EasyUIDataGridResult();
		er.setTotal((int) pageInfo.getTotal());
		er.setRows(pageInfo.getList());
		return er;
	}

	@Override
	public E3Result saveItem(TbItem item, String desc) {
		// TODO Auto-generated method stub
		// 生成商品ID
		final long id = IDUtils.genItemId();
		// 将商品ID赋值给商品
		item.setId(id);
		// 商品状态，1-正常，2-下架，3-删除
		Byte status = 1;
		item.setStatus(status);
		// 赋值创建时间
		item.setCreated(new Date());
		// 赋值更新时间
		item.setUpdated(new Date());
		// 保存商品
		int i = itemMapper.insert(item);
		// 创建留言实体类
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 保存实体类留言
		itemDescMapper.insert(itemDesc);
		//发送一个商品添加消息  ////
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(id+"");
				return message;
			}
		});
		return E3Result.ok();
	}

	@Override
	public Map getItemandDescById(long ids) {
		// TODO Auto-generated method stub
//		1、查询TbItem
		TbItem tbItem = itemMapper.selectByPrimaryKey(ids);
//		2、查询Desc方法
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(ids);
//		3、将TbItem 和TbItem封装到Map中
		Map<Object, Object> map = new HashMap<>();
		map.put("item", tbItem);
		map.put("desc", tbItemDesc.getItemDesc());
//		4、返回QueryVo
		return map;
	}

	@Override
	public E3Result updateItemAndDesc(TbItem item, TbItemDesc desc) {
//      1、查询商品
		TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
		// 赋值商品的初始日期
		item.setCreated(tbItem.getCreated());
//		2、给商品创建修改日期
		item.setUpdated(new Date());
		// 给商品赋值状态 商品状态，1-正常，2-下架，3-删除
		item.setStatus(tbItem.getStatus());
//		4、进行商品保存
		itemMapper.updateByPrimaryKey(item);
		// 给描述设置初始日期
		desc.setCreated(tbItem.getCreated());
		// 7、给描述进行设置修改日期
		desc.setUpdated(new Date());
//		8、进行描述保存
		itemDescMapper.updateByPrimaryKey(desc);
//		9、返回E3Result
		return new E3Result().ok();
	}

	@Override
	/**
	 * 根据id查询商品描述
	 */
	public TbItemDesc getItemDescById(Long id) {
		try {
			//查询缓存
			String json = jedisClient.get("ITEM_INFO_PRE:"+id+":DESC");
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
		//同步缓存
				try {
					jedisClient.set("ITEM_INFO_PRE:"+id+":DESC", JsonUtils.objectToJson(tbItemDesc));
					//设置缓存有效期
					jedisClient.expire("ITEM_INFO_PRE:"+id+":DESC", 3600);
				} catch (Exception e) {
					e.printStackTrace();
				}
		return tbItemDesc;
	}

	@Override
	public TbItem getItemById(Long id) {
		try {
			//查询缓存
			String json = jedisClient.get("ITEM_INFO_PRE:"+id+":BASE");
			if(StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(id);
		//同步缓存
		try {
			jedisClient.set("ITEM_INFO_PRE:"+id+":BASE", JsonUtils.objectToJson(item));
			//设置缓存有效期
			jedisClient.expire("ITEM_INFO_PRE:"+id+":BASE", 3600);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public E3Result deleteItemById(Long ids) {
		// 查询商品
		TbItem tbItem = itemMapper.selectByPrimaryKey(ids);
		// 根据id修改商品状态 商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 3);
		itemMapper.updateByPrimaryKeySelective(tbItem);
		// 返回R3Rusult
		return new E3Result().ok();
	}

	@Override
	public E3Result updateInstockByID(Long ids) {
		// 查询商品
		TbItem tbItem = itemMapper.selectByPrimaryKey(ids);
		// 根据id修改商品状态 商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 2);
		itemMapper.updateByPrimaryKeySelective(tbItem);
		// 返回R3Rusult
		return new E3Result().ok();
	}

	@Override
	public E3Result updateReshelfByID(Long ids) {
		// 查询商品
		TbItem tbItem = itemMapper.selectByPrimaryKey(ids);
		// 根据id修改商品状态 商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		itemMapper.updateByPrimaryKeySelective(tbItem);
		// 返回R3Rusult
		return new E3Result().ok();
	}
}
