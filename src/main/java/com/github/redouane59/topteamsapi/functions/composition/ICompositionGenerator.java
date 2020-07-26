package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.util.List;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;

public interface ICompositionGenerator {

    /**
     * Get the best composition from a player list
     * @param players the available players
     * @return the best possible solution
     */
    AbstractComposition getBestComposition(List<Player> players);

    /**
     * Get the best composition from an already started composition
     * @param composition the initial composition
     * @return the best possible solution regarding the initial constraints
     */
    AbstractComposition getBestComposition(AbstractComposition composition);

    /**
     * Get the best compositions from a player list
     * @param players the available players
     * @return all the generated compositions from the strongest to the weakest
     */
    List<AbstractComposition> getBestCompositions(List<Player> players);

    /**
     * Get the best composition from an already started composition
     * @param composition the initial composition
     * @return all the generated compositions from the strongest to the weakest
     */
    List<AbstractComposition> getBestCompositions(AbstractComposition composition);

}
