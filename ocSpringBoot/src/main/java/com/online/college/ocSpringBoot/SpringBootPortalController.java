package com.online.college.ocSpringBoot;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.business.IPortalBusiness;
import com.online.college.domain.AuthUser;
import com.online.college.domain.ConstsSiteCarousel;
import com.online.college.domain.Course;
import com.online.college.domain.CourseEnum;
import com.online.college.domain.CourseQueryDto;
import com.online.college.service.IAuthUserService;
import com.online.college.service.IConstsSiteCarouselService;
import com.online.college.service.ICourseService;
import com.online.college.vo.ConstsClassifyVO;

@Controller
@RequestMapping()
public class SpringBootPortalController {
	//轮播
	@Autowired
	private IConstsSiteCarouselService siteCarouselService;
	
	//课程分类
	@Autowired
	private IPortalBusiness portalBusiness;
	
	//实战免费
	@Autowired
	private ICourseService courseService;
	
	//名校导师
	@Autowired
	private IAuthUserService authUserService;
	
	@RequestMapping("/index")
	public String index(Map<String,Object> model , HttpServletRequest request){
					
		//加载轮播
		List<ConstsSiteCarousel> carouselList = siteCarouselService.queryCarousels(4);
		model.put("carouselList", carouselList);
		
		//课程分类(一级分类）
		List<ConstsClassifyVO> classifys = portalBusiness.queryAllClassify();
		
		//课程推荐
		portalBusiness.prepareRecomdCourses(classifys);
		model.put("classifys", classifys);
		
		//实战推荐，根据权重（weight）进行排序
		CourseQueryDto queryEntity = new CourseQueryDto();
		queryEntity.setCount(5);//5门
		queryEntity.setFree(CourseEnum.FREE_NOT.value());
		queryEntity.descSortField("weight");//按照weight降序排列
		List<Course> actionCourseList = this.courseService.queryList(queryEntity);
		model.put("actionCourseList", actionCourseList);
		
		//免费好课，根据权重（weight）进行排序
		queryEntity.setFree(CourseEnum.FREE.value());
		List<Course> freeCourseList = this.courseService.queryList(queryEntity);
		model.put("freeCourseList", freeCourseList);
		
		//获取7门新视野大学英语课程，根据权重（学习数量studyCount）进行排序
		queryEntity.setCount(7);
		queryEntity.setFree(null);//不分实战和免费类别
		queryEntity.setSubClassify("新视野大学英语");//新视野大学英语分类
		queryEntity.descSortField("studyCount");//按照studyCount降序排列
		List<Course> javaCourseList = this.courseService.queryList(queryEntity);
		model.put("javaCourseList", javaCourseList);
		
		//名校讲师
		List<AuthUser> recomdTeacherList = authUserService.queryRecomd();
		model.put("recomdTeacherList", recomdTeacherList);
		
		return "index";
	}
	
}
