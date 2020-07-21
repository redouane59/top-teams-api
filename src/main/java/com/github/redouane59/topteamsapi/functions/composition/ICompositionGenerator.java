package com.github.redouane59.topteamsapi.functions.composition;

import java.util.List;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;

public interface ICompositionGenerator {

    AbstractComposition getBestComposition(List<Player> players);
    List<AbstractComposition> getNBestCompositions(List<Player> players);
    AbstractComposition buildRandomComposition(List<Player> availablePlayers);
}
