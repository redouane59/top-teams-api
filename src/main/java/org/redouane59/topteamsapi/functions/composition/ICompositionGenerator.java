package org.redouane59.topteamsapi.functions.composition;

import java.util.List;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.composition.AbstractComposition;

public interface ICompositionGenerator {

    AbstractComposition getBestComposition(List<Player> players);
    List<AbstractComposition> getNBestCompositions(List<Player> players);
    AbstractComposition buildRandomComposition(List<Player> availablePlayers);
}
