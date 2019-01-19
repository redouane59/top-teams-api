package com.amazonaws.compositionGenerator;

import com.amazonaws.model.Player;
import com.amazonaws.model.PlayerPosition;
import com.amazonaws.model.Team;
import com.amazonaws.model.complex.ComplexComposition;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class ComplexCompositionGenerator extends AbstractCompositionGenerator {

    @Override
    public ComplexComposition buildRandomComposition(List<Player> availablePlayers, GeneratorConfiguration config) {
        ComplexComposition randomComposition = new ComplexComposition(availablePlayers);
        int nbPlayerPerTeam = availablePlayers.size()/config.getNbTeamsNeeded();

        List<Team> teamList = new ArrayList<>();
        // init teamList
        for(int i=0;i<config.getNbTeamsNeeded();i++){
            teamList.add(new Team());
        }

        if(config.isSplitGoalKeepers()){
            this.splitPlayersByPosition(teamList, availablePlayers, PlayerPosition.GK);
        }

        if(config.isSplitBestPlayers()){
            List<Player> bestPlayers = getNSortedPlayers(availablePlayers, config.getNbTeamsNeeded(), true);
            if(bestPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).addPlayer(bestPlayers.get(i));
                }
            }
        }

        if(config.isSplitWorstPlayers()){
            List<Player> worstPlayers = getNSortedPlayers(availablePlayers, config.getNbTeamsNeeded(), false);
            if(worstPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).addPlayer(worstPlayers.get(i));
                }
            }
        }

        for(int i=0;i<config.getNbTeamsNeeded();i++){
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
