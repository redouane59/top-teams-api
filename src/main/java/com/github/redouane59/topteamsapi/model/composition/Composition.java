package com.github.redouane59.topteamsapi.model.composition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.topteamsapi.functions.composition.GeneratorConfiguration;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.TeamSide;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;

@Setter
@Getter
@NoArgsConstructor
@Log
@SuperBuilder
public class Composition extends AbstractComposition {

    @JsonProperty("team_A")
    @Builder.Default
    private Team teamA = new Team();
    @JsonProperty("team_B")
    @Builder.Default
    private Team teamB = new Team();

    @Override
    public double getRatingDifference(){
        return (this.getTeamA().getRatingSum(this.getNbPlayersOnField())
                - this.getTeamB().getRatingSum(this.getNbPlayersOnField()));
    }

    public double getRatingAverageDifference(){
        return (this.getTeamA().getRatingAverage(this.getNbPlayersOnField())
                - this.getTeamB().getRatingAverage(this.getNbPlayersOnField()));
    }

    public double getPrediction(double kf){
        return this.getRatingDifference()/kf;
    }

    public int getNbPlayersInTeam(CompositionType compositionType, TeamSide teamSide){
        int totalNumberOfPlayers = this.getAvailablePlayers().size()
                                   + this.teamA.getPlayers().size()+this.teamB.getPlayers().size();

        if(totalNumberOfPlayers%2==0 || compositionType == CompositionType.REGULAR){
            return totalNumberOfPlayers/2;
        } else{
            if(teamSide == TeamSide.A){
                return totalNumberOfPlayers/2 + 1;
            } else{
                return totalNumberOfPlayers/2;
            }
        }
    }

    @Override
    public List<Player> getAllPlayers(){
        List<Player> result = new ArrayList<>(this.teamA.getPlayers());
        result.addAll( new ArrayList<>(this.teamB.getPlayers()));
        result.addAll( new ArrayList<>(this.getAvailablePlayers()));
        return result;
    }

    @Override
    public AbstractComposition generateRandomComposition(GeneratorConfiguration configuration) {
        Composition composition = Composition.builder()
                                             .availablePlayers(new ArrayList<>(this.getAvailablePlayers()))
                                             .teamA(Team.builder().players(new ArrayList<>(this.getTeamA().getPlayers())).build())
                                             .teamB(Team.builder().players(new ArrayList<>(this.getTeamB().getPlayers())).build())
                                             .build();
        int         nbPlayersTeamA            = this.getNbPlayersInTeam(configuration.getCompositionType(), TeamSide.A);
        int         nbPlayersTeamB            = this.getNbPlayersInTeam(configuration.getCompositionType(), TeamSide.B);
        int         maxNbPlayerPerTeamOnField = this.getNbPlayersPerTeamOnField(this.getAllPlayers().size(), configuration);

        if(configuration.isSplitGoalKeepers()){
            this.splitTwoPlayersByPosition(composition.getTeamA(), composition.getTeamB(), composition.getAvailablePlayers(), PlayerPosition.GK);
        }
        if(configuration.isSplitDefenders()){
            this.splitAllPlayersByPosition(composition.getTeamA(), composition.getTeamB(), composition.getAvailablePlayers(), PlayerPosition.DEF);
        }
        if(configuration.isSplitStrikers()){
            this.splitAllPlayersByPosition(composition.getTeamA(), composition.getTeamB(), composition.getAvailablePlayers(), PlayerPosition.ATT);
        }

        if(configuration.isSplitBestPlayers()){
            List<Player> twoBestPlayers = getNSortedPlayers(composition.getAvailablePlayers(), 2, true);
            if(twoBestPlayers.size()>1) {
                Collections.shuffle(twoBestPlayers);
                composition.getTeamA().getPlayers().add(twoBestPlayers.get(0));
                composition.getTeamB().getPlayers().add(twoBestPlayers.get(1));
                composition.getAvailablePlayers().remove(twoBestPlayers.get(0));
                composition.getAvailablePlayers().remove(twoBestPlayers.get(1));
            }
        }
        if(configuration.isSplitWorstPlayers()){
            List<Player> twoWorstPlayers = getNSortedPlayers(composition.getAvailablePlayers(), 2, false);
            if(twoWorstPlayers.size()>1) {
                Collections.shuffle(twoWorstPlayers);
                composition.getTeamA().getPlayers().add(twoWorstPlayers.get(0));
                composition.getTeamB().getPlayers().add(twoWorstPlayers.get(1));
                composition.getAvailablePlayers().remove(twoWorstPlayers.get(0));
                composition.getAvailablePlayers().remove(twoWorstPlayers.get(1));
            }
        }

        composition.setTeamA(generateRandomTeam(composition, TeamSide.A, nbPlayersTeamA));
        composition.setTeamB(generateRandomTeam(composition, TeamSide.B, nbPlayersTeamB));
        composition.setNbPlayersOnField(maxNbPlayerPerTeamOnField);
        return composition;
    }

