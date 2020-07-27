package com.github.redouane59.topteamsapi.model.composition;

import com.github.redouane59.topteamsapi.functions.composition.GeneratorConfiguration;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.Team;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@Getter
@Setter
@SuperBuilder
@Log
public class ComplexComposition extends AbstractComposition {

    @Builder.Default
    private List<Team> teams = new ArrayList<>();

    @Override
    public double getRatingDifference(){
        double[] ratingValues = new double[this.teams.size()];
        for(int i = 0; i<this.teams.size(); i++){
            ratingValues[i] = this.teams.get(i).getRatingSum();
        }
        StandardDeviation stdDev = new StandardDeviation();
        return stdDev.evaluate(ratingValues);
    }

    @Override
    public List<Player> getAllPlayers() {
        List<Player> result = new ArrayList<>(this.getAvailablePlayers());
        for(Team team : this.teams){
            result.addAll(new ArrayList<>(team.getPlayers()));
        }
        return result;
    }

    public double getRatingAverageDifference(){
        double[] ratingValues = new double[this.teams.size()];
        for(int i = 0; i<this.teams.size(); i++){
            ratingValues[i] = this.teams.get(i).getRatingAverage();
        }
        StandardDeviation stdDev = new StandardDeviation();
        return stdDev.evaluate(ratingValues);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ComplexComposition)
        {
            ComplexComposition other = (ComplexComposition) obj;
            for(Team otherTeam : other.getTeams()){
                boolean found = false;
                for(Team team : this.getTeams()){
                    if (team.equals(otherTeam)) {
                        found = true;
                        break;
                    }
                }
                if(!found){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.teams, this.getAvailablePlayers());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i<this.teams.size(); i++){
            Team team = this.teams.get(i);
            team.getPlayers().sort(Collections.reverseOrder());
            s.append("TEAM ").append(i + 1).append(" [").append(new DecimalFormat("##.##").format(team.getRatingAverage())).append("]\n");
            for (Player p : team.getPlayers()) {
                s.append("- ").append(p).append("\n");
            }
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public AbstractComposition generateRandomComposition(GeneratorConfiguration configuration) {
        ComplexComposition randomComposition = ComplexComposition.builder()
                                                                 .availablePlayers(new ArrayList<>(this.getAvailablePlayers()))
                                                                 .build();

        List<Team> teamList = new ArrayList<>();
        this.teams.forEach(t -> teamList.add(Team.builder().players(new ArrayList<>(t.getPlayers())).build()));
        randomComposition.setTeams(teamList);
        while(randomComposition.getTeams().size()<configuration.getNbTeamsNeeded()){
            randomComposition.getTeams().add(new Team());
        }
        int nbPlayerPerTeam = randomComposition.getAvailablePlayers().size()/configuration.getNbTeamsNeeded();

        if(configuration.isSplitGoalKeepers()){
            this.splitPlayersByPosition(teamList, randomComposition.getAvailablePlayers(), PlayerPosition.GK);
        }

        if(configuration.isSplitBestPlayers()){
            List<Player> bestPlayers = getNSortedPlayers(randomComposition.getAvailablePlayers(), configuration.getNbTeamsNeeded(), true);
            if(bestPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).getPlayers().add(bestPlayers.get(i));
                    randomComposition.getAvailablePlayers().remove(bestPlayers.get(i));
                }
            }
        }

        if(configuration.isSplitWorstPlayers()){
            List<Player> worstPlayers = getNSortedPlayers(randomComposition.getAvailablePlayers(), configuration.getNbTeamsNeeded(), false);
            if(worstPlayers!=null) {
                for(int i=0; i<teamList.size();i++){
                    teamList.get(i).getPlayers().add(worstPlayers.get(i));
                    randomComposition.getAvailablePlayers().remove(worstPlayers.get(i));
                }
            }
        }

        for(int i=0;i<configuration.getNbTeamsNeeded();i++){
            Team team = generateRandomTeam(randomComposition, i, nbPlayerPerTeam);
            randomComposition.getAvailablePlayers().removeAll(team.getPlayers());
            teamList.set(i,team);
        }

        randomComposition.setTeams(teamList);
        return randomComposition;
    }

    @SneakyThrows
    public Team generateRandomTeam(ComplexComposition composition, int index, int maxNbPlayerPerTeam){
        List<Player> availablePlayers = new ArrayList<>(composition.getAvailablePlayers());
        Random rand       = SecureRandom.getInstanceStrong();
        int    i          = 0;
        Team team = composition.getTeams().get(index);
        while(i < maxNbPlayerPerTeam && team.getPlayers().size()<maxNbPlayerPerTeam && !availablePlayers.isEmpty()) {
            int randomNum = rand.nextInt(availablePlayers.size());
            team.getPlayers().add(availablePlayers.get(randomNum));
            availablePlayers.remove(randomNum);
            i++;
        }
        composition.setAvailablePlayers(availablePlayers);
        return team;
    }

    @SneakyThrows
    public void splitPlayersByPosition(List<Team> teamList, List<Player> availablePlayers, PlayerPosition position){
        List<Player> goalKeepers = getPlayersByPosition(availablePlayers, position);
        int goalKeepersAffected = 0;
        while(!goalKeepers.isEmpty() && goalKeepersAffected<teamList.size() || goalKeepersAffected<goalKeepers.size()){
            Random rand = SecureRandom.getInstanceStrong();
            Player goalKeeper = goalKeepers.get(rand.nextInt(goalKeepers.size()));
            teamList.get(goalKeepersAffected).getPlayers().add(goalKeeper);
            availablePlayers.remove(goalKeeper);
            goalKeepers.remove(goalKeeper);
            goalKeepersAffected++;
        }
    }

}
