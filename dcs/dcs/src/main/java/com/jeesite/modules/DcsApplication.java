/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages={"com.jeesite.modules"})
@Slf4j
@EnableAsync
public class DcsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DcsApplication.class, args);
		log.info("################### user.dir=>{}", System.getProperty("user.dir"));
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DcsApplication.class);
	}
	
}