package com.online.college.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.ConstsSiteCarouselDao;
import com.online.college.domain.ConstsSiteCarousel;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsSiteCarouselService;
import com.online.college.storage.QiniuStorage;


@Service
public class ConstsSiteCarouselServiceImpl implements IConstsSiteCarouselService{

	@Autowired
	private ConstsSiteCarouselDao entityDao;

	@Override
	public ConstsSiteCarousel getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<ConstsSiteCarousel> queryCarousels(Integer count){
		List<ConstsSiteCarousel> resultList = entityDao.queryCarousels(count);
		//处理为七牛图片链接
		for(ConstsSiteCarousel item : resultList){
			item.setPicture(QiniuStorage.getUrl(item.getPicture()));
		}
		return resultList;
	}

	@Override
	public TailPage<ConstsSiteCarousel> queryPage(ConstsSiteCarousel queryEntity ,TailPage<ConstsSiteCarousel> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<ConstsSiteCarousel> items = entityDao.queryPage(queryEntity,page);
		if(CollectionUtils.isNotEmpty(items)){
			for(ConstsSiteCarousel item : items){
				String pictureUrl = QiniuStorage.getUrl(item.getPicture());//处理图片
				item.setPicture(pictureUrl);
			}
		}
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void create(ConstsSiteCarousel entity){
		entityDao.create(entity);
	}
	
	/**
	 * 创建新记录
	 */
	@Override
	public void createSelectivity(ConstsSiteCarousel entity){
		this.entityDao.createSelectivity(entity);
	}

	@Override
	public void update(ConstsSiteCarousel entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(ConstsSiteCarousel entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(ConstsSiteCarousel entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(ConstsSiteCarousel entity){
		entityDao.deleteLogic(entity);
	}



}

