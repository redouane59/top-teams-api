package org.redouane59.topteamsapi.functions.compositionGenerator;

import java.util.List;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.composition.AbstractComposition;

public interface ICompositionGenerator {

    AbstractComposition getBestComposition(List<Player> players);
    List<? extends AbstractComposition> getNBestCompositions(List<Player> players);
    AbstractComposition buildRandomComposition(List<Player> availablePlayers);
}
