package org.redouane59.topteamsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Score {

    private int scoreA;
    private int scoreB;

    public int getGoalDifference(){
        return scoreA-scoreB;
    }

    @Override
    public String toString(){
        return this.scoreA + "-" + this.scoreB;
    }
}
