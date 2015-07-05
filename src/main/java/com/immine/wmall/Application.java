package com.immine.wmall;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.immine.wmall.config.WebappConfig;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import(WebappConfig.class)
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@PostConstruct
	public void logSomething() {
		logger.debug("Active Debug Message");
		logger.trace("Active Trace Message");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
}
