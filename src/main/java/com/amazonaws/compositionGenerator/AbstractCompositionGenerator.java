package com.amazonaws.compositionGenerator;
import com.amazonaws.model.*;
import lombok.Data;

import java.util.*;

@Data
public abstract class AbstractCompositionGenerator implements ICompositionGenerator {

    private int NBTRY = 1000;

    @Override
    public AbstractComposition getBestComposition(List<Player> players) {
        return this.getBestComposition(players, new GeneratorConfiguration());
    }

    @Override
    public AbstractComposition getBestComposition(List<Player> players, GeneratorConfiguration config){
        return this.getNBestCompositions(players, config).get(0);
    }

    @Override
    public List<AbstractComposition> getNBestCompositions(List<Player> players, GeneratorConfiguration config) {
        List<AbstractComposition> generatedCompositions = new ArrayList<>();
        for (int i = 0; i < NBTRY; i++) {
            AbstractComposition randomCompo = buildRandomComposition(getClonedPlayers(players), config);
            if(!this.doesAlreadyExist(randomCompo, generatedCompositions)){
                generatedCompositions.add(randomCompo);
            }
        }
        Collections.sort(generatedCompositions);
        int nbCompositions = config.getNbCompositionsNeeded();
        if(nbCompositions>=generatedCompositions.size()){
            nbCompositions = generatedCompositions.size()-1;
        }
        return generatedCompositions.subList(0, nbCompositions);
    }

    @Override
    public abstract AbstractComposition buildRandomComposition(List<Player> availablePlayers, GeneratorConfiguration config);


    List<Player> getClonedPlayers(List<Player> players) {
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

    public int getMaxNbPlayerPerTeam(int nbPlayers, GeneratorConfiguration config){
        int nbTeams = config.getNbTeamsNeeded();
        if(nbPlayers%nbTeams==0 || config.getGameType() == GameType.SAME_NB_OF_PLAYERS_PER_TEAM){
            return nbPlayers/nbTeams;
        } else{
            return nbPlayers/nbTeams + 1;
        }
    }

    public int getMaxNbPlayerPerTeamOnField(int nbPlayers, GeneratorConfiguration config){
        int nbTeams = config.getNbTeamsNeeded();
        if (config.getGameType() == GameType.FREE && nbPlayers%2==1){
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
