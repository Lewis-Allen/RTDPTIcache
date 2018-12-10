package com.lewisallen.rtdptiCache;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@PropertySource("application.properties")
public class AppConfig {
	
	public AppConfig(){
		Dotenv dotenv = Dotenv.load();
		
		AppConfig.siriUri = dotenv.get("SIRI_URI");
	}
	
	public static String siriUri;
}
