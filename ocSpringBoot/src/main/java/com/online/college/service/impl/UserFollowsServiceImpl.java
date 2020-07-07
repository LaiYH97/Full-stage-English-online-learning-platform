package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.UserFollowsDao;
import com.online.college.domain.UserFollowStudyRecord;
import com.online.college.domain.UserFollows;
import com.online.college.page.TailPage;
import com.online.college.service.IUserFollowsService;


@Service
public class UserFollowsServiceImpl implements IUserFollowsService{

	@Autowired
	private UserFollowsDao entityDao;

	@Override
	public UserFollows getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<UserFollows> queryAll(UserFollows queryEntity){
		return entityDao.queryAll(queryEntity);
	}

	@Override
	public TailPage<UserFollows> queryPage(UserFollows queryEntity ,TailPage<UserFollows> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<UserFollows> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}
	
	@Override
	public TailPage<UserFollowStudyRecord> queryUserFollowStudyRecordPage(UserFollowStudyRecord queryEntity ,TailPage<UserFollowStudyRecord> page){
		Integer itemsTotalCount = entityDao.getFollowStudyRecordCount(queryEntity);
		List<UserFollowStudyRecord> items = entityDao.queryFollowStudyRecord(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void createSelectivity(UserFollows entity){
		entityDao.createSelectivity(entity);
	}

	@Override
	public void update(UserFollows entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(UserFollows entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(UserFollows entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(UserFollows entity){
		entityDao.deleteLogic(entity);
	}



}

