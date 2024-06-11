package com.sportsbet.depthchart.controller;

import com.sportsbet.depthchart.api.PlayerRequest;
import com.sportsbet.depthchart.repository.modal.Player;
import com.sportsbet.depthchart.service.DepthChartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/depth-chart")
public class DepthChartController {

    @Autowired
    private DepthChartService depthChartService;

    @PostMapping("/add-player")
    public ResponseEntity<String> addPlayerToDepthChart(
            @Valid @RequestBody PlayerRequest playerRequest,
            @RequestParam(required = false) Integer positionDepth) {
        Player player = new Player(playerRequest.getPlayerId(), playerRequest.getName(), playerRequest.getPosition().name());
        depthChartService.addPlayerToDepthChart(player,player.getPosition(), positionDepth);
        return ResponseEntity.ok("Player added to depth chart");
    }

    @DeleteMapping("/remove-player")
    public ResponseEntity<String> removePlayerFromDepthChart(
            @Valid @RequestBody PlayerRequest playerRequest) {
        Player player = new Player(playerRequest.getPlayerId(), playerRequest.getName(), playerRequest.getPosition().name());
        depthChartService.removePlayerFromDepthChart(player, player.getPosition());
        return ResponseEntity.ok("Player removed from depth chart");
    }

    @GetMapping("/full")
    public ResponseEntity<Map<String, List<Integer>>> getFullDepthChart() {
        return ResponseEntity.ok(depthChartService.getFullDepthChart());
    }

    @GetMapping("/under-player")
    public ResponseEntity<?> getPlayersUnderPlayerInDepthChart(
            @Valid @RequestBody PlayerRequest playerRequest,
            @RequestParam String position) {
        Player player = new Player(playerRequest.getPlayerId(), playerRequest.getName(), playerRequest.getPosition().name());
        return ResponseEntity.ok(depthChartService.getPlayersUnderPlayerInDepthChart(player, position));
    }
}
