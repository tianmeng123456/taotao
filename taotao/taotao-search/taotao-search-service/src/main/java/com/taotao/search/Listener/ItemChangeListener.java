package com.taotao.search.Listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.service.SearchItemService;

public class ItemChangeListener implements MessageListener {
	@Autowired
	private SearchItemService searchItemService;
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
			//向索引库中加文档
			searchItemService.addDocument(itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
