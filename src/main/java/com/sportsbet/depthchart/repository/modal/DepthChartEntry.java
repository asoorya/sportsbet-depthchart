package com.sportsbet.depthchart.repository.modal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
public class DepthChartEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    private String position;
    private int positionDepth;
}
