package com.lewisallen.rtdptiCache.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/*
    WARNING: Contains tests that rely on the data inserted by migrations.
    Ensure db is populated appropriately before running tests.
    Migrations located in db/migrations
 */

@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimetableControllerTest
{
    @Autowired
    private WebTestClient wtc;

    @Test
    void testTimetableScreenCreate()
    {
        this.wtc
                .get()
                .uri(builder -> builder.path("/timetable/create").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testViewTimetable()
    {
        this.wtc
                .get()
                .uri(builder -> builder.path("/timetable/1").build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testMissingTimetable()
    {
        this.wtc
                .get()
                .uri(builder -> builder.path("/timetable/10000").build())
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void testPostTimetable()
    {
        this.wtc
                .post()
                .uri(builder -> builder.path("/timetable").build())
                .body(BodyInserters.fromObject("data=Test Stop" + System.lineSeparator() + "18:00,UB1,Old Steine"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        // Make request again to make sure redirect to already created resource.
        this.wtc
                .post()
                .uri(builder -> builder.path("/timetable").build())
                .body(BodyInserters.fromObject("data=Test Stop" + System.lineSeparator() + "18:00,UB1,Old Steine"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}