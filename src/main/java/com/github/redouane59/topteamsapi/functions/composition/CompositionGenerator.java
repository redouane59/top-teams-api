package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Setter
@Getter
@Log
public class CompositionGenerator extends AbstractCompositionGenerator {

    public CompositionGenerator(GeneratorConfiguration configuration){
        this.setConfiguration(configuration);
    }

    @Override
    public List<AbstractComposition> getBestCompositions(List<Player> players){
        Composition composition = Composition.builder().availablePlayers(players).build();
        return this.getBestCompositions(composition);
    }

}

