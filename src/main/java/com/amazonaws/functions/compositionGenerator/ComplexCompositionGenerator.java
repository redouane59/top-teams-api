package com.amazonaws.functions.compositionGenerator;

import com.amazonaws.model.Player;
import com.amazonaws.model.PlayerPosition;
import com.amazonaws.model.Team;
import com.amazonaws.model.complex.ComplexComposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComplexCompositionGenerator extends AbstractCompositionGenerator {

    public ComplexCompositionGenerator(){
        super();
    }

    public ComplexCompositionGenerator(GeneratorConfiguration configuration){
        super(configuration);
    }

    @Override
    public ComplexComposition buildRandomComposition(List<Player> availablePlayers) {
        ComplexComposition randomComposition = new ComplexComposition(availablePlayers);
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
                    teamList.get(i).addPlayer(bestPlayers.get(i));
                }
            }
        }

        if(this.getConfiguration().isSplitWorstPlayers()){
            List<Player> worstPlayers = getNSortedPlayers(availablePlayers, this.getConfiguration().getNbTeamsNeeded(), false);
            if(worstPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).addPlayer(worstPlayers.get(i));
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
            teamList.get(goalKeepersAffected).addPlayer(goalKeeper);
            availablePlayers.remove(goalKeeper);
            goalKeepers.remove(goalKeeper);
            goalKeepersAffected++;
        }
    }

}
