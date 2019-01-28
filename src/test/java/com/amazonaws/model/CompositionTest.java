package com.amazonaws.model;

import com.amazonaws.functions.ratingUpdatesCalculator.CalculatorConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompositionTest {
    private Player best1 = new Player("best1",90);
    private Player best2 = new Player("best2",89);
    private Player worst1 = new Player("worst1",20);
    private Player worst2 = new Player("worst2",21);
    private Player playerA = new Player("playerA",30);
    private Player playerB = new Player("playerB",50);
    private Player playerC = new Player("playerC",50);
    private Player playerD = new Player("playerD",50);
    private Player playerE = new Player("playerE",50);
    private Player playerF = new Player("playerF",55);

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        players.add(best1);
        players.add(best2);
        players.add(worst1);
        players.add(worst2);
        players.add(playerA);
        players.add(playerB);
        players.add(playerC);
        players.add(playerD);
        players.add(playerE);
        players.add(playerF);
        return players;
    }

    private Composition getdTwoDifferentRatingTeamsComposition(){
        Player p1 = new Player("player1", 70, 1);
        Player p2 = new Player("player2", 70, 1);
        Player p3 = new Player("player3", 70, 1);
        Player p4 = new Player("player4", 70, 1);
        Player p5 = new Player("player5", 70, 1);
        Player p6 = new Player("player6", 60, 1);
        Player p7 = new Player("player7", 60, 1);
        Player p8 = new Player("player8", 60, 1);
        Player p9 = new Player("player9", 60, 1);
        Player p10 = new Player("player10", 60, 1);
        Team teamA = new Team(p1, p2, p3, p4, p5);
        Team teamB = new Team(p6, p7, p8, p9, p10);
        return new Composition(teamA, teamB);
    }

    @Test
    public void testEqualsOK(){
        Composition c1 = new Composition(null);
        c1.setTeamA(new Team(playerA, playerB, playerC));
        c1.setTeamB(new Team(playerD, playerE, playerF));
        Composition c2 = new Composition(null);
        c2.setTeamA(new Team(playerC, playerA, playerB));
        c2.setTeamB(new Team(playerE, playerF, playerD));
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsOKopposite(){
        Composition c1 = new Composition(null);
        c1.setTeamA(new Team(playerA, playerB, playerC));
        c1.setTeamB(new Team(playerD, playerE, playerF));
        Composition c2 = new Composition(null);
        c2.setTeamB(new Team(playerC, playerA, playerB));
        c2.setTeamA(new Team(playerE, playerF, playerD));
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsKO(){
        Composition c1 = new Composition(null);
        c1.setTeamA(new Team(playerA, playerB, playerC));
        c1.setTeamB(new Team(playerD, playerE, playerF));
        Composition c2 = new Composition(null);
        c2.setTeamA(new Team(playerC, playerA, playerB));
        c2.setTeamB(new Team(playerE, playerF, worst1));
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testRatingDifferenceRegularMode(){
        Composition c1 = new Composition(null);
        Player player1 = new Player("player1", 10);
        Player player2 = new Player("player2", 20);
        Player player3 = new Player("player", 30);
        Team teamA = new Team(player1, player2, player3);
        Player player4 = new Player("player4", 10);
        Player player5 = new Player("player5", 20);
        Player player6 = new Player("player6", 30);
        Team teamB = new Team(player4, player5, player6);
        Composition composition = new Composition(teamA, teamB);
        composition.setNbPlayersOnField(3);
        assertTrue(composition.getRatingAverageDifference() ==
                (teamA.getRatingAverage(composition.getNbPlayersOnField())-teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertTrue(composition.getRatingDifference() ==
                ((teamA.getRatingSum(composition.getNbPlayersOnField()) - teamB.getRatingSum(composition.getNbPlayersOnField()))));

    }

    @Test
    public void testRatingDifferenceSubstituteMode(){
        Player player1 = new Player("player1", 10);
        Player player2 = new Player("player2", 20);
        Player player3 = new Player("player3", 30);
        Player player4 = new Player("player4", 40);
        Team teamA = new Team(player1, player2, player3, player4);
        Player player5 = new Player("player5", 10);
        Player player6 = new Player("player6", 20);
        Player player7 = new Player("player7", 30);
        Team teamB = new Team(player5, player6, player7);
        Composition composition = new Composition(teamA, teamB);
        composition.setNbPlayersOnField(3);
        assertTrue(composition.getRatingAverageDifference() ==
                (teamA.getRatingAverage(composition.getNbPlayersOnField())-teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertTrue(composition.getRatingDifference() ==
                ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testRatingDifferenceFreeMode(){
        Player player1 = new Player("player1", 10);
        Player player2 = new Player("player2", 20);
        Player player3 = new Player("player3", 30);
        Player player4 = new Player("player4", 40);
        Team teamA = new Team(player1, player2, player3, player4);
        Player player5 = new Player("player5", 10);
        Player player6 = new Player("player6", 20);
        Player player7 = new Player("player7", 30);
        Team teamB = new Team(player5, player6, player7);
        Composition composition = new Composition(teamA, teamB);
        composition.setNbPlayersOnField(4);
        assertTrue(composition.getRatingAverageDifference() ==
                (teamA.getRatingAverage(composition.getNbPlayersOnField())-teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertTrue(composition.getRatingDifference() ==
                ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testPredictionWithKf(){
        Composition composition = this.getdTwoDifferentRatingTeamsComposition();
        CalculatorConfiguration config = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        assertTrue(composition.getPrediction(CalculatorConfiguration.calculateKf(6))<composition.getPrediction(config.calculateKf(5)));
        assertTrue(composition.getPrediction(CalculatorConfiguration.calculateKf(5))<composition.getPrediction(config.calculateKf(4)));
    }
}
