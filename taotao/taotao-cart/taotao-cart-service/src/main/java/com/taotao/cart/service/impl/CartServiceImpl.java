package com.taotao.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cart.service.CartService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Override 
	public E3Result addCart(Long userId, Long itemId, Integer num) {
		//判断商品在Redis中是否存在
		Boolean hexists = jedisClient.hexists("CART:"+userId, itemId +"");
		if (hexists) {
			//如果存在就将数量相加
			String hget = jedisClient.hget("CART:"+userId, itemId +"");
			//将json数据转换成pojo
			TbItem item = JsonUtils.jsonToPojo(hget, TbItem.class);
			item.setNum(item.getNum()+num);
			jedisClient.hset("CART:"+userId, itemId +"", JsonUtils.objectToJson(item));
		}
		//如果不存在查询出此商品保存到redis中
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		jedisClient.hset("CART:"+userId, itemId +"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

}
