package com.taotao.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.jedis.JedisClient;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.ToKenService;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;

/**
 * token服务的实现类
 * 
 * @author 田猛
 *
 */
@Service
public class ToKenServiceImpl implements ToKenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("USER_INFO")
	private String USER_INFO;

	/**
	 * 根据token从Redis查询user
	 */
	@Override
	public E3Result userByToken(String token) {
		// 根据Token查询redis中的用户信息
		String json = jedisClient.get(USER_INFO + ":" + token);
		// 判断是否为空，为空就返回一个提示
		if (StringUtils.isBlank(json)) {
			return E3Result.build(201, "用户信息以过期");
		}
		// 判断有值，说明用户已经登录,从新设置session的过期时间
		jedisClient.expire(USER_INFO + ":" + token, 1800);
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(tbUser);
	}

	/**
	 * 根据token删除Redis中的user
	 */
	@Override
	public E3Result deleteToken(String token) {
		// 根据Token查询redis中的用户信息
		jedisClient.expire(USER_INFO + ":" + token, 1);
		// 判断是否为空，为空就返回一个提示
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return E3Result.ok();
	}

}
