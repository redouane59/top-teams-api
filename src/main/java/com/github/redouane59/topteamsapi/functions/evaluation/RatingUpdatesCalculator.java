package com.github.redouane59.topteamsapi.functions.evaluation;

import com.github.redouane59.topteamsapi.model.Game;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.TeamSide;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public List<Player> getUpdatedPlayers(Game game){
        List<Player> players = new ArrayList<>(game.getComposition().getTeamA().getPlayers());
        players.addAll(new ArrayList<>(game.getComposition().getTeamB().getPlayers()));
        Map<String, Double> ratingUpdates = this.getRatingUpdates(game);
        players.forEach(p -> p.setPreviousRating(p.getRating()));
        players.forEach(p -> p.setRating(p.getRating() + ratingUpdates.get(p.getId())));
        return players;
    }

    public Map<String, Double> getRatingUpdates(Game game){
        Team teamA = game.getComposition().getTeamA();
        Team teamB = game.getComposition().getTeamB();
        int nbPlayersOnField;
        if(game.getComposition().getNbPlayersOnField()!=0){
            nbPlayersOnField = game.getComposition().getNbPlayersOnField();
        } else{
            nbPlayersOnField = teamA.getPlayers().size();
        }

        double modifA = this.getModif(game, nbPlayersOnField, TeamSide.A);
        double modifB = this.getModif(game, nbPlayersOnField, TeamSide.B);
        Map<String, Double> playerRatingModifications = new LinkedHashMap<>();
        teamA.getPlayers().forEach(p -> playerRatingModifications.put(p.getId(), calculatePlayerRatingUpdate(teamA, modifA, p)));
        teamB.getPlayers().forEach(p -> playerRatingModifications.put(p.getId(), calculatePlayerRatingUpdate(teamB, modifB, p)));

        return playerRatingModifications;
    }

    private double getModif(Game game, int nbPlayersOnField, TeamSide teamSide){
        double modif;
        double globalModif = game.getPredictionError(nbPlayersOnField);
        if(this.configuration.isSplitPointsByTeam()){
            if(teamSide==TeamSide.A) {
                modif = globalModif / 2;
            } else{
                modif = -globalModif/2;
            }
        } else{
            if(teamSide==TeamSide.A) {
                modif = this.getTeamPoints(globalModif, game, TeamSide.A);
            } else{
                modif = this.getTeamPoints(globalModif, game, TeamSide.B);
            }
        }
        return modif*this.configuration.getKf();
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