    @SneakyThrows
    private void splitTwoPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        Random rand = SecureRandom.getInstanceStrong();
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        if(playersFound.size()>1){
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.getPlayers().add(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.getPlayers().add(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }

    @SneakyThrows
    private void splitAllPlayersByPosition(Team teamA, Team teamB, List<Player> availablePlayers, PlayerPosition position){
        Random rand = SecureRandom.getInstanceStrong();
        List<Player> playersFound = getPlayersByPosition(availablePlayers, position);
        while(playersFound.size()>1){
            Player firstPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamA.getPlayers().add(firstPlayer);
            playersFound.remove(firstPlayer);
            availablePlayers.remove(firstPlayer);
            Player secondPlayer = playersFound.get(rand.nextInt(playersFound.size()));
            teamB.getPlayers().add(secondPlayer);
            playersFound.remove(secondPlayer);
            availablePlayers.remove(secondPlayer);
        }
    }

    @SneakyThrows
    public Team generateRandomTeam(Composition composition, TeamSide teamSide, int maxNbPlayerPerTeam){
        List<Player> availablePlayers = new ArrayList<>(composition.getAvailablePlayers());
        Random rand       = SecureRandom.getInstanceStrong();
        int    i          = 0;
        Team team = teamSide==TeamSide.A ? composition.getTeamA() : composition.getTeamB();
        while(i < maxNbPlayerPerTeam && team.getPlayers().size()<maxNbPlayerPerTeam && !availablePlayers.isEmpty()) {
            int randomNum = rand.nextInt(availablePlayers.size());
            team.getPlayers().add(availablePlayers.get(randomNum));
            availablePlayers.remove(randomNum);
            i++;
        }
        composition.setAvailablePlayers(availablePlayers);
        return team;
    }

    @Override
    public boolean equals (Object obj){
        if(obj instanceof Composition)
        {
            Composition other = (Composition) obj;
            return (other.getTeamA().equals(this.getTeamA())
                    && other.getTeamB().equals(this.getTeamB()))
                   || (other.getTeamA().equals(this.getTeamB())
                       && other.getTeamB().equals(this.getTeamA()));
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        List<Player> playersA = new ArrayList<>(this.getTeamA().getPlayers());
        playersA.sort(Collections.reverseOrder());
        s.append("TEAM A [").append(new DecimalFormat("##.##").format(this.getTeamA().getRatingAverage())).append("]\n");
        for (Player p : playersA) {
            s.append("- ").append(p).append("\n");
        }
        s.append("[VS]\n");
        s.append("TEAM B [").append(new DecimalFormat("##.##").format(this.getTeamB().getRatingAverage())).append("]\n");
        List<Player> playersB = new ArrayList<>(this.getTeamB().getPlayers());
        playersB.sort(Collections.reverseOrder());
        for (Player p : playersB) {
            s.append("- ").append(p).append("\n");
        }
        return s.toString();

    }
}
