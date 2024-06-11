package com.sportsbet.depthchart.service;


import com.sportsbet.depthchart.exception.BadRequestException;
import com.sportsbet.depthchart.repository.DepthChartEntryRepository;
import com.sportsbet.depthchart.repository.PlayerRepository;
import com.sportsbet.depthchart.repository.modal.DepthChartEntry;
import com.sportsbet.depthchart.repository.modal.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DepthChartService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DepthChartEntryRepository depthChartEntryRepository;

    @Transactional
    public void addPlayerToDepthChart(Player player, String position, Integer positionDepth) {

        List<DepthChartEntry> entries = depthChartEntryRepository.findByPosition(position);

        boolean isDuplicate = entries.stream().anyMatch(entry -> entry.getPlayer().equals(player));
        if (isDuplicate) {
            throw new BadRequestException("Duplicate player entry in the depth chart");
        }

        if (positionDepth == null) {
            positionDepth = depthChartEntryRepository.findByPositionOrderByPositionDepth(position).size();
        } else {
            adjustDepthChart(position, positionDepth);
        }
        playerRepository.save(player);
        DepthChartEntry entry = DepthChartEntry.builder()
                .player(player)
                .position(position)
                .positionDepth(positionDepth)
                .build();
        depthChartEntryRepository.save(entry);
    }

    @Transactional
    public void removePlayerFromDepthChart(Player player, String position) {
        // Find the depth chart entry associated with the specified player and position
        List<DepthChartEntry> entries = depthChartEntryRepository.findByPlayerAndPosition(player, position);

        // Remove the first entry found for the player and position
        if (!entries.isEmpty()) {
            DepthChartEntry entryToRemove = entries.get(0);
            depthChartEntryRepository.delete(entryToRemove);

            // Adjust position depths for remaining entries
            List<DepthChartEntry> positionEntries = depthChartEntryRepository.findByPositionOrderByPositionDepth(position);
            for (int i = 0; i < positionEntries.size(); i++) {
                positionEntries.get(i).setPositionDepth(i);
                depthChartEntryRepository.save(positionEntries.get(i));
            }
        } else {
            throw new NoSuchElementException("Player not found in the specified position");
        }
    }

    public Map<String, List<Integer>> getFullDepthChart() {
        Map<String, List<Integer>> fullDepthChart = new HashMap<>();
        List<DepthChartEntry> allEntries = depthChartEntryRepository.findAll();
        for (DepthChartEntry entry : allEntries) {
            fullDepthChart.computeIfAbsent(entry.getPosition(), k -> new ArrayList<>()).add(entry.getPlayer().getPlayerId());
        }
        for (List<Integer> entries : fullDepthChart.values()) {
            entries.sort(Comparator.comparingInt(playerId -> allEntries.stream()
                    .filter(entry -> entry.getPlayer().getPlayerId() == playerId)
                    .findFirst().get().getPositionDepth()));
        }
        return fullDepthChart;
    }

    public List<DepthChartEntry> getPlayersUnderPlayerInDepthChart(Player player, String position) {
        int positionDepth = depthChartEntryRepository.findByPositionOrderByPositionDepth(position).stream()
                .filter(entry -> entry.getPlayer().equals(player))
                .findFirst()
                .map(DepthChartEntry::getPositionDepth)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in depth chart"));
        return depthChartEntryRepository.findByPositionAndPositionDepthGreaterThanOrderByPositionDepth(position, positionDepth);
    }

    private void adjustDepthChart(String position, int positionDepth) {
        List<DepthChartEntry> entries = depthChartEntryRepository.findByPositionOrderByPositionDepth(position);
        for (DepthChartEntry entry : entries) {
            if (entry.getPositionDepth() >= positionDepth) {
                entry.setPositionDepth(entry.getPositionDepth() + 1);
                depthChartEntryRepository.save(entry);
            }
        }
    }
}
