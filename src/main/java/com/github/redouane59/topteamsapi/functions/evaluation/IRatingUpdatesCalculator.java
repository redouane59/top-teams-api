package com.github.redouane59.topteamsapi.functions.evaluation;

import com.github.redouane59.topteamsapi.model.Game;
import com.github.redouane59.topteamsapi.model.Player;
import java.util.List;

public interface IRatingUpdatesCalculator {

    List<Player> getUpdatedPlayers(Game game);


}
