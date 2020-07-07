package com.online.college.ocSpringBootCms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.domain.AuthUser;
import com.online.college.util.EncryptUtil;
import com.online.college.web.SessionContext;


/**
 * 用户登录 & 注册
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
		
	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login")
	public String login(Map<String,Object> model , HttpServletRequest request){
		if(SessionContext.isLogin()){
			return "redirect:/index.html";
		}
		return "auth/login";
	}
		
	/**
	 * 实现登录
	 */
	@RequestMapping(value = "/doLogin")
	public String doLogin(AuthUser user, String identiryCode, Map<String,Object> model, HttpServletRequest request, HttpServletResponse response){
		
		//如果已经登录过
		if(SessionContext.getAuthUser() != null){
			return "redirect:/index.html";
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
			return "redirect:/index.html";
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
