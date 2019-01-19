package com.amazonaws.compositionGenerator;

import com.amazonaws.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorConfiguration {

    private boolean splitBestPlayers = true;
    private boolean splitWorstPlayers = true;
    private boolean splitGoalKeepers = true;
    private int nbTeamsNeeded = 2;
    private int nbCompositionsNeeded = 1;
    private GameType gameType = GameType.SAME_NB_OF_PLAYERS_PER_TEAM;
}


