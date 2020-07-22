package com.github.redouane59.topteamsapi.functions.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class CalculatorConfiguration {
    @Builder.Default
    private boolean splitPointsByTeam = true; // points split by team or globally
    @Builder.Default
    private RelativeDistribution relativeDistribution = RelativeDistribution.MEDIUM;
    private double kf;

    public CalculatorConfiguration(int nbPlayers){
        this.kf = calculateKf(nbPlayers);
        this.relativeDistribution = RelativeDistribution.MEDIUM;
        this.splitPointsByTeam = true;
    }

    public double getLambda() {
        double lambda;
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
            default:
                lambda = 0;
                break;
        }
        return lambda;
    }

    public static double calculateKf(int nbPlayers){
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
