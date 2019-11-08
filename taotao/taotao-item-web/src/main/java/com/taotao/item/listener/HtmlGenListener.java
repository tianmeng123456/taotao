package com.taotao.item.listener;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	@Value("HTML_GEN_PATH")
	private String HTML_GEN_PATH;
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = null;
			Long itemId = null;
			if(message instanceof TextMessage) {
				textMessage = (TextMessage) message;
				itemId = Long.parseLong(textMessage.getText());
			}
			Thread.sleep(100);
			//根据商品id查询商品信息，商品的基本信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			Map itemandDesc = new HashMap();
			itemandDesc.put("item", tbItem);
			itemandDesc.put("itemDesc", tbItemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流，指定输出的目录及文件名
			FileWriter fileWriter = new FileWriter(HTML_GEN_PATH+itemId+".html");
			//生成静态页面
			template.process(itemandDesc, fileWriter);
	}catch (Exception e) {
		e.printStackTrace();
	}

}
}
