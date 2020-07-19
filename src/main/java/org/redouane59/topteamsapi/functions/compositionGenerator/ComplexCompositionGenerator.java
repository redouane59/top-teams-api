package org.redouane59.topteamsapi.functions.compositionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.PlayerPosition;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.composition.ComplexComposition;

public class ComplexCompositionGenerator extends AbstractCompositionGenerator {

    public ComplexCompositionGenerator(GeneratorConfiguration configuration){
        this.setConfiguration(configuration);
    }

    @Override
    public ComplexComposition buildRandomComposition(List<Player> availablePlayers) {
        ComplexComposition randomComposition = new ComplexComposition();
        randomComposition.setAvailablePlayers(availablePlayers);
        int nbPlayerPerTeam = availablePlayers.size()/this.getConfiguration().getNbTeamsNeeded();

        List<Team> teamList = new ArrayList<>();
        // init teamList
        for(int i=0;i<this.getConfiguration().getNbTeamsNeeded();i++){
            teamList.add(new Team());
        }

        if(this.getConfiguration().isSplitGoalKeepers()){
            this.splitPlayersByPosition(teamList, availablePlayers, PlayerPosition.GK);
        }

        if(this.getConfiguration().isSplitBestPlayers()){
            List<Player> bestPlayers = getNSortedPlayers(availablePlayers, this.getConfiguration().getNbTeamsNeeded(), true);
            if(bestPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).getPlayers().add(bestPlayers.get(i));
                }
            }
        }

        if(this.getConfiguration().isSplitWorstPlayers()){
            List<Player> worstPlayers = getNSortedPlayers(availablePlayers, this.getConfiguration().getNbTeamsNeeded(), false);
            if(worstPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).getPlayers().add(worstPlayers.get(i));
                }
            }
        }

        for(int i=0;i<this.getConfiguration().getNbTeamsNeeded();i++){
            Team team = buildRandomTeam(teamList.get(i), availablePlayers, nbPlayerPerTeam);
            teamList.set(i,team);
        }

        randomComposition.setTeams(teamList);
        return randomComposition;
    }

    public void splitPlayersByPosition(List<Team> teamList, List<Player> availablePlayers, PlayerPosition position){
        List<Player> goalKeepers = getPlayersByPosition(availablePlayers, position);
        int goalKeepersAffected = 0;
        while(goalKeepers.size()>0 && goalKeepersAffected<teamList.size() || goalKeepersAffected<goalKeepers.size()){
            Random rand = new Random();
            Player goalKeeper = goalKeepers.get(rand.nextInt(goalKeepers.size()));
            teamList.get(goalKeepersAffected).getPlayers().add(goalKeeper);
            availablePlayers.remove(goalKeeper);
            goalKeepers.remove(goalKeeper);
            goalKeepersAffected++;
        }
    }

}
