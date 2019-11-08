package com.taotao.cart.service;


import com.taotao.utils.E3Result;

public interface CartService {
	public E3Result addCart(Long userId,Long itemId,Integer num);
}
