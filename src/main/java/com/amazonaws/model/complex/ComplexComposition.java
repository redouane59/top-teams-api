package com.amazonaws.model.complex;

import com.amazonaws.model.AbstractComposition;
import com.amazonaws.model.Player;
import com.amazonaws.model.Team;
import com.amazonaws.model.libraries.Statistics;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class ComplexComposition extends AbstractComposition {

    private List<Team> teams = new ArrayList<>();

    public ComplexComposition(List<Player> players){
        super(players);
    }
    public List<Team> getTeams(){
        return this.teams;
    }

    public void setTeams(List<Team> teams){
        this.teams = teams;
    }

    @Override
    public double getRatingDifference(){
        double[] ratingValues = new double[this.teams.size()];
        for(int i = 0; i<this.teams.size(); i++){
            ratingValues[i] = this.teams.get(i).getRatingSum();
        }
        Statistics statistics = new Statistics(ratingValues);
        return statistics.getStdDev();
    }

    public double getRatingAverageDifference(){
        double[] ratingValues = new double[this.teams.size()];
        for(int i = 0; i<this.teams.size(); i++){
            ratingValues[i] = this.teams.get(i).getRatingAverage();
        }
        Statistics statistics = new Statistics(ratingValues);
        return statistics.getStdDev();
    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ComplexComposition)
        {
            ComplexComposition other = (ComplexComposition) obj;
            for(Team otherTeam : other.getTeams()){
                boolean found = false;
                for(Team team : this.getTeams()){
                    if(team.equals(otherTeam)){
                        found = true;
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
    public String toString() {
        try{
            StringBuilder s = new StringBuilder();

            for(int i = 0; i<this.teams.size(); i++){
                Team team = this.teams.get(i);
                team.getPlayers().sort(Collections.reverseOrder());
                s.append("TEAM " + (i+1) +" [").append(new DecimalFormat("##.##").format(team.getRatingAverage())).append("]\n");
                for (Player p : team.getPlayers()) {
                    s.append("- ").append(p).append("\n");
                }
                s.append("\n");
            }
            return s.toString();
        } catch (Exception e){
            return e.toString();
        }
    }

}
