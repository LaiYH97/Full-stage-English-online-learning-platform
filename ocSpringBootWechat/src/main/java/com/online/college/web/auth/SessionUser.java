package com.online.college.web.auth;

import java.util.Set;


/**
 * 权限用户
 */
public interface SessionUser {
	
	String getUsername();
	
	Long getUserId();
	
	Set<String> getPermissions();
	
}
