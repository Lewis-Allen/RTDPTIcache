package com.lewisallen.rtdptiCache.jobs;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import com.lewisallen.rtdptiCache.caches.TrainStationCache;
import com.lewisallen.rtdptiCache.db.TransportDatabase;
import com.lewisallen.rtdptiCache.parser.SIRIResponseParser;
import com.lewisallen.rtdptiCache.requests.SIRIRequester;
import com.lewisallen.rtdptiCache.requests.SIRIString;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ScheduledTasks
{

    private TransportDatabase db = new TransportDatabase();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private AccessToken accessToken;
    private Ldb soap;
    private LDBServiceSoap soapService;

    public ScheduledTasks()
    {
        soap = new Ldb();
        soapService = soap.getLDBServiceSoap12();

        accessToken = new AccessToken();
        accessToken.setTokenValue(AppConfig.ldbToken);
    }

    /**
     * Updates all caches.
     */
    @Scheduled(fixedRate = 1000 * 30) // Thirty seconds
    public void updateCaches()
    {
        log.info("updateCaches: Starting task to update caches at {}", dateFormat.format(new Date()));

        updateNaPTANCache();
        updateSIRICache();
        updateStationCache();
        updateTrainsDepartureCache();

        log.info("updateCaches: Caches update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the NaPTAN cache.
     */
    public void updateNaPTANCache()
    {
        log.debug("updateNaPTANCache: starting NaPTAN cache update at {}", dateFormat.format(new Date()));

        NaPTANCache.populateCache(this.db, NaPTANCache.naptanQuery);

        log.debug("updateNaPTANCache: NaPTAN cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the SIRI cache.
     */
    public void updateSIRICache()
    {
        log.debug("updateNaPTANCache: starting SIRI cache update at {}", dateFormat.format(new Date()));

        SIRIString xml = new SIRIString();
        String req = xml.generateXml(NaPTANCache.getCachedCodes().stream().toArray(String[]::new));
        SIRIRequester requester = new SIRIRequester();
        SIRIResponseParser parser = new SIRIResponseParser();
        parser.parse(requester.makeSIRIRequest(req));

        log.debug("updateNaPTANCache: SIRI cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the Station cache.
     */
    public void updateStationCache()
    {
        log.debug("updateStationCache: starting Stations cache update at {}", dateFormat.format(new Date()));

        TrainStationCache.populateCache(this.db, TrainStationCache.stationQuery);

        log.debug("updateStationCache: Station cache update complete at {}", dateFormat.format(new Date()));
    }

    /**
     * Updates the Departure cache.
     */
    public void updateTrainsDepartureCache()
    {
        log.debug("updateTrainsDepartureCache: starting Trains cache update at {}", dateFormat.format(new Date()));

        GetBoardRequestParams params = new GetBoardRequestParams();
        params.setTimeWindow(60); // Set time window to 60 minutes.

        Map<Object, JSONObject> temporaryDepartureCache = new HashMap<>();

        for (String crsCode : TrainStationCache.getCachedCodes())
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
            stationJSON.put("stationName", TrainStationCache.stationCache.get(crsCode).getStationName());
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
        TrainDepartureCache.trainDepartureCache = temporaryDepartureCache;

        log.debug("updateTrainsDepartureCache: Trains cache update complete at {}", dateFormat.format(new Date()));
    }
}

