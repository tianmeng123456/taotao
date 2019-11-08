package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;

public class Item extends TbItem {

	public String[] getImages() {
		String image2 = this.getImage();
		if (image2 != null && "".equals(image2)) {
			String[] split = image2.split(",");
			return split;
		}
		return null;
	}

	public Item() {

	}

	public Item(TbItem tbItem) {
		 this.setId(tbItem.getId());
	     this.setTitle(tbItem.getTitle());
	     this.setSellPoint(tbItem.getSellPoint());
	     this.setPrice(tbItem.getPrice());
	     this.setNum(tbItem.getNum());
	     this.setBarcode(tbItem.getBarcode());
	     this.setImage(tbItem.getImage());
	     this.setCid(tbItem.getCid());
	     this.setStatus(tbItem.getStatus());
	     this.setCreated(tbItem.getCreated());
	     this.setUpdated(tbItem.getUpdated());
	}
}
