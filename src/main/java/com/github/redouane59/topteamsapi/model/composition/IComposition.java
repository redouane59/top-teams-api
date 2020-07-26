package com.github.redouane59.topteamsapi.model.composition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.redouane59.topteamsapi.functions.composition.GeneratorConfiguration;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.TeamSide;
import java.util.List;

public interface IComposition {
    double getRatingDifference();
    @JsonIgnore
    List<Player> getAllPlayers();
    @JsonIgnore
    AbstractComposition generateRandomComposition(GeneratorConfiguration configuration);
    @JsonIgnore
    Team generateRandomTeam(int maxNbPlayerPerTeam);
}
