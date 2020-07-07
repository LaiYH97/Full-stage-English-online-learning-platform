package com.online.college.web;

import javax.servlet.http.HttpServletRequest;

import com.online.college.web.auth.SessionUser;
/**
 * session工具类
 */
public class SessionContext {
	public static final String IDENTIFY_CODE_KEY = "_consts_identify_code_key_";// 其他人不得占用
	public static final String AUTH_USER_KEY = "_consts_auth_user_key_";// 其他人不得占用
	
	public static Long getWxUserId(HttpServletRequest request){
		if(null != getWxAuthUser(request))
			return getWxAuthUser(request).getUserId();
		return null;
	}
	
	public static String getWxUsername(HttpServletRequest request){
		if(null != getWxAuthUser(request))
			return getWxAuthUser(request).getUsername();
		return null;
	}
	
	public static boolean isWxLogin(HttpServletRequest request){
		return null != getWxAuthUser(request);
	}
	
	// 如果是微信登录获取当前用户
	public static SessionUser getWxAuthUser(HttpServletRequest request){
		String sessionId = request.getSession().getId();
		Object obj = SessionContext.getAttribute(request, sessionId);
		if(null != obj){
			return (SessionUser)obj;
		}
		return null;
	}
	
	// 获取属性
	public static Object getAttribute(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}

	// 设置属性
	public static void setAttribute(HttpServletRequest request, String key, Object value) {
		request.getSession().setAttribute(key, value);
	}

	// 删除属性
	public static void removeAttribute(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}
	


}
