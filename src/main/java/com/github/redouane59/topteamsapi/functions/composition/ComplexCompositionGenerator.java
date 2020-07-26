package com.github.redouane59.topteamsapi.functions.composition;

import com.github.redouane59.topteamsapi.model.composition.AbstractComposition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.java.Log;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.composition.ComplexComposition;
import org.apache.commons.math3.complex.Complex;

@Log
public class ComplexCompositionGenerator extends AbstractCompositionGenerator {

    public ComplexCompositionGenerator(GeneratorConfiguration configuration){
        this.setConfiguration(configuration);
    }

    @Override
    public List<AbstractComposition> getBestCompositions(List<Player> players){
        ComplexComposition composition = ComplexComposition.builder().availablePlayers(players).build();
        return this.getBestCompositions(composition);
    }

/*
    @Override
    public ComplexComposition buildRandomComposition(AbstractComposition composition) {
        List<Player> availablePlayers = new ArrayList<>(composition.getAvailablePlayers());
        ComplexComposition randomComposition = ComplexComposition.builder().availablePlayers(availablePlayers).build();
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
            availablePlayers.removeAll(team.getPlayers());
            teamList.set(i,team);
        }

        randomComposition.setTeams(teamList);
        return randomComposition;
    }
  */
    public void splitPlayersByPosition(List<Team> teamList, List<Player> availablePlayers, PlayerPosition position){
        List<Player> goalKeepers = getPlayersByPosition(availablePlayers, position);
        int goalKeepersAffected = 0;
        while(!goalKeepers.isEmpty() && goalKeepersAffected<teamList.size() || goalKeepersAffected<goalKeepers.size()){
            try {
                Random rand = SecureRandom.getInstanceStrong();
                Player goalKeeper = goalKeepers.get(rand.nextInt(goalKeepers.size()));
                teamList.get(goalKeepersAffected).getPlayers().add(goalKeeper);
                availablePlayers.remove(goalKeeper);
                goalKeepers.remove(goalKeeper);
                goalKeepersAffected++;
            } catch (NoSuchAlgorithmException e) {
                log.severe(e.getMessage());
            }
        }
    }

}
