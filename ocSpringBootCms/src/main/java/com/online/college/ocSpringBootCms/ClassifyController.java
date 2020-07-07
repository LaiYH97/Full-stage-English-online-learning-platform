package com.online.college.ocSpringBootCms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.business.IPortalBusiness;
import com.online.college.domain.ConstsClassify;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsClassifyService;
import com.online.college.vo.ConstsClassifyVO;
import com.online.college.web.JsonView;

/**
 * 课程分类管理
 */
@Controller
@RequestMapping("/classify")
public class ClassifyController{

	@Autowired
	private IConstsClassifyService entityService;
	
	@Autowired
	private IPortalBusiness portalBusiness;
	

	@RequestMapping(value = "/getById")
	@ResponseBody
	public String getById(Long id){
		return JsonView.render(entityService.getById(id));
	}

	@RequestMapping(value = "/index")
	public String classifyIndex(ConstsClassify queryEntity , TailPage<ConstsClassify> page , Map<String,Object> model , HttpServletRequest request){
		model.put("curNav", "classify");
		//获取整个课程列表
		Map<String,ConstsClassifyVO> classifyMap = portalBusiness.queryAllClassifyMap();
		
		//获取出所有一级分类
		List<ConstsClassifyVO> classifysList = new ArrayList<ConstsClassifyVO>();
		for(ConstsClassifyVO vo : classifyMap.values()){
			classifysList.add(vo);
		}
		model.put("classifys", classifysList);
		
		//获取所有一级分类所对应的二级分类
		List<ConstsClassify> subClassifys = new ArrayList<ConstsClassify>();
		for(ConstsClassifyVO vo : classifyMap.values()){
			subClassifys.addAll(vo.getSubClassifyList());
		}
		model.put("subClassifys", subClassifys);
		
		return "cms/classify/classifyIndex";
	}

	@RequestMapping(value = "/doMerge")
	@ResponseBody
	public String doMerge(ConstsClassify entity){
		if(entity.getId() == null){
			ConstsClassify tmpEntity = entityService.getByCode(entity.getCode());
			if(tmpEntity != null){
				return JsonView.render(1, "此编码已存在");
			}
			entityService.createSelectivity(entity);
		}else{
			entityService.updateSelectivity(entity);
		}
		return new JsonView().toString();
	}

	@RequestMapping(value = "/deleteLogic")
	@ResponseBody
	public String deleteLogic(ConstsClassify entity){
		entityService.deleteLogic(entity);
		return new JsonView().toString();
	}
	
	
}

