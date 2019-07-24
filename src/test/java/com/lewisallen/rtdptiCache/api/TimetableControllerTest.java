package com.lewisallen.rtdptiCache.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class TimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testTimetableScreenCreate() throws Exception {
        mockMvc.perform(get("/timetable/create"))
                .andExpect(status().isOk());
    }

    @Test
    void testViewTimetable() throws Exception {
        mockMvc.perform(get("/timetable/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/timetable/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testMissingTimetable() throws Exception {
        mockMvc.perform(get("/timetable/10000"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testPostTimetable() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",
                "example.csv",
                "text/plain",
                ("Test Stop 2" + System.lineSeparator() + "11:59,UB1,Old Steine").getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/timetable")
                .file(file))
                .andExpect(status().is3xxRedirection());

        // Make request again to make sure redirect to already created resource.
        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/timetable")
                .file(file))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testGetTimetableList() throws Exception {
        mockMvc.perform(get("/timetable"))
                .andExpect(status().isOk());
    }
}