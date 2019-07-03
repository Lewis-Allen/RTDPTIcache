package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.caches.BusDataCache;
import com.lewisallen.rtdptiCache.caches.TrainDataCache;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    WARNING: Contains tests that rely on the data inserted by migrations.
    Ensure db is populated appropriately before running tests.
    Migrations located in db/migrations
 */

@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DashboardControllerTest
{
    @Autowired
    private WebTestClient wtc;

    @BeforeAll
    void setup()
    {
        new AppConfig();
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateCaches();
    }

    @Test
    void showDashboardCreatePage()
    {
        this.wtc
                .get()
                .uri("/dashboard/create")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "CI", matches = "true")
    void showDefaultDashboardCI()
    {
        AppConfig.updateFromSystem();
        testGetDashboard();
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void testGetDashboard()
    {
        // Test dashboard with that does not exist.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/10000").build())
                .exchange()
                .expectStatus()
                .is4xxClientError();

        // One bus.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/1").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // One train.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/2").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Multiple buses.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/3").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Multiple trains.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/4").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Flipping
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/5").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/6").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Name override.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/7").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Test existing stop but no data
        // Wipe Bus Departure cache
        BusDataCache.siriCache = new HashMap<>();
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/1").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Wipe Train Departures
        TrainDataCache.trainDepartureCache = new HashMap<>();

        // Test empty station
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard/2").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testPostDashboard()
    {
        JSONObject body = new JSONObject();
        List<String> codes = new ArrayList<>();

        codes.add("149000007939");
        body.put("buses", codes);
        body.put("template", "single");
        body.put("flipTo", "default");

        this.wtc
                .post()
                .uri(builder -> builder.path("/dashboard").build())
                .body(BodyInserters.fromObject(body.toString()))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        this.wtc
                .post()
                .uri(builder -> builder.path("/dashboard").build())
                .body(BodyInserters.fromObject(body.toString()))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}