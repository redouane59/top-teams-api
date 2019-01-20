package com.amazonaws.compositionGenerator;
import com.amazonaws.model.AbstractComposition;
import com.amazonaws.model.Player;

import java.util.List;

public interface ICompositionGenerator {

    AbstractComposition getBestComposition(List<Player> players);
    List<? extends AbstractComposition> getNBestCompositions(List<Player> players);
    AbstractComposition buildRandomComposition(List<Player> availablePlayers);
}
