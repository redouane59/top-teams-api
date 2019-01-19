package com.amazonaws.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
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
