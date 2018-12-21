package com.lewisallen.rtdptiCache;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import com.lewisallen.rtdptiCache.requests.SIRIString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RtdptiCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtdptiCacheApplication.class, args);
		
		new AppConfig();
	}
}
