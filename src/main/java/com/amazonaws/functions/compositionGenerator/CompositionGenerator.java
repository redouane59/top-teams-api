package com.amazonaws.functions.compositionGenerator;

import com.amazonaws.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CompositionGenerator extends AbstractCompositionGenerator {

    public CompositionGenerator(){
        super();
    }

    public CompositionGenerator(GeneratorConfiguration configuration){
        super(configuration);
    }

    @Override
    public AbstractComposition buildRandomComposition(List<Player> availablePlayers) {
        Composition randomComposition = new Composition(availablePlayers);
        Team teamA = new Team();
        Team teamB = new Team();
        int nbPlayersTeamA = getNbPlayersInTeamA(availablePlayers.size());
        int nbPlayersTeamB = getNbPlayersInTeamB(availablePlayers.size(), nbPlayersTeamA);
        int maxNbPlayerPerTeamOnField = getNbPlayersPerTeamOnField(availablePlayers.size());

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
            if(twoBestPlayers!=null) {
                Collections.shuffle(twoBestPlayers);
                teamA.addPlayer(twoBestPlayers.get(0));
                teamB.addPlayer(twoBestPlayers.get(1));
            }
        }
        if(this.getConfiguration().isSplitWorstPlayers()){
            List<Player> twoWorstPlayers = getNSortedPlayers(availablePlayers, 2, false);
            if(twoWorstPlayers!=null) {
                Collections.shuffle(twoWorstPlayers);
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

    private int getNbPlayersInTeamA(int nbPlayers){
        int nbTeams = this.getConfiguration().getNbTeamsNeeded();
        if(nbPlayers%nbTeams==0 || this.getConfiguration().getGameType() == GameType.REGULAR){
            return nbPlayers/nbTeams;
        } else{
            return nbPlayers/nbTeams + 1;
        }
    }

    private int getNbPlayersInTeamB(int nbAvailablePlayers, int nbPlayersTeamA){
        if(nbAvailablePlayers%2==0 || this.getConfiguration().getGameType()==GameType.REGULAR){
            return nbPlayersTeamA;
        } else{
            return nbPlayersTeamA-1;
        }
    }

    private void splitTwoPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        if(playersFound.size()>1){
            Random rand = new Random();
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.addPlayer(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.addPlayer(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }

    private void splitAllPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        while(playersFound.size()>1){
            Random rand = new Random();
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.addPlayer(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.addPlayer(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }

}
