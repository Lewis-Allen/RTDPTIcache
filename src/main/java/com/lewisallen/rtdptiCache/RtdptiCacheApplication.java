package com.lewisallen.rtdptiCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RtdptiCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtdptiCacheApplication.class, args);
		
		new AppConfig();
	}
}
