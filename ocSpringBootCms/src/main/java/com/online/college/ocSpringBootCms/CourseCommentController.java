package com.online.college.ocSpringBootCms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.domain.CourseComment;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseCommentService;
import com.online.college.storage.QiniuStorage;
import com.online.college.web.JsonView;

/**
 * 课程评论管理
 */
@Controller
@RequestMapping("/courseComment")
public class CourseCommentController {
	
	@Autowired
	private ICourseCommentService courseCommentService;
	
	/**
	 * 课程评论管理
	 */
	//评论的加载
	@RequestMapping("/pagelist")
	public String commentSegment(CourseComment queryEntity , TailPage<CourseComment> page , Map<String,Object> model , HttpServletRequest request){
		queryEntity.setCourseId(1L);
		TailPage<CourseComment> commentPage = this.courseCommentService.queryPage(queryEntity, page);
		//处理用户头像
		for(CourseComment item : commentPage.getItems()){
			if(StringUtils.isNotEmpty(item.getHeader())){
				item.setHeader(QiniuStorage.getUrl(item.getHeader()));
			}
		}
		model.put("page", commentPage);
		model.put("queryEntity", queryEntity);
		return "cms/course/readComment";
	}
	
	//评论的删除
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(CourseComment entity){
		courseCommentService.delete(entity);
		return new JsonView().toString();
	}
	
}

