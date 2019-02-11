package com.lewisallen.rtdptiCache.tests;


import com.lewisallen.rtdptiCache.api.StopController;
import com.lewisallen.rtdptiCache.caches.SIRICache;
import com.lewisallen.rtdptiCache.caches.TrainDepartureCache;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class ApiTest {

    private StopController controller = new StopController();

    @Test
    void testEmptyRequest(){
        JSONObject req = new JSONObject();
        JSONObject res = new JSONObject(controller.stops(req).getBody());

        Assertions.assertTrue(res.has("payload"));
    }

    @Test
    void testDodgyRequest(){
        JSONObject req = new JSONObject();
        req.put("codes", "dfghjkdfghjkdfghjkl");
        JSONObject res = new JSONObject(controller.stops(req).getBody());

        Assertions.assertTrue(res.has("payload"));
        Assertions.assertTrue(res.getJSONObject("payload").has("busStops"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("busStops").length() == 1);
    }

    @Test
    void singleStopRequests() {
        SIRICache.siriCache = new HashMap<>();
        JSONObject req = new JSONObject();
        req.put("codes","14900000670");

        JSONObject res = new JSONObject(controller.stops(req).getBody());
        System.out.println(res.toString());
        Assertions.assertTrue(res.has("payload"));
        Assertions.assertTrue(res.getJSONObject("payload").has("busStops"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("busStops").has("14900000670"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("busStops").getJSONObject("14900000670").isEmpty());

        TrainDepartureCache.trainDepartureCache = new HashMap<>();
        req = new JSONObject();
        req.put("CRS", "MCB");

        res = new JSONObject(controller.stops(req).getBody());
        System.out.println(res.toString());
        Assertions.assertTrue(res.has("payload"));
        Assertions.assertTrue(res.getJSONObject("payload").has("trainStations"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("trainStations").has("MCB"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("trainStations").getJSONObject("MCB").isEmpty());
    }

    @Test
    void multipleStopRequests(){
        // Query multiple buses
        SIRICache.siriCache = new HashMap<>();
        JSONObject req = new JSONObject();
        JSONArray codes = new JSONArray();
        codes.put("14900000670");
        codes.put("14900000671");
        req.put("codes",codes);

        JSONObject res = new JSONObject(controller.stops(req).getBody());

        Assertions.assertTrue(res.has("payload"));
        Assertions.assertTrue(res.getJSONObject("payload").has("busStops"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("busStops").has("14900000670"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("busStops").has("14900000671"));

        // Query multiple trains.
        TrainDepartureCache.trainDepartureCache = new HashMap<>();
        req = new JSONObject();
        codes = new JSONArray();
        codes.put("MCB");
        codes.put("BTN");
        req.put("CRS",codes);

        res = new JSONObject(controller.stops(req).getBody());

        Assertions.assertTrue(res.has("payload"));
        Assertions.assertTrue(res.getJSONObject("payload").has("trainStations"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("trainStations").has("MCB"));
        Assertions.assertTrue(res.getJSONObject("payload").getJSONObject("trainStations").has("BTN"));
    }
}
