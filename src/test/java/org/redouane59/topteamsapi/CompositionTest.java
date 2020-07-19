package org.redouane59.topteamsapi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.composition.Composition;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompositionTest {
    private Player best1   = Player.builder().id("best1").ratingValue(90).build();
    private Player best2   = Player.builder().id("best2").ratingValue(89).build();
    private Player worst1  = Player.builder().id("worst1").ratingValue(20).build();
    private Player worst2  = Player.builder().id("worst2").ratingValue(21).build();
    private Player playerA = Player.builder().id("playerA").ratingValue(30).build();
    private Player playerB = Player.builder().id("playerB").ratingValue(50).build();
    private Player playerC = Player.builder().id("playerC").ratingValue(50).build();
    private Player playerD = Player.builder().id("playerD").ratingValue(50).build();
    private Player playerE = Player.builder().id("playerE").ratingValue(50).build();
    private Player playerF = Player.builder().id("playerF").ratingValue(55).build();

    private Composition getdTwoDifferentRatingTeamsComposition(){
        Player p1 = Player.builder().id("player1").ratingValue(70).nbGamesPlayed(1).build();
        Player p2 = Player.builder().id("player2").ratingValue(70).nbGamesPlayed(1).build();
        Player p3 = Player.builder().id("player3").ratingValue(70).nbGamesPlayed(1).build();
        Player p4 = Player.builder().id("player4").ratingValue(70).nbGamesPlayed(1).build();
        Player p5 = Player.builder().id("player5").ratingValue(70).nbGamesPlayed(1).build();
        Player p6 = Player.builder().id("player6").ratingValue(60).nbGamesPlayed(1).build();
        Player p7 = Player.builder().id("player7").ratingValue(60).nbGamesPlayed(1).build();
        Player p8 = Player.builder().id("player8").ratingValue(60).nbGamesPlayed(1).build();
        Player p9 = Player.builder().id("player9").ratingValue(60).nbGamesPlayed(1).build();
        Player p10   = Player.builder().id("player10").ratingValue(60).nbGamesPlayed(1).build();
        Team   teamA = Team.builder().players(List.of(p1, p2, p3, p4, p5)).build();
        Team   teamB = Team.builder().players(List.of(p6, p7, p8, p9, p10)).build();
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    @Test
    public void testEqualsOK(){
        Composition c1 = new Composition();
        c1.setTeamA(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.setTeamB(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        Composition c2 = new Composition();
        c2.setTeamA(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.setTeamB(Team.builder().players(List.of(playerE, playerF, playerD)).build());
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsOKopposite(){
        Composition c1 = new Composition();
        c1.setTeamA(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.setTeamB(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        Composition c2 = new Composition();
        c2.setTeamB(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.setTeamA(Team.builder().players(List.of(playerE, playerF, playerD)).build());
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsKO(){
        Composition c1 = new Composition();
        c1.setTeamA(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.setTeamB(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        Composition c2 = new Composition();;
        c2.setTeamA(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.setTeamB(Team.builder().players(List.of(playerE, playerF, worst1)).build());
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testRatingDifferenceRegularMode(){
        Composition c1 = new Composition();;
        Player player1 = Player.builder().id("player1").ratingValue(10).build();
        Player player2 = Player.builder().id("player2").ratingValue(20).build();
        Player player3 = Player.builder().id("player").ratingValue(30).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3)).build();
        Player player4 = Player.builder().id("player4").ratingValue(10).build();
        Player player5 = Player.builder().id("player5").ratingValue(20).build();
        Player player6 = Player.builder().id("player6").ratingValue(30).build();
        Team teamB = Team.builder().players(List.of(player4, player5, player6)).build();
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(3);
        assertEquals(composition.getRatingAverageDifference(),
                     (teamA.getRatingAverage(composition.getNbPlayersOnField()) - teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertEquals(composition.getRatingDifference(),
                     ((teamA.getRatingSum(composition.getNbPlayersOnField()) - teamB.getRatingSum(composition.getNbPlayersOnField()))));
    }

    @Test
    public void testRatingDifferenceSubstituteMode(){
        Player player1 = Player.builder().id("player1").ratingValue(10).build();
        Player player2 = Player.builder().id("player2").ratingValue(20).build();
        Player player3 = Player.builder().id("player3").ratingValue(30).build();
        Player player4 = Player.builder().id("player4").ratingValue(40).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3, player4)).build();
        Player player5 = Player.builder().id("player5").ratingValue(10).build();
        Player player6 = Player.builder().id("player6").ratingValue(20).build();
        Player player7 = Player.builder().id("player7").ratingValue(30).build();
        Team teamB = Team.builder().players(List.of(player5, player6, player7)).build();
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(3);
        assertTrue(composition.getRatingAverageDifference() ==
                (teamA.getRatingAverage(composition.getNbPlayersOnField())-teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertTrue(composition.getRatingDifference() ==
                ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testRatingDifferenceFreeMode(){
        Player player1 = Player.builder().id("player1").ratingValue(10).build();
        Player player2 = Player.builder().id("player2").ratingValue(20).build();
        Player player3 = Player.builder().id("player3").ratingValue(30).build();
        Player player4 = Player.builder().id("player4").ratingValue(40).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3, player4)).build();
        Player player5 = Player.builder().id("player5").ratingValue(10).build();
        Player player6 = Player.builder().id("player6").ratingValue(20).build();
        Player player7 = Player.builder().id("player7").ratingValue(30).build();
        Team teamB = Team.builder().players(List.of(player5, player6, player7)).build();
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(4);
        assertTrue(composition.getRatingAverageDifference() ==
                (teamA.getRatingAverage(composition.getNbPlayersOnField())-teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertTrue(composition.getRatingDifference() ==
                ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    /* @todo to put somewhere else
    @Test
    public void testPredictionWithKf(){
        Composition composition = this.getdTwoDifferentRatingTeamsComposition();
        CalculatorConfiguration config = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        assertTrue(composition.getPrediction(CalculatorConfiguration.calculateKf(6))<composition.getPrediction(config.calculateKf(5)));
        assertTrue(composition.getPrediction(CalculatorConfiguration.calculateKf(5))<composition.getPrediction(config.calculateKf(4)));
    }  */
}
