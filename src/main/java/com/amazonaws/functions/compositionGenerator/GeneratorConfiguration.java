package com.amazonaws.functions.compositionGenerator;

import com.amazonaws.model.CompositionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeneratorConfiguration {

    private boolean splitBestPlayers = true;
    private boolean splitWorstPlayers = true;
    private boolean splitGoalKeepers = true;
    private boolean splitDefenders = true;
    private boolean splitStrikers = true;
    private int nbTeamsNeeded = 2;
    private int nbCompositionsNeeded = 1;
    private CompositionType compositionType = CompositionType.REGULAR;
}

