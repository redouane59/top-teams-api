package com.github.redouane59.topteamsapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    private Composition composition;
    private Score       score;

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
