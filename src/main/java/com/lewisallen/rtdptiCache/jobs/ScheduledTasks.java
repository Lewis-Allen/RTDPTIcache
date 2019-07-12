package com.lewisallen.rtdptiCache.jobs;

import com.lewisallen.rtdptiCache.busInterfacer.SIRIRequester;
import com.lewisallen.rtdptiCache.busInterfacer.SIRIResponseParser;
import com.lewisallen.rtdptiCache.busInterfacer.SIRIString;
import com.lewisallen.rtdptiCache.caches.BusCodesCache;
import com.lewisallen.rtdptiCache.caches.Caches;
import com.lewisallen.rtdptiCache.caches.TrainCodesCache;
import com.thalesgroup.rtti._2012_01_13.ldb.types.ArrayOfNRCCMessages;
import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2017_10_01.ldb.GetBoardRequestParams;
import com.thalesgroup.rtti._2017_10_01.ldb.LDBServiceSoap;
import com.thalesgroup.rtti._2017_10_01.ldb.Ldb;
import com.thalesgroup.rtti._2017_10_01.ldb.StationBoardResponseType;
import com.thalesgroup.rtti._2017_10_01.ldb.types.ServiceItem;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScheduledTasks
{
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private AccessToken accessToken;
    private Ldb soap;
    private LDBServiceSoap soapService;

    @Value("${rtdpti.ldbtoken}")
    private String ldbToken;

    @Value("${rtdpti.siriuri}")
    private String siriuri;

    @PostConstruct
    public void init()
    {
        accessToken = new AccessToken();
        accessToken.setTokenValue(ldbToken);

        soap = new Ldb();
        soapService = soap.getLDBServiceSoap();
    }

    /**
     * Updates all caches.
     */
    public void updateCaches()
    {
        LocalDateTime start = LocalDateTime.now();
        updateBusCodesCache();
        updateSIRICache();
        updateStationCache();
        updateTrainsDepartureCache();
        log.info("updateCaches: Caches update complete at {}, taking {} milliseconds on {}",
                dateFormat.format(new Date()), start.until(LocalDateTime.now(), ChronoUnit.MILLIS), Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 1000 * 30)
    public void updateBusCaches()
    {
        LocalDateTime start = LocalDateTime.now();
        updateBusCodesCache();
        updateSIRICache();
        log.info("updateBusCaches: Buses update complete at {}, taking {} milliseconds on {}",
                dateFormat.format(new Date()), start.until(LocalDateTime.now(), ChronoUnit.MILLIS), Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 1000 * 15)
    public void updateTrainCaches()
    {
        LocalDateTime start = LocalDateTime.now();
        updateStationCache();
        updateTrainsDepartureCache();
        log.info("updateStationCaches: Stations update complete at {}, taking {} milliseconds on {}",
                dateFormat.format(new Date()), start.until(LocalDateTime.now(), ChronoUnit.MILLIS), Thread.currentThread().getName());
    }

    /**
     * Updates the NaPTAN cache.
     */
    public void updateBusCodesCache()
    {
        log.debug("updateBusCodesCache: starting NaPTAN cache update at {}", dateFormat.format(new Date()));

        BusCodesCache.populateCache();

        log.debug("updateBusCodesCache: NaPTAN cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the SIRI cache.
     */
    public void updateSIRICache()
    {
        log.debug("updateBusCodesCache: starting SIRI cache update at {}", dateFormat.format(new Date()));

        SIRIString xml = new SIRIString();
        String req = xml.generateXml(BusCodesCache.getCachedCodes().stream().toArray(String[]::new));
        SIRIRequester requester = new SIRIRequester(siriuri);
        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(requester.makeSIRIRequest(req));

        log.debug("updateBusCodesCache: SIRI cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the Station cache.
     */
    public void updateStationCache()
    {
        log.debug("updateStationCache: starting Stations cache update at {}", dateFormat.format(new Date()));

        TrainCodesCache.populateCache();

        log.debug("updateStationCache: Station cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the Departure cache.
     */
    public void updateTrainsDepartureCache()
    {
        log.debug("updateTrainsDepartureCache: starting Trains cache update at {}", dateFormat.format(new Date()));

        // TODO: split up the method into multiple

        GetBoardRequestParams params = new GetBoardRequestParams();
        params.setTimeWindow(60); // Set time window to 60 minutes.

        Map<Object, JSONObject> temporaryDepartureCache = new ConcurrentHashMap<>();

        for (String crsCode : TrainCodesCache.getCachedCodes())
        {
            params.setCrs(crsCode);
            params.setNumRows(15);

            // Query the station data from the Darwin API.
            StationBoardResponseType departureBoard = soapService.getDepartureBoard(params, accessToken);

            // Create a list of hold the departures for this station.
            List<JSONObject> thisStationDepartures = new ArrayList<>();

            if (departureBoard.getGetStationBoardResult().getTrainServices() != null)
            {
                List<ServiceItem> service = departureBoard.getGetStationBoardResult().getTrainServices().getService();

                String due, destination, status, platform;

                // Create a list of JSON Objects for the departures from this station.
                thisStationDepartures = new ArrayList<>();

                for (ServiceItem si : service)
                {
                    due = si.getStd();
                    destination = si.getDestination().getLocation().get(0).getLocationName();
                    status = si.getEtd();
                    platform = si.getPlatform();

                    JSONObject departure = new JSONObject();
                    departure.put("due", due);
                    departure.put("destination", destination);
                    departure.put("status", status);
                    departure.put("platform", platform);

                    thisStationDepartures.add(departure);
                }
            }

            // JSON Object for a stations departures
            JSONObject stationJSON = new JSONObject();
            stationJSON.put("stationName", TrainCodesCache.stationCache.get(crsCode).getStationName());
            stationJSON.put("departures", thisStationDepartures);

            // Add any messages to the JSON.
            ArrayOfNRCCMessages nrccMessages = departureBoard.getGetStationBoardResult().getNrccMessages();
            if (nrccMessages != null)
            {
                stationJSON.put("nrccMessages", nrccMessages.getMessage());
            }

            // Add the final station object to the temporary cache.
            temporaryDepartureCache.put(crsCode, stationJSON);
        }

        // Replace the global cache with the just created one.
        Caches.resetTrainData(temporaryDepartureCache);

        log.debug("updateTrainsDepartureCache: Trains cache update complete at {}", dateFormat.format(new Date()));
    }
}

