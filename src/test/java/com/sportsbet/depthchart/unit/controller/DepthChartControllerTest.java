package com.sportsbet.depthchart.unit.controller;


import com.sportsbet.depthchart.api.PlayerRequest;
import com.sportsbet.depthchart.api.Position;
import com.sportsbet.depthchart.controller.DepthChartController;
import com.sportsbet.depthchart.service.DepthChartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DepthChartController.class)
public class DepthChartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepthChartService depthChartService;

    @Test
    public void testAddPlayerToDepthChart_Validation() throws Exception {
        PlayerRequest playerRequest = PlayerRequest.builder()
                .position(Position.valueOf("WR"))
                .name("Bob")
                .build();
        mockMvc.perform(post("/api/depth-chart/add-player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequest))
                        .param("positionDepth", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value("Player Id is required"));
    }

    @Test
    public void testRemovePlayerFromDepthChart_Validation() throws Exception {
        PlayerRequest playerRequest = PlayerRequest.builder()
                .playerId(1)
                .position(Position.valueOf("WR"))
                .name("")
                .build();

        mockMvc.perform(delete("/api/depth-chart/remove-player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequest))
                        .param("position", "WR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value("Name is required"));
    }
}
