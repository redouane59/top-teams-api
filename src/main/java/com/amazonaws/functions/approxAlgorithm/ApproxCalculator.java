package com.amazonaws.functions.approxAlgorithm;

import com.amazonaws.model.Composition;
import com.amazonaws.model.Game;
import com.amazonaws.model.Player;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;

public class ApproxCalculator {

    List<Game> games;
    List<Player> players;

    public ApproxCalculator(List<Game> games, List<Player> players){
        this.games = games;
        this.players = players;
    }

    public void launch(){
        double[][] playerMatrix = this.buildPlayerMatrix();
        double[] gameMatrix = this.buildGameMatrix();
        RealMatrix coefficients = this.buildCoeffs(0.1, playerMatrix); // ??

        DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(gameMatrix, false);
        RealVector solution = solver.solve(constants);
        double minRating = 40;
        double delta = minRating-solution.getMinValue();
        List<Double> finalRatings = new ArrayList<>();
        for(int i=0; i<solution.getDimension(); i++){
            double val = solution.getEntry(i);
            solution.setEntry(i, val+delta);
            System.out.println(players.get(i) + " ; " + solution.getEntry(i));
            finalRatings.add(solution.getEntry(i));
        }
        System.out.println(finalRatings);
    }

    private RealMatrix buildCoeffs(double coeff, double[][] playerMatrix){
        RealMatrix coefficients = new Array2DRowRealMatrix(playerMatrix,	false);
        for(int i=0; i<coefficients.getRowDimension(); i++){
            for(int j=0; j<coefficients.getColumnDimension(); j++){
                double val = coefficients.getEntry(i,j);
                coefficients.setEntry(i, j, coeff*val);
            }
        }
        return coefficients;
    }
    private double[][] buildPlayerMatrix(){
        double[][] playerMatrix = new double[games.size()][players.size()];
        for(int i=0; i<players.size();i++){
            Player p = players.get(i);
            if(p!=null){
                for(int j=0; j<games.size();j++){
                    Composition compo = games.get(j).getComposition();
                    if(compo.getTeamA().getPlayers().indexOf(p)!=-1){
                        playerMatrix[j][i] = 1;
                    } else if (compo.getTeamB().getPlayers().indexOf(p)!=-1){
                        playerMatrix[j][i] = -1;
                    } else{
                        playerMatrix[j][i] = 0;
                    }
                }
            }
        }
        return playerMatrix;
    }

    private double[] buildGameMatrix(){
        double[] gameMatrix = new double[games.size()];
        for(int i=0; i<games.size();i++){
            Game g = games.get(i);
            gameMatrix[i] = g.getScore().getGoalDifference();
        }
        return gameMatrix;
    }
}
