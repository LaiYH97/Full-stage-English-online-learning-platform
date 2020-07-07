package com.online.college.ocSpringBootWechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.domain.Course;
import com.online.college.domain.CourseEnum;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseService;

/**
 * M站
 */
@Controller
@RequestMapping()
public class IndexController {

	@Autowired
	private ICourseService courseService;
	
	//memcache客户端
	/*@Autowired
	MemcachedClient memcachedClient;*/
	
	/**
	 * 首页
	 */
	@RequestMapping("/index")
	public String index(TailPage<Course> page , Map<String,Object> model , HttpServletRequest request){
		
		//只展示第一页的课程
		Course queryEntity = new Course();
		queryEntity.setOnsale(CourseEnum.ONSALE.value());
		
		page.descSortField("weight");
		/*page.setPageSize(5);*/
		page = this.courseService.queryPage(queryEntity, page);
		model.put("page", page);
		return "index";
	}
	
}
