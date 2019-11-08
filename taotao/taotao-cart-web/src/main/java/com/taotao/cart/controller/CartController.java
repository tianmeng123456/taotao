package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.service.CartService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.E3Result;
import com.taotao.utils.JsonUtils;
/**
 * 购物车功能
 * @author 田猛
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	
	/**
	 * 商品添加到购物车功能
	 * @param itemId
	 * @param request
	 * @param response
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response,Integer num) {
        //1、从cookie中查询商品列表。
		List<TbItem> list = getCratList(request);
		//判断用户是否登录，登录后需要将商品信息放入Redis缓存中\
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.addCart(user.getId(), itemId, num);
		}
		boolean flage = false;
		for (TbItem tbItem : list) {
			//2、判断商品在商品列表中是否存在。
			if(tbItem.getId() == itemId.longValue()) {
				//3、如果存在，商品数量相加。
				flage = true;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
        //4、不存在，根据商品id查询商品信息。
		if(!flage) {
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			//取一张图片
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)) {
				String[] split = image.split(",");
				item.setImage(split[0]);
			}
			//5、把商品添加到购车列表。
			list.add(item);
		}
		// 6、把购车商品列表写入cookie。
	    CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), 68000, true);
	    return "cartSuccess";
	}
	
	
	/**
	 * 将商品从购物车中取出
	 * @param request
	 * @return
	 */
	private static List<TbItem> getCratList(HttpServletRequest request){
		//从cookie中取出商品列表
		String string = CookieUtils.getCookieValue(request, "TT_CART", true);
		//判断是否为空，如果为空也返回list防止报空指向异常
		if(!StringUtils.isNotBlank(string)) {
			return new ArrayList<TbItem>();
		}
		//判断该商品存在返回
		List<TbItem> jsonToList = JsonUtils.jsonToList(string, TbItem.class);
		return jsonToList;
	}
	
	/**
	 * 展示购物车商品
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart/cart")
	private String showCatList(HttpServletRequest request,Model model) {
		//查看用户是否登录
		//取商品列表页
		List<TbItem> cratList = getCratList(request);
		model.addAttribute("cartList", cratList);
		return "cart";
	}
	/**
	 * 修改购物车里面商品数量信息
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//获取cookie中的数据
		List<TbItem> cratList = getCratList(request);
		//遍历商品找到对应的商品
		for (TbItem tbItem : cratList) {
			if(tbItem.getId() == itemId.longValue()) {
				//修改数量
				tbItem.setNum(num);
				break;
			}
		}
		//将该的商品列表保存到cookie
		 CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cratList), 68000, true);
		 return E3Result.ok();
	}
	
	/**
	 * 删除购物车中的商品
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、从url中取商品id
		// 2、从cookie中取购物车商品列表
		List<TbItem> cartList = getCratList(request);
		// 3、遍历列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 4、删除商品。
				cartList.remove(tbItem);
				break;
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartList), 68000, true);
		// 6、返回逻辑视图：在逻辑视图中做redirect跳转。
		return "redirect:/cart/cart.html";
	}
	
}
