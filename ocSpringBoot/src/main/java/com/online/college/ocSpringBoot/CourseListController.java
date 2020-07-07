package com.online.college.ocSpringBoot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.business.IPortalBusiness;
import com.online.college.domain.ConstsClassify;
import com.online.college.domain.Course;
import com.online.college.domain.CourseEnum;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsClassifyService;
import com.online.college.service.ICourseService;
import com.online.college.vo.ConstsClassifyVO;

/**
 * 课程分类页
 */

@Controller
@RequestMapping("/course")
public class CourseListController {
	
	@Autowired
	private IConstsClassifyService constsClassifyService;
	
	@Autowired
	private IPortalBusiness portalBusiness;
	
	@Autowired
	private ICourseService courseService;
	
	/**
	 * 课程分类页
	 * @param c 分类code
	 * @param sort 排序
	 * @param page 分页
	 */
	@RequestMapping("/list")
	public String list(String c, String sort, TailPage<Course> page, Map<String,Object> model , HttpServletRequest request){
		
		//当前方向，-1代表方向的全部
		String curCode = "-1";
		//当前分类，-2代表分类的全部
		String curSubCode = "-2";
		
		//加载所有课程分类，展示一级分类与二级分类的联动
		Map<String,ConstsClassifyVO> classifyMap = portalBusiness.queryAllClassifyMap();
		
		//1、先加载出所有一级分类并遍历出来
		List<ConstsClassifyVO> classifysList = new ArrayList<ConstsClassifyVO>();
		for(ConstsClassifyVO vo : classifyMap.values()){
			classifysList.add(vo);
		}
		model.put("classifys", classifysList);
				
		//2、判断当前选中的分类
		ConstsClassify curClassify = constsClassifyService.getByCode(c);
		
		//2-1、当前没有选择分类，加载所有二级分类并遍历出来
		if(null == curClassify){
			List<ConstsClassify> subClassifys = new ArrayList<ConstsClassify>();
			for(ConstsClassifyVO vo : classifyMap.values()){
				subClassifys.addAll(vo.getSubClassifyList());
			}
			model.put("subClassifys", subClassifys);
		}else{
			//2-2、当前选中的是二级分类，	加载此分类平级的二级分类
			if(!"0".endsWith(curClassify.getParentCode())){
				curSubCode = curClassify.getCode();
				curCode = curClassify.getParentCode();
				model.put("subClassifys", classifyMap.get(curClassify.getParentCode()).getSubClassifyList());
			}else{
			//2-3、当前选中的是一级分类，加载此分类下的二级分类
				curCode = curClassify.getCode();
				model.put("subClassifys", classifyMap.get(curClassify.getCode()).getSubClassifyList());
			}
		}
		model.put("curCode", curCode);
		model.put("curSubCode", curSubCode);
		
		//分页排序数据
		//分页的分类参数
		Course queryEntity = new Course();
		if(!"-1".equals(curCode)){
			queryEntity.setClassify(curCode);
		}
		if(!"-2".equals(curSubCode)){
			queryEntity.setSubClassify(curSubCode);
		}
		
		//排序参数
		if("pop".equals(sort)){//最热
			page.descSortField("studyCount");
		}else{
			sort = "last";//最新
			page.descSortField("id");
		}
		model.put("sort", sort);
		
		//分页参数
		queryEntity.setOnsale(CourseEnum.ONSALE.value());
		//设置一页显示5个
		page.setPageSize(5);
		page = this.courseService.queryPage(queryEntity, page);
		model.put("page", page);
		
		return "list";
	}
}
