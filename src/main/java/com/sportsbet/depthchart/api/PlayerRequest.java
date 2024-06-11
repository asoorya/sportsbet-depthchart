package com.sportsbet.depthchart.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PlayerRequest {

    @NotNull(message = "Player Id is required")
    @Range(min = 1)
    @Valid
    private Integer playerId;

    @NotBlank(message = "Name is required")
    @Valid
    private String name;

    @NotNull(message = "Position required should be one of [SS, PR, C, DH, KR, K, _3B, _2B, RB, QB, RF, LF, WR, SP, RP, CF, P, TE, _1B]")
    @Valid
    private Position position;
}