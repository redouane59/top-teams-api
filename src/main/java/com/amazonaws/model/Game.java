package com.amazonaws.model;

import lombok.Data;

@Data
public class Game {

    private Composition composition;
    private Score score;

    public Game(Composition compo, Score s){
        this.composition = compo;
        this.score = s;
    }

    /**
     * @param kf the factor
     * @return the différence between the prediction and the observed result using a factor
     */
    public double getPredictionError(double kf){
        return this.getScore().getGoalDifference()-this.composition.getPrediction(kf);
    }


    @Override
    public String toString(){
        return this.score + "\n"+this.composition;
    }
}
