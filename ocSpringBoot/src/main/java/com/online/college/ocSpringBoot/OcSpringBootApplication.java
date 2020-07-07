package com.online.college.ocSpringBoot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.online.college"})
@MapperScan("com.online.college.dao")
public class OcSpringBootApplication{
	   
	public static void main(String[] args) {
		SpringApplication.run(OcSpringBootApplication.class, args);
	}
}
