package com.taotao.sso.service;
/**
 * 登录业务的Service
 * @author 田猛
 *
 */

import com.taotao.utils.E3Result;

public interface LoginService {
	public E3Result login(String usernam,String password);
}
