package com.online.college.ocSpringBootWechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.online.college"})
@MapperScan("com.online.college.dao")
public class OcSpringBootWechatApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(OcSpringBootWechatApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(OcSpringBootWechatApplication.class);
    }
}
