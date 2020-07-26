package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.ComplexComposition;
import java.util.List;
import lombok.extern.java.Log;

@Log
public class ComplexCompositionGenerator extends AbstractCompositionGenerator {

    public ComplexCompositionGenerator(GeneratorConfiguration configuration){
        this.setConfiguration(configuration);
    }

    @Override
    public List<AbstractComposition> getBestCompositions(List<Player> players){
        ComplexComposition composition = ComplexComposition.builder().availablePlayers(players).build();
        return this.getBestCompositions(composition);
    }
}
