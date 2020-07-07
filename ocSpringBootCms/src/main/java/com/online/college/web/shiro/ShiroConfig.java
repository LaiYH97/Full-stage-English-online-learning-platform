package com.online.college.web.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.online.college.web.auth.AuthRealm;

/**
 * 在这里进行 Shiro 的配置
 */
@Configuration
public class ShiroConfig {
	
	/**
     * 定义shiroFilter过滤器并注入securityManager
     * 这里的配置其实和xml的配置大致是一样的
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    	//1.定义shiroFactoryBean
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //2.设置securityManager
        bean.setSecurityManager(securityManager);
        //3.设置默认登录的url
        bean.setLoginUrl("/auth/login.html");
        //4.设置成功之后要跳转的链接
        bean.setSuccessUrl("/");
        //5.设置未授权界面
        bean.setUnauthorizedUrl("/error/403.html");
        //6.LinkedHashMap是有序的，进行顺序拦截器配置
        Map<String, String> map = new LinkedHashMap<>();
        //7.配置logout过滤器
        map.put("/auth/logout.html", "logout");
        //8.所有url必须通过认证才可以访问
        map.put("/error/**", "anon");
        map.put("/res/**", "anon");
        map.put("/tools/**", "anon");
        map.put("/auth/**", "anon");     
        map.put("/**", "authc");
        //9.设置shiroFilterFactoryBean的FilterChainDefinitionMap
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }
    /**
     * @Description: 定义安全管理器securityManager，注入自定义的realm
     * 这里返回的是SessionsSecurityManager是因为在我们引入的stater包中存在的就是这个类
     */
	@Bean
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        ThreadContext.bind(manager);
        manager.setRealm(authRealm);
        return manager;
    }
    
	@Bean(name = "authRealm")
    public AuthRealm authRealm() {
		AuthRealm authRealm = new AuthRealm();
        return authRealm;
    }
    
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

/*	@Bean
	public AuthFilter authFilter(){
		return new AuthFilter();
	}*/
	

}
