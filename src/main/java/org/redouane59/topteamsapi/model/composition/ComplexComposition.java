package org.redouane59.topteamsapi.model.composition;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.Team;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    public String toString() {
        try{
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
        } catch (Exception e){
            return e.toString();
        }
    }

}
