package com.online.college.ocSpringBoot;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.college.domain.AuthUser;
import com.online.college.service.IAuthUserService;
import com.online.college.util.EncryptUtil;
import com.online.college.web.HttpHelper;
import com.online.college.web.JsonView;
import com.online.college.web.SessionContext;


/**
 * 用户登录 & 注册
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IAuthUserService authUserService;
	
	/**
	 * 注册页面
	 */
	@RequestMapping(value = "/register")
	public String register(Map<String,Object> model , HttpServletRequest request){
/*		if(SessionContext.isLogin()){
			return "redirect:/index.html";
		}*/
		return "auth/register";
	}
	
	/**
	 * 实现注册
	 */
	@RequestMapping(value = "/doRegister")
	@ResponseBody
	public String doRegister(AuthUser authUser, String identiryCode, HttpServletRequest request) {
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			return JsonView.render(2);
		}
		
		AuthUser tmpUser = authUserService.getByUsername(authUser.getUsername());
		if(tmpUser != null){
			return JsonView.render(1);
		}else{
			authUser.setPassword(EncryptUtil.encodedByMD5(authUser.getPassword()));
			authUserService.createSelectivity(authUser);
			return JsonView.render(0);
		}
	}
	
	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login")
	public String login(Map<String,Object> model , HttpServletRequest request){
/*		if(SessionContext.isLogin()){
			return "redirect:/index.html";
		}*/
		return "auth/login";
	}
	
	/**
	 * ajax登录
	 */
	@RequestMapping(value = "/ajaxlogin")
	@ResponseBody
	public String ajaxLogin(AuthUser user, String identiryCode, Integer rememberMe, HttpServletRequest request){
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			return JsonView.render(2, "验证码不正确！");
		}
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),EncryptUtil.encodedByMD5(user.getPassword()));
		try {
			if(rememberMe != null && rememberMe == 1){
				token.setRememberMe(true);
			}
			//shiro：不抛出异常，登录成功
			currentUser.login(token);
			return new JsonView().toString();
		}catch(AuthenticationException e){ 
			//shiro：抛出异常，登录失败
			return JsonView.render(1, "用户名或密码不正确");
		}
	}
	
	/**
	 * 实现登录
	 */
	@RequestMapping(value = "/doLogin")
	public String doLogin(AuthUser user, String identiryCode, Map<String,Object> model, HttpServletRequest request, HttpServletResponse response){
		
		//如果已经登录过
		if(SessionContext.getAuthUser() != null){
			return "redirect:/user/home.html";
		}
		
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			model.put("errcode", 1);
			return "auth/login";
		}
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),EncryptUtil.encodedByMD5(user.getPassword()));
		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);//shiro实现登录
			String url = "http://localhost:8080/user/home.html";
			HttpHelper.redirectHttpUrl(request, response, url);
			return null;
		}catch(AuthenticationException e){ //登录失败
			model.put("errcode", 2);
			return "auth/login";
		}
	}
	
	@RequestMapping(value = "/logout")
	public String logout(Map<String,Object> model, HttpServletRequest request) {
		SessionContext.shiroLogout();
		return "redirect:/index.html";
	}
	
}
