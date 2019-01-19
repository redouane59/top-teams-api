package com.amazonaws.ratingUpdateCalculator;

import com.amazonaws.model.RelativeDistribution;
import lombok.Data;

import static com.amazonaws.model.RelativeDistribution.MEDIUM;

@Data
public class CalculatorConfiguration {
    private boolean splitPointsByTeam; // points split by team or globally
    private RelativeDistribution relativeDistribution;

    public CalculatorConfiguration(){
        this.splitPointsByTeam = true;
        this.relativeDistribution = MEDIUM;
    }

    public double getLambda() {
        double lambda = 0;
        switch (this.relativeDistribution) {
            case LOW:
                lambda = 50;
                break;
            case MEDIUM:
                lambda = 10;
                break;
            case HIGH:
                lambda = 1;
                break;
        }
        return lambda;
    }

    public double getKf(int nbPlayers){
        if(nbPlayers<5){
            return 3;
        } else if(nbPlayers==5){
            return 4;
        } else if (nbPlayers==6){
            return 5;
        } else {
            return 6;
        }
    }
}
