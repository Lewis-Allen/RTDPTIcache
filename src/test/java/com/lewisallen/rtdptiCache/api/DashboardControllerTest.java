package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.caches.Caches;
import com.lewisallen.rtdptiCache.jobs.ScheduledTasks;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    WARNING: Contains tests that rely on the data inserted by migrations.
    Ensure db is populated appropriately before running tests.
    Migrations located in db/migrations
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduledTasks tasks;

    @BeforeAll
    void setup() {
        tasks.updateCaches();
    }

    @Test
    void showDashboardCreatePage() throws Exception {
        mockMvc.perform(get("/dashboard/create"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testMissingDashboard() throws Exception {
        mockMvc.perform(get("/dashboard/10000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testOneBusDashboard() throws Exception {
        mockMvc.perform(get("/dashboard/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testOneTrain() throws Exception {
        mockMvc.perform(get("/dashboard/2"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testMultipleBuses() throws Exception {
        mockMvc.perform(get("/dashboard/3"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testMultipleTrains() throws Exception {
        mockMvc.perform(get("/dashboard/4"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testFlipping() throws Exception {
        mockMvc.perform(get("/dashboard/5"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/dashboard/6"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testNameOverride() throws Exception {
        mockMvc.perform(get("/dashboard/7"))
                .andExpect(status().is2xxSuccessful());
    }


    /**
     * This test must run after prior tests due to the wiping of the static caches.
     * The 'Order' annotation ensures this.
     */
    @Test
    @Order(2)
    void testNoData() throws Exception {
        Caches.resetBusData(new ConcurrentHashMap<>());

        mockMvc.perform(get("/dashboard/1"))
                .andExpect(status().is2xxSuccessful());

        Caches.resetTrainData(new ConcurrentHashMap<>());

        mockMvc.perform(get("/dashboard/2"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testPostDashboard() throws Exception {
        JSONObject body = new JSONObject();
        List<String> codes = new ArrayList<>();

        codes.add("149000007939");
        body.put("buses", codes);
        body.put("template", "single");
        body.put("flipTo", "default");

        mockMvc.perform(post("/dashboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())).andExpect(status().is3xxRedirection());

        // Make request again to make sure redirect to already created resource.
        mockMvc.perform(post("/dashboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())).andExpect(status().is3xxRedirection());

        body.remove("flipTo");

        mockMvc.perform(post("/dashboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())).andExpect(status().is3xxRedirection());
    }
}