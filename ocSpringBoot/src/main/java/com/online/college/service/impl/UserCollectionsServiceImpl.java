package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.UserCollectionsDao;
import com.online.college.domain.UserCollections;
import com.online.college.page.TailPage;
import com.online.college.service.IUserCollectionsService;


@Service
public class UserCollectionsServiceImpl implements IUserCollectionsService{

	@Autowired
	private UserCollectionsDao entityDao;

	@Override
	public UserCollections getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<UserCollections> queryAll(UserCollections queryEntity){
		return entityDao.queryAll(queryEntity);
	}

	@Override
	public TailPage<UserCollections> queryPage(UserCollections queryEntity ,TailPage<UserCollections> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<UserCollections> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void createSelectivity(UserCollections entity){
		entityDao.createSelectivity(entity);
	}

	@Override
	public void update(UserCollections entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(UserCollections entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(UserCollections entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(UserCollections entity){
		entityDao.deleteLogic(entity);
	}



}

