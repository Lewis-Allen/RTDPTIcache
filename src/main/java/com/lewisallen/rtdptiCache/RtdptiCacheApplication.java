package com.lewisallen.rtdptiCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lewisallen.rtdptiCache.requests.SIRIString;

@SpringBootApplication
public class RtdptiCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtdptiCacheApplication.class, args);
		
		new AppConfig();
		
		SIRIString xml = new SIRIString();
		xml.generateXml(new String[]{"123","124"});
	}
}
