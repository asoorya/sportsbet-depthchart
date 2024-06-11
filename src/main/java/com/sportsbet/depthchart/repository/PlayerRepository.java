package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.repository.modal.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
