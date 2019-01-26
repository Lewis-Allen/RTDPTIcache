package com.lewisallen.rtdptiCache.jobs;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.db.NaptanDatabase;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import com.lewisallen.rtdptiCache.requests.SIRIString;
import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2017_10_01.ldb.GetBoardRequestParams;
import com.thalesgroup.rtti._2017_10_01.ldb.LDBServiceSoap;
import com.thalesgroup.rtti._2017_10_01.ldb.Ldb;
import com.thalesgroup.rtti._2017_10_01.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2017_10_01.ldb.types.FormationData;
import com.thalesgroup.rtti._2017_10_01.ldb.types.ServiceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {
    
    private NaptanDatabase db = new NaptanDatabase();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private AccessToken accessToken;
    private Ldb soap;
    private LDBServiceSoap soapService;

    public ScheduledTasks(){
        soap = new Ldb();
        soapService = soap.getLDBServiceSoap12();

        accessToken = new AccessToken();
        accessToken.setTokenValue(AppConfig.ldbToken);
    }

    @Scheduled(fixedRate = 1000 * 30) // Thirty seconds
    public void updateCaches() {
        log.info("updateCaches: Starting task to update caches at {}", dateFormat.format(new Date()));
        
        updateNaPTANCache();
        updateSIRICache();
        updateTrainsCache();
        
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

    public void updateTrainsCache(){
        log.debug("updateTrainsCache: starting Trains cache update at {}", dateFormat.format(new Date()));

        GetBoardRequestParams params = new GetBoardRequestParams();

        params.setCrs("MCB");

        StationBoardResponseType departureBoard = soapService.getDepartureBoard(params, accessToken);
        System.out.println("Trains at " + departureBoard.getGetStationBoardResult().getLocationName());

        List<ServiceItem> service = departureBoard.getGetStationBoardResult().getTrainServices().getService();

        for(ServiceItem si : service){
            System.out.println(si.getStd() + " to " + si.getDestination().getLocation().get(0).getLocationName() + " - " + si.getEtd() + " - Platform " + si.getPlatform());
        }

        log.debug("updateTrainsCache: Trains cache update complete at {}", dateFormat.format(new Date()));
    }
}

