package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.CourseSectionDao;
import com.online.college.domain.CourseSection;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseSectionService;


@Service
public class CourseSectionServiceImpl implements ICourseSectionService{

	@Autowired
	private CourseSectionDao entityDao;

	@Override
	public CourseSection getById(Long id){
		return entityDao.getById(id);
	}

	@Override
	public List<CourseSection> queryAll(CourseSection queryEntity){
		return entityDao.queryAll(queryEntity);
	}
	
	/**
	 * 获取课程章最大的sort
	 */
	@Override
	public Integer getMaxSort(Long courseId){
		return entityDao.getMaxSort(courseId);
	}

	@Override
	public TailPage<CourseSection> queryPage(CourseSection queryEntity ,TailPage<CourseSection> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<CourseSection> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void createSelectivity(CourseSection entity){
		entityDao.createSelectivity(entity);
	}
	
	/**
	*批量创建
	**/
	@Override
	public void createList(List<CourseSection> entityList){
		entityDao.createList(entityList);
	}

	@Override
	public void update(CourseSection entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(CourseSection entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(CourseSection entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(CourseSection entity){
		entityDao.deleteLogic(entity);
	}

	/**
	 * 比当前sort大的，正序排序的第一个
	 * @param curCourseSection
	 * @return
	 */
	@Override
	public CourseSection getSortSectionMax(CourseSection curCourseSection){
		return entityDao.getSortSectionMax(curCourseSection);
	}
	
	/**
	 * 比当前sort小的，倒序排序的第一个
	 * @param curCourseSection
	 * @return
	 */
	@Override
	public CourseSection getSortSectionMin(CourseSection curCourseSection){
		return entityDao.getSortSectionMin(curCourseSection);
	}

}

