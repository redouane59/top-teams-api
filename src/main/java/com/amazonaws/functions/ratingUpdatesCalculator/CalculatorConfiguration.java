package com.amazonaws.functions.ratingUpdatesCalculator;

import lombok.Data;

import static com.amazonaws.functions.ratingUpdatesCalculator.RelativeDistribution.MEDIUM;

@Data
public class CalculatorConfiguration {
    private boolean splitPointsByTeam; // points split by team or globally
    private RelativeDistribution relativeDistribution;
    private double kf;

    public CalculatorConfiguration(double kf){
        this.splitPointsByTeam = true;
        this.relativeDistribution = MEDIUM;
        this.kf = kf;
    }

    public CalculatorConfiguration(int nbPlayers){
        this.splitPointsByTeam = true;
        this.relativeDistribution = MEDIUM;
        this.kf = calculateKf(nbPlayers);
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
