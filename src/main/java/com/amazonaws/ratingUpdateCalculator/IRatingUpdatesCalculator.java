package com.amazonaws.ratingUpdateCalculator;

import com.amazonaws.model.Game;

import java.util.Map;

public interface IRatingUpdatesCalculator {

    Map<String, Double> getRatingUpdates(Game game);

}
