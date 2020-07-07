package com.online.college.ocSpringBootWechat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.business.IPortalBusiness;
import com.online.college.domain.Course;
import com.online.college.domain.CourseComment;
import com.online.college.domain.CourseSection;
import com.online.college.domain.UserCourseSection;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseCommentService;
import com.online.college.service.ICourseSectionService;
import com.online.college.service.ICourseService;
import com.online.college.service.IUserCourseSectionService;
import com.online.college.vo.CourseSectionVO;
import com.online.college.web.SessionContext;


@Controller
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private IPortalBusiness portalBusiness;

	@Autowired
	private ICourseSectionService courseSectionService;
	
	@Autowired
	private ICourseCommentService courseCommentService;
		
	@Autowired
	private IUserCourseSectionService userCourseSectionService;
	
	/**
	 * 课程详情
	 */
	@RequestMapping("/read")
	public String read(Long id , Map<String,Object> model , HttpServletRequest request){
		
		//课程基本信息
		Course course = courseService.getById(id);
		if(null == course){
			return "error/404"; 
		}
		model.put("course", course);
		
		//课程章节
		List<CourseSectionVO> chaptSections = this.portalBusiness.queryCourseSection(id);
		model.put("chaptSections", chaptSections);
		
		return "read";
	}
	
	/**
	 * 课程视频学习
	 */
	@RequestMapping("/video")
	public String video(Map<String,Object> model , HttpServletRequest request , Long id){
		
		//课程基本信息
		CourseSection courseSection = courseSectionService.getById(id);
		if(null == courseSection)
			return "error/404"; 
		model.put("courseSection", courseSection);
		
		//学习记录
		Long userId = SessionContext.getWxUserId(request);//当前登录用户id
		if(null != userId){
			//获取的学习记录
			UserCourseSection userCourseSection = new UserCourseSection();
			userCourseSection.setUserId(userId);
			userCourseSection.setCourseId(courseSection.getCourseId());
			userCourseSection.setSectionId(courseSection.getId());
			UserCourseSection result = userCourseSectionService.queryLatest(userCourseSection);
			
			//如果没有学习过这节课，添加学习记录
			if(null == result){
				userCourseSection.setCreateTime(new Date());
				userCourseSection.setCreateUser(SessionContext.getWxUsername(request));
				userCourseSection.setUpdateTime(new Date());
				userCourseSection.setUpdateUser(SessionContext.getWxUsername(request));
				
				userCourseSectionService.createSelectivity(userCourseSection);
			}else{
				result.setUpdateTime(new Date());
				userCourseSectionService.update(result);
			}
		}
		return "video";
	}
	
	/**
	 * 课程评论分页
	 */
	@RequestMapping("/comment")
	public String comment(CourseComment queryEntity , TailPage<CourseComment> page , Map<String,Object> model , HttpServletRequest request){
		TailPage<CourseComment> commentPage = this.courseCommentService.queryPage(queryEntity, page);
		model.put("page", commentPage);
		return "comment";
	}
	
}

