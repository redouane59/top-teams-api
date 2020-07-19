package org.redouane59.topteamsapi.model;

import lombok.Builder;
import lombok.Data;
import org.redouane59.topteamsapi.model.composition.Composition;

@Data
@Builder
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
