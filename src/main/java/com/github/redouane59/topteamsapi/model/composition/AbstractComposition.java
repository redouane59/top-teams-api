package com.github.redouane59.topteamsapi.model.composition;

import com.github.redouane59.topteamsapi.functions.composition.GeneratorConfiguration;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.Team;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Getter
@Setter
@Log
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractComposition implements IComposition, Comparable<AbstractComposition>, Cloneable  {

    private List<Player> availablePlayers;
    private int          nbPlayersOnField;

    @Override
    public int compareTo(AbstractComposition o) {
        return Double.compare(Math.abs(this.getRatingDifference()), Math.abs(o.getRatingDifference()));
    }

    public int getNbPlayersPerTeamOnField(int nbPlayers, GeneratorConfiguration configuration){
        int nbTeams = configuration.getNbTeamsNeeded();
        if (configuration.getCompositionType() == CompositionType.ODD && nbPlayers % 2 == 1){
            return nbPlayers/nbTeams + 1;
        } else{
            return nbPlayers/nbTeams;
        }
    }

    public List<Player> getNSortedPlayers(List<Player> availablePlayers, int n, boolean best) {
        List<Player> result = new ArrayList<>(availablePlayers);
        if(result.size()>=n) {
            if (best) {
                result.sort(Collections.reverseOrder());
            } else {
                Collections.sort(result);
            }
            List<Player> bestPlayers = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                bestPlayers.add(result.get(i));
            }
            for (Player bp : bestPlayers) {
                result.remove(bp);
            }
            return bestPlayers;
        }else{
            return new ArrayList<>();
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

    @Override
    @SneakyThrows
    public Team generateRandomTeam(int maxNbPlayerPerTeam){
        List<Player> availablePlayers = new ArrayList<>(this.getAvailablePlayers());
        Random rand       = SecureRandom.getInstanceStrong();
        Team   randomTeam = new Team();
        int    i          = 0;
        while(i < maxNbPlayerPerTeam && randomTeam.getPlayers().size()<maxNbPlayerPerTeam && !availablePlayers.isEmpty()) {
            int randomNum = rand.nextInt(availablePlayers.size());
            randomTeam.getPlayers().add(availablePlayers.get(randomNum));
            availablePlayers.remove(randomNum);
            i++;
        }
        this.setAvailablePlayers(availablePlayers);
        return randomTeam;
    }

    @Override
    public abstract boolean equals (Object obj);

}
