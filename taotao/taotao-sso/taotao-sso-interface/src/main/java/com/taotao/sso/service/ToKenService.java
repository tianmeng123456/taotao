package com.taotao.sso.service;
/**
 * Token的业务接口
 * @author 田猛
 *
 */

import com.taotao.utils.E3Result;

public interface ToKenService {
	public E3Result userByToken(String token);
	public E3Result deleteToken(String token);
}
