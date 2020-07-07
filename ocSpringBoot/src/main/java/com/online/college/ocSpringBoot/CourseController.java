package com.online.college.ocSpringBoot;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.business.ICourseBusiness;
import com.online.college.domain.AuthUser;
import com.online.college.domain.Course;
import com.online.college.domain.CourseQueryDto;
import com.online.college.domain.CourseSection;
import com.online.college.domain.UserCourseSection;
import com.online.college.service.IAuthUserService;
import com.online.college.service.ICourseSectionService;
import com.online.college.service.ICourseService;
import com.online.college.service.IUserCourseSectionService;
import com.online.college.storage.QiniuStorage;
import com.online.college.vo.CourseSectionVO;
import com.online.college.web.JsonView;
import com.online.college.web.SessionContext;

/**
 * 课程详情信息
 */
@Controller
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	private ICourseBusiness courseBusiness;
	
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private IAuthUserService authUserService;
	
	@Autowired
	private ICourseSectionService courseSectionService;
	
	@Autowired
	private IUserCourseSectionService userCourseSectionService;
	
	/**
	 * 课程章节页面
	 * @return
	 */
	@RequestMapping("/learn/{courseId}")
	public String learn(@PathVariable Long courseId, Map<String,Object> model , HttpServletRequest request){
		
		//如果没有该课程，则显示404页面
		if(null == courseId)
			return "error/404"; 
		
		//获取课程，如果课程为null，也显示404页面
		Course course = courseService.getById(courseId);
		if(null == course)
			return "error/404"; 
		
		//获取课程章节
		List<CourseSectionVO> chaptSections = this.courseBusiness.queryCourseSection(courseId);
		model.put("course", course);
		model.put("chaptSections", chaptSections);
		
		//获取讲师，通过课程数据获取讲师名字
		AuthUser courseTeacher = this.authUserService.getByUsername(course.getUsername());
		//如果讲师的头像不为空，显示讲师头像
		if(StringUtils.isNotEmpty(courseTeacher.getHeader())){
			courseTeacher.setHeader(QiniuStorage.getUrl(courseTeacher.getHeader()));
		}
		model.put("courseTeacher", courseTeacher);
		
		//获取推荐课程
		CourseQueryDto queryEntity = new CourseQueryDto();
		queryEntity.descSortField("weight");
		queryEntity.setCount(5);//5门推荐课程
		queryEntity.setSubClassify(course.getSubClassify());
		List<Course> recomdCourseList = this.courseService.queryList(queryEntity);
		model.put("recomdCourseList", recomdCourseList);
			
		//当前学习的章节
		UserCourseSection userCourseSection = new UserCourseSection();
		//获取课程id
		userCourseSection.setCourseId(course.getId());
		//获取当前用户的id
		userCourseSection.setUserId(SessionContext.getUserId());
		//获取学习的最新章节
		userCourseSection = this.userCourseSectionService.queryLatest(userCourseSection);
		if(null != userCourseSection){
			CourseSection curCourseSection = this.courseSectionService.getById(userCourseSection.getSectionId());
			model.put("curCourseSection", curCourseSection);
		}
		
		return "learn";
	}
	
	/**
	 * 视频学习页面
	 * @return
	 */
	@RequestMapping("/video/{sectionId}")
	public String video(@PathVariable Long sectionId, Map<String,Object> model , HttpServletRequest request){
		if(null == sectionId)
			return "error/404";  
		
		CourseSection courseSection = courseSectionService.getById(sectionId);
		if(null == courseSection)
			return "error/404"; 
		
		//课程章节
		List<CourseSectionVO> chaptSections = this.courseBusiness.queryCourseSection(courseSection.getCourseId());
		model.put("courseSection", courseSection);
		model.put("chaptSections", chaptSections);
		
		//学习记录
		UserCourseSection userCourseSection = new UserCourseSection();
		userCourseSection.setUserId(SessionContext.getUserId());
		userCourseSection.setCourseId(courseSection.getCourseId());
		userCourseSection.setSectionId(courseSection.getId());
		UserCourseSection result = userCourseSectionService.queryLatest(userCourseSection);
		
		if(null == result){//如果没有，插入
			userCourseSection.setCreateTime(new Date());
			userCourseSection.setCreateUser(SessionContext.getUsername());
			userCourseSection.setUpdateTime(new Date());
			userCourseSection.setUpdateUser(SessionContext.getUsername());
			
			userCourseSectionService.createSelectivity(userCourseSection);
		}else{		
			//自己补充的 start
			result.setUserId(SessionContext.getUserId());	
			result.setCourseId(1L);
			result.setSectionId(courseSection.getId());
			result.setUpdateUser(SessionContext.getUsername());
			//自己补充的 end
			
			result.setUpdateTime(new Date());
			userCourseSectionService.update(result);
		}
		
		return "video";
	}
	
	@RequestMapping(value = "/getCurLeanInfo")
	@ResponseBody
	public String getCurLeanInfo(){
		JsonView jv = new JsonView();
		//加载当前用户学习最新课程
		if(SessionContext.isLogin()){
			UserCourseSection userCourseSection = new UserCourseSection();
			userCourseSection.setUserId(SessionContext.getUserId());
			userCourseSection = this.userCourseSectionService.queryLatest(userCourseSection);
			if(null != userCourseSection){
				JSONObject jsObj = new JSONObject();
				CourseSection curCourseSection = this.courseSectionService.getById(userCourseSection.getSectionId());
				jsObj.put("curCourseSection", curCourseSection);
				Course curCourse = courseService.getById(userCourseSection.getCourseId());
				jsObj.put("curCourse", curCourse);
				jv.setData(jsObj);
			}
		}
		return jv.toString();
	}

	
}
