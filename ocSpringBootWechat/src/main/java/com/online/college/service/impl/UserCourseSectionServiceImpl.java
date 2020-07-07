package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.UserCourseSectionDao;
import com.online.college.domain.UserCourseSection;
import com.online.college.domain.UserCourseSectionDto;
import com.online.college.page.TailPage;
import com.online.college.service.IUserCourseSectionService;


@Service
public class UserCourseSectionServiceImpl implements IUserCourseSectionService{

	@Autowired
	private UserCourseSectionDao entityDao;

	@Override
	public UserCourseSection getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<UserCourseSection> queryAll(UserCourseSection queryEntity){
		return entityDao.queryAll(queryEntity);
	}
	
	@Override
	public UserCourseSection queryLatest(UserCourseSection queryEntity){
		return entityDao.queryLatest(queryEntity);
	}

	@Override
	public TailPage<UserCourseSectionDto> queryPage(UserCourseSection queryEntity , TailPage<UserCourseSectionDto> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<UserCourseSectionDto> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void createSelectivity(UserCourseSection entity){
		entityDao.createSelectivity(entity);
	}

	@Override
	public void update(UserCourseSection entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(UserCourseSection entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(UserCourseSection entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(UserCourseSection entity){
		entityDao.deleteLogic(entity);
	}



}

