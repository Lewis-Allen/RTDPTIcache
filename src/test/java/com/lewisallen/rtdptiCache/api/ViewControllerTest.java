package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.AppConfig;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
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

/**
 * TODO: Redo these tests.
 * Many of these tests assume that a particular stop has been configured to be retrieved in the database.
 * If this is not the case, the test will fail.
 * Need to explore some different ways of testing these classes.
 */

@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ViewControllerTest {

    @Autowired
    private WebTestClient wtc;

    @BeforeAll
    void setup() {
        new AppConfig();
        ScheduledTasks tasks = new ScheduledTasks();
        tasks.updateCaches();
    }

    @Test
    void showTitlePage() {
        this.wtc
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    @EnabledIfEnvironmentVariable(named="CI", matches="true")
    void showDashboardCI()
    {
        AppConfig.updateFromSystem();
        showDashboard();
    }

    @Test
    @DisabledIfEnvironmentVariable(named="CI", matches="true")
    void showDashboard() {
        String[] multipleCodes = new String[]{"149000006070", "149000007070"};
        String[] multipleCRS = new String[]{"BTN", "MCB"};
        String[] singleCode = new String[]{"149000007070"};
        String[] singleCRS = new String[]{"MCB"};

        // Test dashboard with no provided parameters.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard").build())
                .exchange()
                .expectStatus()
                .is5xxServerError();

        // Test dashboard with multiple bus/train codes.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("code[]", multipleCodes)
                        .queryParam("crs[]", multipleCRS).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();


        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("code[]", multipleCodes)
                        .queryParam("crs[]", singleCRS).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("code[]", singleCode)
                        .queryParam("crs[]", multipleCRS).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Test dashboard with bus codes only.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("code[]", singleCode).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Test instances of multiple codes.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("code[]", multipleCodes).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        // Test dashboard with train codes only.
        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("crs[]", singleCRS).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        this.wtc
                .get()
                .uri(builder -> builder.path("/dashboard")
                        .queryParam("crs[]", multipleCRS).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}
