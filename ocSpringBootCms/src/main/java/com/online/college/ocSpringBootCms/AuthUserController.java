package com.online.college.ocSpringBootCms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.domain.AuthUser;
import com.online.college.page.TailPage;
import com.online.college.service.IAuthUserService;
import com.online.college.web.JsonView;

@Controller
@RequestMapping("/user")
public class AuthUserController{

	@Autowired
	private IAuthUserService entityService;

	@RequestMapping(value = "/getById")
	@ResponseBody
	public String getById(Long id){
		AuthUser user = entityService.getById(id);
		return JsonView.render(user);
	}

	/**
	 * 分页
	 */
	@RequestMapping(value = "/userPageList")
	public String queryPage(AuthUser queryEntity , TailPage<AuthUser> page , Map<String,Object> model , HttpServletRequest request){
		model.put("curNav", "user");
		
		if(StringUtils.isNotEmpty(queryEntity.getUsername())){
			queryEntity.setUsername(queryEntity.getUsername().trim());
		}else{
			queryEntity.setUsername(null);
		}
		
		if(Integer.valueOf(-1).equals(queryEntity.getStatus())){
			queryEntity.setStatus(null);
		}
		
		page = entityService.queryPage(queryEntity,page);
		model.put("page",page);
		model.put("queryEntity",queryEntity);
		
		return "cms/user/userPageList";
	}
	
	@RequestMapping(value = "/doMerge")
	@ResponseBody
	public String doMerge(AuthUser entity){
		entity.setUsername(null);//不更新
		entity.setRealname(null);//不更新
		entityService.updateSelectivity(entity);
		return new JsonView(0).toString();
	}
	
}

