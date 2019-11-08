package com.taotao.portal.service;

import java.util.List;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TbContent;
import com.taotao.utils.E3Result;
/**
 * 内容分类业务接收
 * @author 田猛
 *
 */
public interface ContentService {
	/**
	 * 根据父类id查询子类内容分类对象业务
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> queryContentList(Long parentId);
	/**
	 * 保存内容分类业务
	 * @param parentId
	 * @param name
	 * @return
	 */
	E3Result addContent(Long parentId, String name);
	/**
	 * 删除内容分类业务
	 * @param id
	 * @return
	 */
	E3Result deleteContentNodeById(Long id);
	/**
	 * 修改内容分类业务
	 * @param id
	 * @param name
	 * @return
	 */
	E3Result updateContentNodeById(Long id, String name);
	
	/**
	 * 查询内容业务
	 * @return
	 */
	EasyUIDataGridResult selectContentList();
	/**
	 * 保存内容业务
	 * @param tbContent
	 * @return
	 */
	E3Result saveContentByContent(TbContent tbContent);
	/**
	 * 修改内容业务方法
	 * @param tbContent
	 * @return
	 */
	E3Result editSaveContentByContent(TbContent tbContent);
	/**
	 * 删除内容业务方法
	 * @param ids
	 * @return
	 */
	E3Result deleteContentByContent(Long ids);
	/**
	 * 根据父id查询内容
	 * @param aDRotation
	 * @return List<TbContent>
	 */
	List<TbContent> getContentListByIds(Long aDRotation);
	
}
