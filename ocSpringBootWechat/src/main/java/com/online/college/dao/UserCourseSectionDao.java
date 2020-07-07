package com.online.college.dao;

import java.util.List;

import com.online.college.domain.UserCourseSection;
import com.online.college.domain.UserCourseSectionDto;
import com.online.college.page.TailPage;


public interface UserCourseSectionDao {

	/**
	*根据id获取
	**/
	public UserCourseSection getById(Long id);

	/**
	*获取所有
	**/
	public List<UserCourseSection> queryAll(UserCourseSection queryEntity);

	/**
	 * 获取最新的学习记录
	 */
	public UserCourseSection queryLatest(UserCourseSection queryEntity);
	
	/**
	*获取总数量
	**/
	public Integer getTotalItemsCount(UserCourseSection queryEntity);

	/**
	*分页获取
	**/
	public List<UserCourseSectionDto> queryPage(UserCourseSection queryEntity , TailPage<UserCourseSectionDto> page);

	/**
	*创建新记录
	**/
	public void createSelectivity(UserCourseSection entity);

	/**
	*根据id更新
	**/
	public void update(UserCourseSection entity);

	/**
	*根据id选择性更新自动
	**/
	public void updateSelectivity(UserCourseSection entity);

	/**
	*物理删除
	**/
	public void delete(UserCourseSection entity);

	/**
	*逻辑删除
	**/
	public void deleteLogic(UserCourseSection entity);



}

