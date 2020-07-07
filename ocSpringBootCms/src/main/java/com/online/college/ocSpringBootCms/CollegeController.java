package com.online.college.ocSpringBootCms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.domain.ConstsCollege;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsCollegeService;
import com.online.college.web.JsonView;

/**
 * 网校管理
 */
@Controller
@RequestMapping("/college")
public class CollegeController {

	@Autowired
	private IConstsCollegeService entityService;
	
	/**
	 * 分页
	 * @param queryEntity
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/queryPageList")
	public String queryPage(ConstsCollege queryEntity , TailPage<ConstsCollege> page , Map<String,Object> model , HttpServletRequest request){
		model.put("curNav", "college");
		
		if(StringUtils.isNotEmpty(queryEntity.getName())){
			queryEntity.setName(queryEntity.getName().trim());
		}else{
			queryEntity.setName(null);
		}
		
		page = entityService.queryPage(queryEntity,page);
		model.put("page",page);
		model.put("queryEntity",queryEntity);
		return "cms/college/collegePageList";
	}
	
	//根据id获取当前数据
	@RequestMapping(value = "/getById")
	@ResponseBody
	public String getById(Long id){
		return JsonView.render(entityService.getById(id));
	}
	
	//如果id为null则创建，不为null则更新
	@RequestMapping(value = "/doMerge")
	@ResponseBody
	public String doMerge(ConstsCollege entity){
		if(entity.getId() == null){
			ConstsCollege tmpEntity = entityService.getByCode(entity.getCode());
			if(tmpEntity != null){
				return JsonView.render(1, "此编码已存在");
			}
			entityService.createSelectivity(entity);
		}else{
			ConstsCollege tmpEntity = entityService.getByCode(entity.getCode());
			if(tmpEntity != null && !tmpEntity.getId().equals(entity.getId())){
				return JsonView.render(1, "此编码已存在");
			}
			entityService.updateSelectivity(entity);
		}
		return new JsonView().toString();
	}

	//逻辑删除
	@RequestMapping(value = "/deleteLogic")
	@ResponseBody
	public String deleteLogic(ConstsCollege entity){
		entityService.deleteLogic(entity);
		return new JsonView().toString();
	}
}
