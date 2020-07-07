package com.online.college.ocSpringBootCms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.online.college"})
@MapperScan({"com.online.college.dao","com.online.college.statics.dao"})
public class OcSpringBootCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OcSpringBootCmsApplication.class, args);
	}
}
