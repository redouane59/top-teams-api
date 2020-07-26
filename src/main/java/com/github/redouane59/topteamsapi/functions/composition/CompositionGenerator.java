package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Setter
@Getter
@Log
public class CompositionGenerator extends AbstractCompositionGenerator {

    private Random rand;

    public CompositionGenerator(GeneratorConfiguration configuration){
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getMessage());
        }
        this.setConfiguration(configuration);
    }

    @Override
    public List<AbstractComposition> getBestCompositions(List<Player> players){
        Composition composition = Composition.builder().availablePlayers(players).build();
        return this.getBestCompositions(composition);
    }

}

