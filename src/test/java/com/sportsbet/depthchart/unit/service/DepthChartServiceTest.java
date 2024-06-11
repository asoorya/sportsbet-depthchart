package com.sportsbet.depthchart.unit.service;

import com.sportsbet.depthchart.exception.BadRequestException;
import com.sportsbet.depthchart.repository.DepthChartEntryRepository;
import com.sportsbet.depthchart.repository.PlayerRepository;
import com.sportsbet.depthchart.repository.modal.DepthChartEntry;
import com.sportsbet.depthchart.repository.modal.Player;
import com.sportsbet.depthchart.service.DepthChartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DepthChartServiceTest {

    @Autowired
    private DepthChartService service;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DepthChartEntryRepository depthChartEntryRepository;

    @Mock
    private DepthChartEntryRepository depthChartEntryRepositoryMock;

    @Mock
    private PlayerRepository playerRepositoryMock;

    @InjectMocks
    private DepthChartService depthChartService;

    @BeforeEach
    public void setUp() {
        playerRepository.deleteAll();
        depthChartEntryRepository.deleteAll();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        depthChartEntryRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void testAddPlayerToDepthChart() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");
        Player charlie = new Player(3, "Charlie", "WR");

        service.addPlayerToDepthChart(bob, "WR", 0);
        service.addPlayerToDepthChart(alice, "WR", 0);
        service.addPlayerToDepthChart(charlie, "WR", 2);

        Map<String, List<Integer>> depthChart = service.getFullDepthChart();
        assertTrue(depthChart.containsKey("WR"));
        assertEquals(3, depthChart.get("WR").size());
    }

    @Test
    public void testRemovePlayerFromDepthChart() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);
        service.addPlayerToDepthChart(alice, "WR", null);

        service.removePlayerFromDepthChart(bob, "WR");

        Map<String, List<Integer>> depthChart = service.getFullDepthChart();
        assertTrue(depthChart.containsKey("WR"));
        assertEquals(1, depthChart.size());
    }

    @Test
    public void testGetPlayersUnderPlayerInDepthChart() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");
        Player charlie = new Player(3, "Charlie", "WR");

        service.addPlayerToDepthChart(bob, "WR", 0);
        service.addPlayerToDepthChart(alice, "WR", 0);
        service.addPlayerToDepthChart(charlie, "WR", 2);

        List<DepthChartEntry> playersUnderAlice = service.getPlayersUnderPlayerInDepthChart(alice, "WR");

        assertEquals(2, playersUnderAlice.size());
        assertEquals(bob, playersUnderAlice.get(0).getPlayer());
        assertEquals(charlie, playersUnderAlice.get(1).getPlayer());
    }

    @Test
    public void testGetPlayersUnderPlayerInDepthChart_playerNotAvailable_throwsNoSuchElementException() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");
        Player charlie = new Player(3, "Charlie", "WR");
        Player charlie2 = new Player(3, "Charlie2", "QB");

        service.addPlayerToDepthChart(bob, "WR", 0);
        service.addPlayerToDepthChart(alice, "WR", 0);
        service.addPlayerToDepthChart(charlie, "WR", 2);

        assertThrows(IllegalArgumentException.class, () -> service.getPlayersUnderPlayerInDepthChart(charlie2, "QB"));
    }

    @Test
    public void testGetFullDepthChart_differentPositions() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");
        Player charlie = new Player(3, "Charlie", "WR");

        service.addPlayerToDepthChart(bob, "WR", 0);
        service.addPlayerToDepthChart(alice, "WR", 0);
        service.addPlayerToDepthChart(charlie, "WR", 2);
        service.addPlayerToDepthChart(bob, "KR", null);

        Map<String, List<Integer>> depthChart = service.getFullDepthChart();
        assertEquals(2, depthChart.size());
        assertTrue(depthChart.containsKey("WR"));
        assertEquals(3, depthChart.get("WR").size());
        assertTrue(depthChart.containsKey("KR"));
        assertEquals(1, depthChart.get("KR").size());
    }

    @Test
    public void testRemovePlayerFromDepthChart_PlayerNotFound() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");
        Player charlie = new Player(3, "Charlie", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);
        service.addPlayerToDepthChart(alice, "WR", null);

        // Attempt to remove a player who does not exist in the depth chart
        assertThrows(NoSuchElementException.class, () -> service.removePlayerFromDepthChart(charlie, "WR"));
    }

    @Test
    public void testRemovePlayerFromDepthChart_PositionNotFound() {
        Player bob = new Player(1, "Bob", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);

        // Attempt to remove a player from a position that does not exist in the depth chart
        assertThrows(NoSuchElementException.class, () -> service.removePlayerFromDepthChart(bob, "QB"));
    }

    @Test
    public void testRemovePlayerFromDepthChart_EmptyDepthChart() {
        Player bob = new Player(1, "Bob", "WR");

        // Attempt to remove a player when the depth chart is empty
        assertThrows(NoSuchElementException.class, () -> service.removePlayerFromDepthChart(bob, "WR"));
    }

    @Test
    public void testRemovePlayerFromDepthChart_RemovingLastPlayer() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);
        service.addPlayerToDepthChart(alice, "WR", null);

        service.removePlayerFromDepthChart(bob, "WR");
        service.removePlayerFromDepthChart(alice, "WR");

        // Verify that the depth chart is empty after removing the last player
        assertTrue(service.getFullDepthChart().isEmpty());
    }

    @Test
    public void testRemovePlayerFromDepthChart_RemovingPlayerWithMultipleEntries() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);
        service.addPlayerToDepthChart(alice, "WR", null);
        service.addPlayerToDepthChart(bob, "TE", null);

        service.removePlayerFromDepthChart(bob, "WR");

        // Verify that only one entry for Bob exists after removal
        List<DepthChartEntry> bobEntries = depthChartEntryRepository.findByPlayer(bob);
        assertEquals(1, bobEntries.size());
    }

    @Test
    public void testRemovePlayerFromDepthChart_RemovingPlayerAtFirstPosition() {
        Player bob = new Player(1, "Bob", "WR");
        Player alice = new Player(2, "Alice", "WR");

        service.addPlayerToDepthChart(bob, "WR", null);
        service.addPlayerToDepthChart(alice, "WR", null);

        service.removePlayerFromDepthChart(bob, "WR");

        // Verify that Alice is now the first player in the depth chart
        Map<String, List<Integer>> depthChart = service.getFullDepthChart();
        assertTrue(depthChart.containsKey("WR"));
        assertEquals(1, depthChart.size());
        assertEquals(List.of(2), depthChart.get("WR"));
    }

    @Test
    public void testAddPlayerToDepthChart_DuplicatePlayer() {
        Player player = new Player(1, "Bob", "WR");
        when(depthChartEntryRepositoryMock.findByPosition("WR"))
                .thenReturn(List.of(DepthChartEntry.builder()
                                .player(player)
                                .position("WR")
                                .positionDepth(0)
                        .build()));
        Player bob = new Player(1, "Bob", "WR");

        service.addPlayerToDepthChart(bob, "WR", 0);
        assertThrows(BadRequestException.class, () -> {
            service.addPlayerToDepthChart(player, "WR", 0);
        });

        verify(depthChartEntryRepositoryMock, never()).save(any(DepthChartEntry.class));
    }
}
