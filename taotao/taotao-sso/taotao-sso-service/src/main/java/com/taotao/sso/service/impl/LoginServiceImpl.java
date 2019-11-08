package com.taotao.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.LoginService;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;
/**
 * 登录功能service实现类
 * @author 田猛
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("USER_INFO")
	private String USER_INFO;
	
	@Override
	public E3Result login(String usernam, String password) {
		//第一步判断用户名和密码的正确性
		TbUserExample tbUserExample = new TbUserExample();
		Criteria createCriteria = tbUserExample.createCriteria();
		createCriteria.andUsernameEqualTo(usernam);
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list == null || list.size() == 0) {
			return E3Result.build(400, "用户名或密码错误");
		}
		if(!list.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return E3Result.build(400, "用户名或密码错误");
		}
		//生成token的key
		String token = UUID.randomUUID().toString();
		//把用户信息保存到Redis，key就是token，value就是用户信息转换成json
		TbUser user = list.get(0);
		user.setPassword(null);
		jedisClient.set(USER_INFO+":"+token,JsonUtils.objectToJson(user) );
		//设置一下过期时间
		jedisClient.expire(USER_INFO+":"+token, 1800);
		//返回E3Result
		return E3Result.ok(token);
	}

}
