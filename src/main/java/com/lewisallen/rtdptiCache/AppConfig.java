package com.lewisallen.rtdptiCache;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class AppConfig {
	
	public static String siriUri;
	
	public AppConfig(){
		Dotenv dotenv = Dotenv.load();
		
		AppConfig.siriUri = dotenv.get("SIRI_URI");
	}
	
}
