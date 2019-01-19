package com.amazonaws.compositionGenerator;

import com.amazonaws.model.*;

import java.util.List;
import java.util.Random;

public class CompositionGenerator extends AbstractCompositionGenerator {

    @Override
    public AbstractComposition buildRandomComposition(List<Player> availablePlayers, GeneratorConfiguration config) {
        Composition randomComposition = new Composition(availablePlayers);
        Team teamA = new Team();
        Team teamB = new Team();
        int nbPlayersTeamA = getMaxNbPlayerPerTeam(availablePlayers.size(), config);
        int nbPlayersTeamB;
        if(availablePlayers.size()%2==0 || config.getGameType()==GameType.SAME_NB_OF_PLAYERS_PER_TEAM){
            nbPlayersTeamB = nbPlayersTeamA;
        } else{
            nbPlayersTeamB = nbPlayersTeamA-1;
        }

        int maxNbPlayerPerTeamOnField = getMaxNbPlayerPerTeamOnField(availablePlayers.size(), config);

        if(config.isSplitGoalKeepers()){
            this.splitPlayersByPosition(teamA, teamB, availablePlayers, PlayerPosition.GK);
        }

        if(config.isSplitBestPlayers()){
            List<Player> twoBestPlayers = getNSortedPlayers(availablePlayers, 2, true);
            if(twoBestPlayers!=null) {
                teamA.addPlayer(twoBestPlayers.get(0));
                teamB.addPlayer(twoBestPlayers.get(1));
            }
        }
        if(config.isSplitWorstPlayers()){
            List<Player> twoWorstPlayers = getNSortedPlayers(availablePlayers, 2, false);
            if(twoWorstPlayers!=null) {
                teamA.addPlayer(twoWorstPlayers.get(0));
                teamB.addPlayer(twoWorstPlayers.get(1));
            }
        }

        teamA = buildRandomTeam(teamA, availablePlayers, nbPlayersTeamA);
        teamB = buildRandomTeam(teamB, availablePlayers, nbPlayersTeamB);
        teamA.setNbPlayersOnField(maxNbPlayerPerTeamOnField);
        teamB.setNbPlayersOnField(maxNbPlayerPerTeamOnField);

        randomComposition.setTeamA(teamA);
        randomComposition.setTeamB(teamB);
        return randomComposition;
    }

    void splitPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        List<Player> goalKeepers = getPlayersByPosition(availablePlayers, position);
        if(goalKeepers.size()>1){
            Random rand = new Random();
            Player firstGK = goalKeepers.get(rand.nextInt(goalKeepers.size()));
            teamA.addPlayer(firstGK);
            goalKeepers.remove(firstGK);
            availablePlayers.remove(firstGK);
            Player secondGK = goalKeepers.get(rand.nextInt(goalKeepers.size()));
            teamB.addPlayer(secondGK);
            goalKeepers.remove(secondGK);
            availablePlayers.remove(secondGK);
        }
    }

}

