package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.CourseCommentDao;
import com.online.college.domain.CourseComment;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseCommentService;


@Service
public class CourseCommentServiceImpl implements ICourseCommentService{

	@Autowired
	private CourseCommentDao entityDao;

	@Override
	public CourseComment getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<CourseComment> queryAll(CourseComment queryEntity){
		return entityDao.queryAll(queryEntity);
	}

	//分页获取课程评论
	@Override
	public TailPage<CourseComment> queryPage(CourseComment queryEntity ,TailPage<CourseComment> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<CourseComment> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}
	
	@Override
	public TailPage<CourseComment> queryMyQAItemsPage(CourseComment queryEntity ,TailPage<CourseComment> page){
		Integer itemsTotalCount = entityDao.getMyQAItemsCount(queryEntity);
		List<CourseComment> items = entityDao.queryMyQAItemsPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void create(CourseComment entity){
		entityDao.create(entity);
	}
	
	/**
	 * 创建
	 */
	@Override
	public void createSelectivity(CourseComment entity){
		entityDao.createSelectivity(entity);
	}

	@Override
	public void update(CourseComment entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(CourseComment entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(CourseComment entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(CourseComment entity){
		entityDao.deleteLogic(entity);
	}



}

