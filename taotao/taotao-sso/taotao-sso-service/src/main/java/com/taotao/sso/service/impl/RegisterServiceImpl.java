package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.RegisterService;
import com.taotao.utils.E3Result;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;

	/**
	 * 校验注册用户的手机号，用户名是否存在
	 * 
	 * @param massage 手机号或用户名或邮箱
	 * @param type    1是用户名，2是手机号，3是邮箱
	 * @return E3Result
	 */
	@Override
	public E3Result changeUserMassage(String massage, Integer type) {
		TbUserExample userExample = new TbUserExample();
		Criteria criteria = userExample.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(massage);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(massage);
		} else if (type == 3) {
			criteria.andEmailEqualTo(massage);
		} else {
			return E3Result.build(400, "非法参数");
		}
		List<TbUser> list = tbUserMapper.selectByExample(userExample);
		if (list == null || list.size() == 0) {
			return E3Result.ok(true);
		}
		return E3Result.ok(false);
	}

	/**
	 * 注册用户业务
	 * 
	 * @param tbUser 用户信息
	 * @return
	 */
	@Override
	public E3Result register(TbUser tbUser) {
		if (!StringUtils.isNotBlank(tbUser.getUsername())) {
			return E3Result.build(400, "用户姓名不能为空");
		}
		if (!StringUtils.isNotBlank(tbUser.getPassword())) {
			return E3Result.build(400, "用户密码不能为空");
		}

		E3Result e3Result = changeUserMassage(tbUser.getUsername(), 1);
		if (!(boolean) e3Result.getData()) {
			return E3Result.build(400, "用户名已经被占用");
		}
		if (StringUtils.isNotBlank(tbUser.getPhone())) {
			e3Result = changeUserMassage(tbUser.getPhone(), 2);
			if (!(boolean) e3Result.getData()) {
				return E3Result.build(400, "手机号已经被占用");
			}
		}
		if (StringUtils.isNotBlank(tbUser.getEmail())) {
			e3Result = changeUserMassage(tbUser.getEmail(), 3);
			if (!(boolean) e3Result.getData()) {
				return E3Result.build(400, "邮箱已经被占用");
			}
		}
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5DigestAsHex);
		tbUserMapper.insert(tbUser);
		return E3Result.ok();
	}

}
