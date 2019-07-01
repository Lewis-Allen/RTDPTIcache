package com.lewisallen.rtdptiCache.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ViewControllerTest
{

    @Autowired
    private WebTestClient wtc;

    @Test
    void showTitlePage()
    {
        this.wtc
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}
