package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Setter
@Getter
@Log
public abstract class AbstractCompositionGenerator implements ICompositionGenerator {

    private GeneratorConfiguration configuration;


    @Override
    public AbstractComposition getBestComposition(AbstractComposition composition) {
        return this.getBestCompositions(composition).get(0);
    }

    @Override
    public AbstractComposition getBestComposition(List<Player> players){
        Composition composition = Composition.builder().availablePlayers(players).build();
        return this.getBestCompositions(composition).get(0);
    }

    @Override
    public List<AbstractComposition> getBestCompositions(AbstractComposition composition) {
        List<AbstractComposition> generatedCompositions = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            AbstractComposition randomCompo = composition.generateRandomComposition(this.configuration);
            if(!this.doesAlreadyExist(randomCompo, generatedCompositions)){
                generatedCompositions.add(randomCompo);
            }
        }
        Collections.sort(generatedCompositions);
        int nbCompositions = this.configuration.getNbCompositionsNeeded();
        if(nbCompositions>=generatedCompositions.size()){
            nbCompositions = generatedCompositions.size()-1;
        }
        return generatedCompositions.subList(0, nbCompositions);
    }

    public List<Player> getClonedPlayers(List<Player> players) {
        List<Player> clone = new ArrayList<>(players.size());
        for (Player p : players) {
            clone.add(new Player(p));
        }
        return clone;
    }

    private boolean doesAlreadyExist(AbstractComposition newCompo, List<AbstractComposition> allCompositions){
        for(AbstractComposition compo : allCompositions){
            if(compo.equals(newCompo)){
                return true;
            }
        }
        return false;
    }
}
