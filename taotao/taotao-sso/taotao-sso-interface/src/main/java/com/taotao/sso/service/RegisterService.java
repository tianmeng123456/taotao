package com.taotao.sso.service;

import com.taotao.pojo.TbUser;
import com.taotao.utils.E3Result;

/**
 * 注册业务Server
 * @author 田猛
 *
 */
public interface RegisterService {
	/**
	 * 校验注册用户的手机号，用户名是否存在
	 * @param massage 手机号或用户名或邮箱
	 * @param type 1是用户名，2是手机号，3是邮箱
	 * @return E3Result
	 */
	public E3Result changeUserMassage(String massage,Integer type);
	
	/**
	 * 注册用户业务
	 * @param tbUser 用户信息
	 * @return
	 */
	public E3Result register(TbUser tbUser);
}
