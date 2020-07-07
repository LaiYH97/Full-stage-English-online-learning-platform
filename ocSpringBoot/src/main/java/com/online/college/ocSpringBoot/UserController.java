package com.online.college.ocSpringBoot;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.online.college.domain.AuthUser;
import com.online.college.domain.CourseComment;
import com.online.college.domain.UserCollections;
import com.online.college.domain.UserCourseSection;
import com.online.college.domain.UserCourseSectionDto;
import com.online.college.domain.UserFollowStudyRecord;
import com.online.college.page.TailPage;
import com.online.college.service.IAuthUserService;
import com.online.college.service.ICourseCommentService;
import com.online.college.service.IUserCollectionsService;
import com.online.college.service.IUserCourseSectionService;
import com.online.college.service.IUserFollowsService;
import com.online.college.storage.QiniuStorage;
import com.online.college.util.EncryptUtil;
import com.online.college.web.JsonView;
import com.online.college.web.SessionContext;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//首页
	@Autowired
	private IUserFollowsService userFollowsService;

	//我的课程
	@Autowired
	private IUserCourseSectionService userCourseSectionService;
	
	//我的收藏
	@Autowired
	private IUserCollectionsService userCollectionsService;
	
	//个人信息
	@Autowired
	private IAuthUserService authUserService;
	
	//答疑
	@Autowired
	private ICourseCommentService courseCommentService;
	
	/**
	 * 首页
	 */
	@RequestMapping("/home")
	public String index(TailPage<UserFollowStudyRecord> page, Map<String,Object> model, HttpServletRequest request){
		/*ModelAndView mv = new ModelAndView("user/home");*/
		model.put("curNav","home");
		
		//加载关注用户的动态
		UserFollowStudyRecord queryEntity = new UserFollowStudyRecord();
		queryEntity.setUserId(SessionContext.getUserId());
		//设置每页显示3条动态
		page.setPageSize(3);
		page = userFollowsService.queryUserFollowStudyRecordPage(queryEntity, page);
		
		//处理用户头像
		for(UserFollowStudyRecord item : page.getItems()){
			if(StringUtils.isNotEmpty(item.getHeader())){
				item.setHeader(QiniuStorage.getUrl(item.getHeader()));
			}
		}
		model.put("page", page);
		
		return "user/home";
	}
	
	/**
	 * 我的课程
	 */
	@RequestMapping("/course")
	public String course(TailPage<UserCourseSectionDto> page, Map<String,Object> model, HttpServletRequest request){
		/*ModelAndView mv = new ModelAndView("user/course");*/
		model.put("curNav","course");
		
		UserCourseSection queryEntity = new UserCourseSection();
		queryEntity.setUserId(SessionContext.getUserId());
		page.setPageSize(3);
		page = userCourseSectionService.queryPage(queryEntity, page);
		model.put("page", page);
		
		return "user/course";
	}
	
	/**
	 * 我的收藏
	 */
	@RequestMapping("/collect")
	public String collect(TailPage<UserCollections> page, Map<String,Object> model, HttpServletRequest request){
		/*ModelAndView mv = new ModelAndView("user/collect");*/
		model.put("curNav","collect");
		UserCollections queryEntity = new UserCollections();
		queryEntity.setUserId(SessionContext.getUserId());
		page.setPageSize(3);
		page = userCollectionsService.queryPage(queryEntity, page);
		
		model.put("page", page);
		return "user/collect";
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info")
	public String info(Map<String,Object> model, HttpServletRequest request){
		model.put("curNav","info");
		
		AuthUser authUser = authUserService.getById(SessionContext.getUserId());
		if(null != authUser && StringUtils.isNotEmpty(authUser.getHeader())){
			authUser.setHeader(QiniuStorage.getUrl(authUser.getHeader()));
		}
		model.put("authUser",authUser);
		return "user/info";
	}
	
	/**
	 * 保存信息
	 */
	@RequestMapping("/saveInfo")
	@ResponseBody
	public String saveInfo(AuthUser authUser, @RequestParam MultipartFile pictureImg){
		try {
			authUser.setId(SessionContext.getUserId());
			if (null != pictureImg && pictureImg.getBytes().length > 0) {
				String key = QiniuStorage.uploadImage(pictureImg.getBytes());
				authUser.setHeader(key);
			}
			authUserService.updateSelectivity(authUser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonView().toString();
	}
	
	/**
	 * 密码
	 */
	@RequestMapping("/passwd")
	public String passwd(Map<String,Object> model, HttpServletRequest request){
		/*ModelAndView mv = new ModelAndView("user/passwd");*/
		model.put("curNav","passwd");
		return "user/passwd";
	}
	
	/**
	 * 密码
	 */
	@RequestMapping("/savePasswd")
	@ResponseBody
	public String savePasswd(String oldPassword, String password, String rePassword){
		AuthUser currentUser = authUserService.getById(SessionContext.getUserId());
		if(null == currentUser){
			return JsonView.render(1,"用户不存在！");
		}
		oldPassword = EncryptUtil.encodedByMD5(oldPassword.trim());
		if(!oldPassword.equals(currentUser.getPassword())){
			return JsonView.render(1,"旧密码不正确！");
		}
		if(StringUtils.isEmpty(password.trim())){
			return JsonView.render(1,"新密码不能为空！");
		}
		if(!password.trim().equals(rePassword.trim())){
			return JsonView.render(1,"新密码与重复密码不一致！");
		}
		currentUser.setPassword(EncryptUtil.encodedByMD5(password));
		authUserService.updateSelectivity(currentUser);
		return new JsonView().toString();
	}
	
	/**
	 * 问答
	 */
	@RequestMapping("/qa")
	public String qa(TailPage<CourseComment> page, Map<String,Object> model, HttpServletRequest request){
		/*ModelAndView mv = new ModelAndView("user/qa");*/
		model.put("curNav","qa");
		
		CourseComment queryEntity = new CourseComment();
		queryEntity.setUsername(SessionContext.getUsername());
		page.setPageSize(3);
		page = courseCommentService.queryMyQAItemsPage(queryEntity, page);
		model.put("page", page);
		
		return "user/qa";
	}
	
}
