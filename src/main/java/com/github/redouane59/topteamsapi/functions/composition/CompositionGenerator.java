package com.github.redouane59.topteamsapi.functions.composition;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.TeamSide;
import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;

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
    public AbstractComposition buildRandomComposition(List<Player> availablePlayers) {
        Composition randomComposition = new Composition();
        randomComposition.setAvailablePlayers(availablePlayers);
        Team        teamA             = new Team();
        Team        teamB             = new Team();
        int         nbPlayersTeamA            = randomComposition.getNbPlayersInTeam(this.getConfiguration().getCompositionType(), TeamSide.A);
        int         nbPlayersTeamB            = randomComposition.getNbPlayersInTeam(this.getConfiguration().getCompositionType(), TeamSide.B);
        int         maxNbPlayerPerTeamOnField = getNbPlayersPerTeamOnField(availablePlayers.size());

        if(this.getConfiguration().isSplitGoalKeepers()){
            this.splitTwoPlayersByPosition(teamA, teamB, availablePlayers, PlayerPosition.GK);
        }
        if(this.getConfiguration().isSplitDefenders()){
            this.splitAllPlayersByPosition(teamA, teamB, availablePlayers, PlayerPosition.DEF);
        }
        if(this.getConfiguration().isSplitStrikers()){
            this.splitAllPlayersByPosition(teamA, teamB, availablePlayers, PlayerPosition.ATT);
        }

        if(this.getConfiguration().isSplitBestPlayers()){
            List<Player> twoBestPlayers = getNSortedPlayers(availablePlayers, 2, true);
            if(twoBestPlayers.size()>1) {
                Collections.shuffle(twoBestPlayers);
                teamA.getPlayers().add(twoBestPlayers.get(0));
                teamB.getPlayers().add(twoBestPlayers.get(1));
            }
        }
        if(this.getConfiguration().isSplitWorstPlayers()){
            List<Player> twoWorstPlayers = getNSortedPlayers(availablePlayers, 2, false);
            if(twoWorstPlayers.size()>1) {
                Collections.shuffle(twoWorstPlayers);
                teamA.getPlayers().add(twoWorstPlayers.get(0));
                teamB.getPlayers().add(twoWorstPlayers.get(1));
            }
        }

        teamA = buildRandomTeam(teamA, availablePlayers, nbPlayersTeamA);
        teamB = buildRandomTeam(teamB, availablePlayers, nbPlayersTeamB);
        randomComposition.setNbPlayersOnField(maxNbPlayerPerTeamOnField);

        randomComposition.setTeamA(teamA);
        randomComposition.setTeamB(teamB);
        return randomComposition;
    }

    private void splitTwoPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        if(playersFound.size()>1){
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.getPlayers().add(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.getPlayers().add(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }

    private void splitAllPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        while(playersFound.size()>1){
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.getPlayers().add(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.getPlayers().add(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }
}

