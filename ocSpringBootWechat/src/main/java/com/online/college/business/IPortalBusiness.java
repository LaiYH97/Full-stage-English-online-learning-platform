package com.online.college.business;

import java.util.List;

import com.online.college.vo.CourseSectionVO;

public interface IPortalBusiness {
	
	/**
	 * 获取课程章节
	 */
	List<CourseSectionVO> queryCourseSection(Long courseId);
	
}
