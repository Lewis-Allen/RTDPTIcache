package com.lewisallen.rtdptiCache;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class AppConfig implements SchedulingConfigurer
{
    public static String siriUri;
    public static String ldbToken;

    public AppConfig()
    {
        updateCredentials();
    }

    public static void updateCredentials()
    {
        String shouldUpdateFromSystem = System.getenv("RTDPTI_UPDATE_FROM_SYSTEM");
        if (shouldUpdateFromSystem != null && shouldUpdateFromSystem.equals("1"))
        {
            updateFromSystem();
        }
        else
        {
            Dotenv dotenv = Dotenv.load();
            AppConfig.siriUri = dotenv.get("SIRI_URI");
            AppConfig.ldbToken = dotenv.get("LDB_TOKEN");
        }
    }

    /**
     * Method to update properties from system variables.
     * Used for continuous integration and heroku deployment.
     */
    public static void updateFromSystem()
    {
        AppConfig.siriUri = System.getenv("RTDPTI_SIRI_URI");
        AppConfig.ldbToken = System.getenv("RTDPTI_LDB_TOKEN");
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar)
    {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(4);
        taskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
    }
}
