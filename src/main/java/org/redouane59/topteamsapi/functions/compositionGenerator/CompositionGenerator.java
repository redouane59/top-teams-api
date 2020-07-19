package org.redouane59.topteamsapi.functions.compositionGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.PlayerPosition;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.TeamSide;
import org.redouane59.topteamsapi.model.composition.AbstractComposition;
import org.redouane59.topteamsapi.model.composition.Composition;

@Setter
@Getter
public class CompositionGenerator extends AbstractCompositionGenerator {

    public CompositionGenerator(GeneratorConfiguration configuration){
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
            if(twoBestPlayers!=null) {
                Collections.shuffle(twoBestPlayers);
                teamA.getPlayers().add(twoBestPlayers.get(0));
                teamB.getPlayers().add(twoBestPlayers.get(1));
            }
        }
        if(this.getConfiguration().isSplitWorstPlayers()){
            List<Player> twoWorstPlayers = getNSortedPlayers(availablePlayers, 2, false);
            if(twoWorstPlayers!=null) {
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
            Random rand = new Random();
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
            Random rand = new Random();
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

