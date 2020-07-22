package com.github.redouane59.topteamsapi.functions.evaluation;

import com.github.redouane59.topteamsapi.model.Game;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.TeamSide;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Getter
@Setter
@Log
public class RatingUpdatesCalculator implements IRatingUpdatesCalculator {

    private CalculatorConfiguration configuration;

    public RatingUpdatesCalculator(CalculatorConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    public Map<String, Double> getRatingUpdates(Game game){
        Team teamA = game.getComposition().getTeamA();
        Team teamB = game.getComposition().getTeamB();
        int nbPlayersOnField;
        if(game.getComposition().getNbPlayersOnField()!=0){
            nbPlayersOnField = game.getComposition().getNbPlayersOnField();
        } else{
            nbPlayersOnField = teamA.getPlayers().size();
        }

        double globalModif = game.getPredictionError(nbPlayersOnField);

        double modifA;
        double modifB;

        if(this.configuration.isSplitPointsByTeam()){
            modifA = globalModif/2;
            modifB = -globalModif/2;
        } else{
            modifA = this.getTeamPoints(globalModif, game, TeamSide.A);
            modifB = this.getTeamPoints(globalModif, game, TeamSide.B);
        }

        modifA = modifA*this.configuration.getKf();
        modifB = modifB*this.configuration.getKf();

        Map<String, Double> playerRatingModifications = new LinkedHashMap<>();

        for(Player p : teamA.getPlayers()){
            playerRatingModifications.put(p.getId(), calculatePlayerRatingUpdate(teamA, modifA, p));
        }

        for(Player p : teamB.getPlayers()){
            playerRatingModifications.put(p.getId(), calculatePlayerRatingUpdate(teamB, modifB, p));
        }

        return playerRatingModifications;
    }

    private double calculatePlayerRatingUpdate(Team team, double teamModif, Player p){
        double individualRatio;
        if(this.configuration.getRelativeDistribution() == RelativeDistribution.NONE){
            individualRatio = 1/(double)team.getPlayers().size();
        } else{
            individualRatio = this.calculateIndividualRatio(p, team);
        }
        return teamModif * individualRatio;
    }

    private double calculateIndividualRatio(Player player, Team team)  {
        double denominator = 0;
        for (Player p : team.getPlayers()) {
            denominator += (1 / (p.getNbGamesPlayed() + 1 + configuration.getLambda()));
        }
        if(denominator==0) {
            log.severe("denominator=0");
            return 0.0;
        }
        double numerator = (1 / (player.getNbGamesPlayed() + 1 + configuration.getLambda()));
        return (numerator / denominator);
    }

    private double getTeamPoints(double globalModif, Game g, TeamSide side){
        int nbGamesA = 0;
        for(Player p : g.getComposition().getTeamA().getPlayers()){
            nbGamesA += p.getNbGamesPlayed();
        }

        int nbGamesB = 0;
        for(Player p : g.getComposition().getTeamB().getPlayers()){
            nbGamesB += p.getNbGamesPlayed();
        }
        if(nbGamesA+nbGamesB==0) return 0;
        if(side == TeamSide.A){
            return globalModif*(1-(nbGamesA)/(double)(nbGamesA+nbGamesB));
        } else if (side == TeamSide.B){
            return - globalModif*(1-(nbGamesB)/(double)(nbGamesA+nbGamesB));
        } else{
            return 0;
        }
    }


}
