package org.redouane59.topteamsapi.model.composition;

import java.text.DecimalFormat;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.TeamSide;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Composition extends AbstractComposition {
    
    private Team teamA = new Team();
    private Team teamB = new Team();

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
            return (other.getTeamA().equals(this.getTeamA())
                    && other.getTeamB().equals(this.getTeamB()))
                   || (other.getTeamA().equals(this.getTeamB())
                       && other.getTeamB().equals(this.getTeamA()));
        }
        return false;
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
}
