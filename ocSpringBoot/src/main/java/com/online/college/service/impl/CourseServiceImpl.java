package com.online.college.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.CourseDao;
import com.online.college.domain.Course;
import com.online.college.domain.CourseEnum;
import com.online.college.domain.CourseQueryDto;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseService;
import com.online.college.storage.QiniuStorage;

@Service
public class CourseServiceImpl implements ICourseService{

	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private CourseDao entityDao;

	private void prepareCoursePicture(Course course){
		if(null != course && StringUtils.isNotEmpty(course.getPicture())){
			course.setPicture(QiniuStorage.getUrl(course.getPicture()));
		}
	}

	@Override
	public Course getById(Long id){
		Course course = entityDao.getById(id);
		prepareCoursePicture(course);
		return course;
	}

	@Override
	public List<Course> queryList(CourseQueryDto queryEntity){
		if(null == queryEntity.getOnsale()){//是否上架
			queryEntity.setOnsale(CourseEnum.ONSALE.value());
		}
		return entityDao.queryList(queryEntity);
	}

	@Override
	public TailPage<Course> queryPage(Course queryEntity ,TailPage<Course> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<Course> items = entityDao.queryPage(queryEntity,page);
		if(CollectionUtils.isNotEmpty(items)){
			for(Course item : items){
				prepareCoursePicture(item);
			}
		}
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void createSelectivity(Course entity){
		entityDao.createSelectivity(entity);
	}

	@Override
	public void updateSelectivity(Course entity){
		entityDao.updateSelectivity(entity);
	}

	//物理删除
	@Override
	public void delete(Course entity){
		entityDao.delete(entity);
	}
	
	//逻辑删除
	@Override
	public void deleteLogic(Course entity){
		entityDao.deleteLogic(entity);
	}
	
	//遍历实战推荐和免费好课
	@Override
	public List<Course> getFiveCourses(Course queryEntity){
		return courseDao.getFiveCourses(queryEntity);
	}
	
}
