package com.github.redouane59.topteamsapi.functions.evaluation;

import com.github.redouane59.topteamsapi.model.Game;
import java.util.Map;

public interface IRatingUpdatesCalculator {

    Map<String, Double> getRatingUpdates(Game game);

}
