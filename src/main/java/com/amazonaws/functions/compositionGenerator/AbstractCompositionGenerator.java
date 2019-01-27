package com.amazonaws.functions.compositionGenerator;
import com.amazonaws.model.*;
import lombok.Data;

import java.util.*;

@Data
public abstract class AbstractCompositionGenerator implements ICompositionGenerator {

    private int NBTRY = 1000;
    private GeneratorConfiguration configuration;

    public AbstractCompositionGenerator(){
        this.configuration = new GeneratorConfiguration();
    }

    public AbstractCompositionGenerator(GeneratorConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    public AbstractComposition getBestComposition(List<Player> players){
        return this.getNBestCompositions(players).get(0);
    }

    @Override
    public List<AbstractComposition> getNBestCompositions(List<Player> players) {
        List<AbstractComposition> generatedCompositions = new ArrayList<>();
        for (int i = 0; i < NBTRY; i++) {
            AbstractComposition randomCompo = buildRandomComposition(getClonedPlayers(players));
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

    @Override
    public abstract AbstractComposition buildRandomComposition(List<Player> availablePlayers);

    public List<Player> getClonedPlayers(List<Player> players) {
        List<Player> clone = new ArrayList<>(players.size());
        for (Player p : players) {
            clone.add(new Player(p));
        }
        return clone;
    }

    public Team buildRandomTeam(List<Player> availablePlayers, int maxNbPlayerPerTeam){
        Random rand = new Random();
        Team randomTeam = new Team();
        int i = 0;
        while(i < maxNbPlayerPerTeam && randomTeam.getPlayers().size()<maxNbPlayerPerTeam && availablePlayers.size()>0) {
            int randomNum = rand.nextInt(availablePlayers.size());
            randomTeam.addPlayer(availablePlayers.get(randomNum));
            availablePlayers.remove(randomNum);
            i++;
        }
        return randomTeam;
    }

    public Team buildRandomTeam(Team currentTeam, List<Player> availablePlayers, int nbPlayerPerTeam){
        Team finalTeam = buildRandomTeam(availablePlayers, nbPlayerPerTeam-currentTeam.getPlayers().size());
        for(Player p : currentTeam.
                getPlayers()){
            finalTeam.addPlayer(p);
        }
        return finalTeam;
    }

    public List<Player> getNSortedPlayers(List<Player> availablePlayers, int n, boolean best) {
        if(availablePlayers.size()>=n) {
            if (best) {
                Collections.sort(availablePlayers, Collections.reverseOrder());
            } else {
                Collections.sort(availablePlayers);
            }
            List<Player> bestPlayers = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                bestPlayers.add(availablePlayers.get(i));
            }
            for (Player bp : bestPlayers) {
                availablePlayers.remove(bp);
            }
            return bestPlayers;
        }else{
            return null;
        }
    }

    public int getNbPlayersPerTeamOnField(int nbPlayers){
        int nbTeams = this.configuration.getNbTeamsNeeded();
        if (this.configuration.getCompositionType() == CompositionType.ODD && nbPlayers%2==1){
            return nbPlayers/nbTeams + 1;
        } else{
            return nbPlayers/nbTeams;
        }
    }

    public List<Player> getPlayersByPosition(List<Player> players, PlayerPosition position){
        List<Player> result = new ArrayList<>();
        for(Player p:players){
            if(p.getPosition()==position){
                result.add(p);
            }
        }
        return result;
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
