package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.pojo.FastDFSClient;
import com.taotao.utils.JsonUtils;
@Controller
public class PictureController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value = "/pic/upload",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	/*
	 * 上传图片方法
	 */
	public String picUpload(MultipartFile uploadFile){
		try {
			//获取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String string = filename.substring(filename.lastIndexOf(".")+1);
			//读取配置文件
			FastDFSClient fastDFS = new FastDFSClient("classpath:resource/client.conf");
			//上传文件
			String url = fastDFS.uploadFile(uploadFile.getBytes(),string);
			String path = IMAGE_SERVER_URL+url;
			Map map = new HashMap<>();
			map.put("error",0);
			map.put("url",path);
			String toJson = JsonUtils.objectToJson(map);
			return toJson;
		} catch (Exception e) {
			e.printStackTrace();
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message","上传失败" );
			String toJson = JsonUtils.objectToJson(map);
			return toJson;
		}
	}
}
