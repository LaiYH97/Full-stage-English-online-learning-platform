package com.online.college.ocSpringBootCms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.web.SessionContext;

/**
 * 后台管理
 */
@Controller
@RequestMapping()
public class CmsController {

	/**
	 * 首页
	 */
	@RequestMapping("/index")
	public String index(Map<String,Object> model , HttpServletRequest request){
		if(SessionContext.isLogin()){
			/*ModelAndView mv = new ModelAndView("cms/index");*/
			model.put("curNav", "home");
			return "cms/index";
		}else{
			return "auth/login";
		}
	}
}
