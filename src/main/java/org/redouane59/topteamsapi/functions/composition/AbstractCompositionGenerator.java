package org.redouane59.topteamsapi.functions.composition;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.PlayerPosition;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.composition.AbstractComposition;
import org.redouane59.topteamsapi.model.composition.CompositionType;

@Setter
@Getter
@Log
public abstract class AbstractCompositionGenerator implements ICompositionGenerator {

    private final static int       NBTRY         = 1000;
    @Builder.Default
    private GeneratorConfiguration configuration = new GeneratorConfiguration();

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

    public List<Player> getClonedPlayers(List<Player> players) {
        List<Player> clone = new ArrayList<>(players.size());
        for (Player p : players) {
            clone.add(new Player(p));
        }
        return clone;
    }

    public Team buildRandomTeam(List<Player> availablePlayers, int maxNbPlayerPerTeam){
        try {
            Random rand = SecureRandom.getInstanceStrong();
            Team randomTeam = new Team();
            int i = 0;
            while(i < maxNbPlayerPerTeam && randomTeam.getPlayers().size()<maxNbPlayerPerTeam && !availablePlayers.isEmpty()) {
                int randomNum = rand.nextInt(availablePlayers.size());
                randomTeam.getPlayers().add(availablePlayers.get(randomNum));
                availablePlayers.remove(randomNum);
                i++;
            }
            return randomTeam;
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getMessage());
        }
        return new Team();
    }

    public Team buildRandomTeam(Team currentTeam, List<Player> availablePlayers, int nbPlayerPerTeam){
        Team finalTeam = buildRandomTeam(availablePlayers, nbPlayerPerTeam-currentTeam.getPlayers().size());
        for(Player p : currentTeam.
                                      getPlayers()){
            finalTeam.getPlayers().add(p);
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
            return new ArrayList<>();
        }
    }

    public int getNbPlayersPerTeamOnField(int nbPlayers){
        int nbTeams = this.configuration.getNbTeamsNeeded();
        if (this.configuration.getCompositionType() == CompositionType.ODD && nbPlayers % 2 == 1){
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
