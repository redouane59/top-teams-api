package com.amazonaws.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class Composition extends AbstractComposition {

    private Team teamA = new Team();
    private Team teamB = new Team();

    public Composition(List<Player> players){
        super(players);
    }

    @Override
    public double getRatingDifference(){
        return (this.getTeamA().getRatingSum(this.getNbPlayersOnField())
                - this.getTeamB().getRatingSum(this.getNbPlayersOnField()));
    }

    public double getRatingAverageDifference(){
        return (this.getTeamA().getRatingAverage(this.getNbPlayersOnField())
                - this.getTeamB().getRatingAverage(this.getNbPlayersOnField()));
    }

    public double getPrediction(double kf){
        return this.getRatingDifference()/kf;
    }

    @Override
    public boolean equals (Object obj){
        if(obj instanceof Composition)
        {
            Composition other = (Composition) obj;
            if((other.getTeamA().equals(this.getTeamA())
                    && other.getTeamB().equals(this.getTeamB()))
                    || (other.getTeamA().equals(this.getTeamB())
                    && other.getTeamB().equals(this.getTeamA()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        try{
            StringBuilder s = new StringBuilder();

            this.getTeamA().getPlayers().sort(Collections.reverseOrder());

            s.append("TEAM A [").append(new DecimalFormat("##.##").format(this.getTeamA().getRatingAverage())).append("]\n");
            for (Player p : this.getTeamA().getPlayers()) {
                s.append("- ").append(p).append("\n");
            }

            s.append("[VS]\n");
            s.append("TEAM B [").append(new DecimalFormat("##.##").format(this.getTeamB().getRatingAverage())).append("]\n");
            this.getTeamB().getPlayers().sort(Collections.reverseOrder());

            for (Player p : this.getTeamB().getPlayers()) {
                s.append("- ").append(p).append("\n");
            }
            return s.toString();
        } catch (Exception e){
            return e.toString();
        }
    }

    public int getNbPlayersInTeam(CompositionType compositionType, TeamSide teamSide){
        int totalNumberOfPlayers = this.getAvailablePlayers().size()
                + this.teamA.getPlayers().size()+this.teamB.getPlayers().size();

        if(totalNumberOfPlayers%2==0 || compositionType == CompositionType.REGULAR){
            return totalNumberOfPlayers/2;
        } else{
            if(teamSide == TeamSide.A){
                return totalNumberOfPlayers/2 + 1;
            } else{
                return totalNumberOfPlayers/2;
            }
        }
    }
}
