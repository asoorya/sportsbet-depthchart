package com.sportsbet.depthchart.repository.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@EqualsAndHashCode
public class Player {
    @Id
    private int playerId;
    private String name;
    private String position;
}
