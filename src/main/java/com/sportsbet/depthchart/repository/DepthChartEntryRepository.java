package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.repository.modal.DepthChartEntry;
import com.sportsbet.depthchart.repository.modal.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepthChartEntryRepository extends JpaRepository<DepthChartEntry, Long> {
    List<DepthChartEntry> findByPositionOrderByPositionDepth(String position);
    List<DepthChartEntry> findByPlayerAndPosition(Player player, String position);
    List<DepthChartEntry> findByPositionAndPositionDepthGreaterThanOrderByPositionDepth(String position, int positionDepth);
    List<DepthChartEntry> findByPlayer(Player player);
    List<DepthChartEntry> findByPosition(String position);
}
