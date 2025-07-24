package com.shri.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SpringbootCorporateFGApplication {
	static Logger logger = LoggerFactory.getLogger(SpringbootCorporateFGApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootCorporateFGApplication.class, args);
		System.out.println("SpringBoot-CorporateFG Application run successfully .!");
		logger.info("SpringBoot-CorporateFG Application run successfully using Logger.!");
	}

}


