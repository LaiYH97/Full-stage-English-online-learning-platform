package com.online.college.web.shiro;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;


/**
 * Description: freemarker配置shiro
 */

@Configuration
public class ShiroFreeMarkerConfigurer{
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
	    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
	    freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
	    freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
	    configuration.setDefaultEncoding("UTF-8");
	    //这里可以添加其他共享变量 比如sso登录地址
	    configuration.setSharedVariable("shiro", new ShiroTags());
	    freeMarkerConfigurer.setConfiguration(configuration);
	    return freeMarkerConfigurer;
	}

}
