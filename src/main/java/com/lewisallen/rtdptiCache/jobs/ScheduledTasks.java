package com.lewisallen.rtdptiCache.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.db.NaptanDatabase;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import com.lewisallen.rtdptiCache.requests.SIRIString;
    
@Component
public class ScheduledTasks {
    
    private NaptanDatabase db = new NaptanDatabase();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 1000 * 30) // Thirty seconds
    public void updateCaches() {
        log.info("updateCaches: Starting task to update caches at {}", dateFormat.format(new Date()));
        
        updateNaPTANCache();
        updateSIRICache();
        
        log.info("updateCaches: Caches update complete at {}", dateFormat.format(new Date()));
    }
    
    public void updateNaPTANCache(){
        log.debug("updateNaPTANCache: starting NaPTAN cache update at {}", dateFormat.format(new Date()));
        
        NaPTANCache.populateCache(this.db);
        
        log.debug("updateNaPTANCache: NaPTAN cache update complete at {}", dateFormat.format(new Date()));
    }
    
    public void updateSIRICache(){
        log.debug("updateNaPTANCache: starting SIRI cache update at {}", dateFormat.format(new Date()));
        
        SIRIString xml = new SIRIString();
        String req = xml.generateXml(NaPTANCache.getCachedCodes().stream().toArray(String[]::new));
        SIRIRequester requester = new SIRIRequester();
        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(requester.makeSIRIRequest(req));
        
        log.debug("updateNaPTANCache: SIRI cache update complete at {}", dateFormat.format(new Date()));
    }
}

