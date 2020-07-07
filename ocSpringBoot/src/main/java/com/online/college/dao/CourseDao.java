package com.online.college.dao;

import java.util.List;

import com.online.college.domain.Course;
import com.online.college.domain.CourseQueryDto;
import com.online.college.page.TailPage;


public interface CourseDao {

	/**
	*根据id获取
	**/
	public Course getById(Long id);

	/**
	*根据条件获取所有，
	*queryEntity：查询条件；
	**/
	public List<Course> queryList(CourseQueryDto queryEntity);
	
	/**
	*获取总数**/
	public Integer getTotalItemsCount(Course queryEntity);

	/**
	*分页获取
	**/
	public List<Course> queryPage(Course queryEntity , TailPage<Course> page);
	
	/**
	*创建新记**/
	public void create(Course entity);
	public void createSelectivity(Course entity);

	/**
	*根据id更新
	**/
	public void update(Course entity);

	/**
	*根据id选择性更新自*/
	public void updateSelectivity(Course entity);

	/**
	*物理删除
	**/
	public void delete(Course entity);

	/**
	*逻辑删除
	**/
	public void deleteLogic(Course entity);

	List<Course> getFiveCourses(Course queryEntity);

}

