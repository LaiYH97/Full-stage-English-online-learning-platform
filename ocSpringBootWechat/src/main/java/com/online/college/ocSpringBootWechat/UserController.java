package com.online.college.ocSpringBootWechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.domain.UserCourseSection;
import com.online.college.domain.UserCourseSectionDto;
import com.online.college.page.TailPage;
import com.online.college.service.IUserCourseSectionService;
import com.online.college.web.SessionContext;

/**
 * 用户controller
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserCourseSectionService userCourseSectionService;
	
	/**
	 * 个人主页
	 */
	@RequestMapping("/index")
	public String index(Map<String,Object> model , HttpServletRequest request, TailPage<UserCourseSectionDto> page){
		
		//当前用户id
		Long userId = SessionContext.getWxUserId(request);
		if(null == userId){
			return "redirect:/auth/login.html"; 
		}
		
		//获取学习记录
		UserCourseSection queryEntity = new UserCourseSection();
		queryEntity.setUserId(userId);
		
		page = userCourseSectionService.queryPage(queryEntity, page);
		model.put("page", page);
		
		//当前用户
		model.put("curUser", SessionContext.getWxAuthUser(request));
		return "user";
		
	}
	
}
