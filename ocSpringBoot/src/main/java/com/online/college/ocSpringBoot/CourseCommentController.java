package com.online.college.ocSpringBoot;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.domain.CourseComment;
import com.online.college.domain.CourseSection;
import com.online.college.page.TailPage;
import com.online.college.service.ICourseCommentService;
import com.online.college.service.ICourseSectionService;
import com.online.college.storage.QiniuStorage;
import com.online.college.web.JsonView;
import com.online.college.web.SessionContext;

/**
 * 课程评论管理
 */
@Controller
@RequestMapping("/courseComment")
public class CourseCommentController {
	
	@Autowired
	private ICourseCommentService courseCommentService;
	
	@Autowired
	private ICourseSectionService courseSectionService;
	
	/**
	 * 加载评论&答疑
	 * sectionId：章节id
	 * courseId ：课程id
	 * type : 类型
	 * @return
	 */
	@RequestMapping("/segment")
	public String segment(CourseComment queryEntity , TailPage<CourseComment> page , Map<String,Object> model , HttpServletRequest request){
		if(null == queryEntity.getCourseId() || queryEntity.getType() == null)
			return "error/404"; 
	
		TailPage<CourseComment> commentPage = this.courseCommentService.queryPage(queryEntity, page);
		commentPage.setPageSize(5);
		
		//处理用户头像
		for(CourseComment item : commentPage.getItems()){
			if(StringUtils.isNotEmpty(item.getHeader())){
				item.setHeader(QiniuStorage.getUrl(item.getHeader()));
			}
		}
		model.put("page", commentPage);
		return "commentSegment";
	}
	
	/**
	 * 发表评论
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/doComment")
	@ResponseBody
	public String doComment(HttpServletRequest request, CourseComment entity,String indeityCode){
		
		//验证码判断，验证码错误
		if(null == indeityCode || 
				(indeityCode != null && !indeityCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request)))){
			return new JsonView(2).toString();
		}
		
		//文字太长，文字太长或者为空
		if(entity.getContent().trim().length() > 200 || entity.getContent().trim().length() == 0){
			return new JsonView(3).toString();
		}
		
		//来自于个人中心评论
		if(null != entity.getRefId()){
			CourseComment refComment = this.courseCommentService.getById(entity.getRefId());
			if(null != refComment){
				CourseSection courseSection = courseSectionService.getById(refComment.getSectionId());
				if(null != courseSection){
					entity.setRefContent(refComment.getContent());
					entity.setRefId(entity.getRefId());
					entity.setCourseId(refComment.getCourseId());
					entity.setSectionId(refComment.getSectionId());
					entity.setSectionTitle(courseSection.getName());
					
					entity.setToUsername(refComment.getUsername());//引用的评论的username
					entity.setUsername(SessionContext.getUsername());
					entity.setCreateTime(new Date());
					entity.setCreateUser(SessionContext.getUsername());
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(SessionContext.getUsername());
					
					this.courseCommentService.createSelectivity(entity);
					return new JsonView(0).toString();
				}
			}
		}else{
			CourseSection courseSection = courseSectionService.getById(entity.getSectionId());
			if(null != courseSection){
				entity.setSectionTitle(courseSection.getName());
				entity.setToUsername(entity.getCreateUser());//toUsername可以作为页面入参
				entity.setUsername(SessionContext.getUsername());
				entity.setCreateTime(new Date());
				entity.setCreateUser(SessionContext.getUsername());
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(SessionContext.getUsername());
				
				this.courseCommentService.createSelectivity(entity);
				return new JsonView(0).toString();
			}
		}
		return new JsonView(1).toString();
	}
	
}

