package com.online.college.statics.service;

import com.online.college.statics.domain.CourseStudyStaticsDto;
import com.online.college.statics.domain.StaticsVO;

/**
 * 报表统计
 */
public interface IStaticsService {
	/**
	*统计课程学习情况
	**/
	public StaticsVO queryCourseStudyStatistics(CourseStudyStaticsDto queryEntity);
	
}
