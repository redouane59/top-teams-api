package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.topteamsapi.functions.composition.GeneratorConfiguration;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.github.redouane59.topteamsapi.model.composition.Composition;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompositionTest {
    private Player best1   = Player.builder().id("best1").rating(90).build();
    private Player best2   = Player.builder().id("best2").rating(89).build();
    private Player worst1  = Player.builder().id("worst1").rating(20).build();
    private Player worst2  = Player.builder().id("worst2").rating(21).build();
    private Player playerA = Player.builder().id("playerA").rating(30).build();
    private Player playerB = Player.builder().id("playerB").rating(50).build();
    private Player playerC = Player.builder().id("playerC").rating(50).build();
    private Player playerD = Player.builder().id("playerD").rating(50).build();
    private Player playerE = Player.builder().id("playerE").rating(50).build();
    private Player playerF = Player.builder().id("playerF").rating(55).build();

    @Test
    public void testEqualsOK(){
        Composition c1 = Composition.builder()
                                    .teamA(Team.builder().players(List.of(playerA, playerB, playerC)).build())
                                    .teamB(Team.builder().players(List.of(playerD, playerE, playerF)).build())
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamA(Team.builder().players(List.of(playerC, playerA, playerB)).build())
                                    .teamB(Team.builder().players(List.of(playerE, playerF, playerD)).build())
                                    .build();
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsOKopposite(){
        Composition c1 = Composition.builder()
                                    .teamA(Team.builder().players(List.of(playerA, playerB, playerC)).build())
                                    .teamB(Team.builder().players(List.of(playerD, playerE, playerF)).build())
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamB(Team.builder().players(List.of(playerC, playerA, playerB)).build())
                                    .teamA(Team.builder().players(List.of(playerE, playerF, playerD)).build())
                                    .build();
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsKO(){
        Composition c1 = Composition.builder()
                                    .teamA(Team.builder().players(List.of(playerA, playerB, playerC)).build())
                                    .teamB(Team.builder().players(List.of(playerD, playerE, playerF)).build())
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamB(Team.builder().players(List.of(playerC, playerA, playerB)).build())
                                    .teamA(Team.builder().players(List.of(playerE, playerF, worst1)).build())
                                    .build();
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testRatingDifferenceRegularMode(){
        Player player1 = Player.builder().id("player1").rating(10).build();
        Player player2 = Player.builder().id("player2").rating(20).build();
        Player player3 = Player.builder().id("player").rating(30).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3)).build();
        Player player4 = Player.builder().id("player4").rating(10).build();
        Player player5 = Player.builder().id("player5").rating(20).build();
        Player player6 = Player.builder().id("player6").rating(30).build();
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
        Player player1 = Player.builder().id("player1").rating(10).build();
        Player player2 = Player.builder().id("player2").rating(20).build();
        Player player3 = Player.builder().id("player3").rating(30).build();
        Player player4 = Player.builder().id("player4").rating(40).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3, player4)).build();
        Player player5 = Player.builder().id("player5").rating(10).build();
        Player player6 = Player.builder().id("player6").rating(20).build();
        Player player7 = Player.builder().id("player7").rating(30).build();
        Team teamB = Team.builder().players(List.of(player5, player6, player7)).build();
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(3);
        assertEquals(composition.getRatingAverageDifference(),
                     (teamA.getRatingAverage(composition.getNbPlayersOnField()) - teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertEquals(composition.getRatingDifference(),
                     ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testRatingDifferenceFreeMode(){
        Player player1 = Player.builder().id("player1").rating(10).build();
        Player player2 = Player.builder().id("player2").rating(20).build();
        Player player3 = Player.builder().id("player3").rating(30).build();
        Player player4 = Player.builder().id("player4").rating(40).build();
        Team teamA = Team.builder().players(List.of(player1, player2, player3, player4)).build();
        Player player5 = Player.builder().id("player5").rating(10).build();
        Player player6 = Player.builder().id("player6").rating(20).build();
        Player player7 = Player.builder().id("player7").rating(30).build();
        Team teamB = Team.builder().players(List.of(player5, player6, player7)).build();
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(4);
        assertEquals(composition.getRatingAverageDifference(),
                     (teamA.getRatingAverage(composition.getNbPlayersOnField()) - teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertEquals(composition.getRatingDifference(),
                     ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testGenerateRandomTeam(){
        for(int i=0; i<10; i++) {
            Composition composition = Composition.builder().availablePlayers(List.of(playerA, playerB, playerC)).build();
            Team team = composition.generateRandomTeam(3);
            assertEquals(3, team.getPlayers().size());
            assertTrue(team.isPlayerOnTeam(playerA.getId()));
            assertTrue(team.isPlayerOnTeam(playerB.getId()));
            assertTrue(team.isPlayerOnTeam(playerC.getId()));
        }
    }

    @Test
    public void testGenerateRandomCompositionConfigFalse(){
        GeneratorConfiguration configuration = GeneratorConfiguration.builder()
                                                                     .splitBestPlayers(false)
                                                                     .splitDefenders(false)
                                                                     .splitStrikers(false)
                                                                     .splitGoalKeepers(false)
                                                                     .splitBestPlayers(false)
                                                                     .splitWorstPlayers(false)
                                                                     .build();        for(int i=0; i<10; i++){
            Composition composition = (Composition)Composition.builder().availablePlayers(List.of(playerA, playerB, playerC, playerD))
                                                              .build().generateRandomComposition(configuration);
            assertEquals(2, composition.getTeamA().getPlayers().size());
            assertEquals(2, composition.getTeamB().getPlayers().size());
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerA.getId()) || composition.getTeamB().isPlayerOnTeam(playerA.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerB.getId()) || composition.getTeamB().isPlayerOnTeam(playerB.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerC.getId()) || composition.getTeamB().isPlayerOnTeam(playerC.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerD.getId()) || composition.getTeamB().isPlayerOnTeam(playerD.getId()));
        }
    }

    @Test
    public void testGenerateRandomCompositionConfigTrue(){
        GeneratorConfiguration configuration = new GeneratorConfiguration();
        for(int i=0; i<10; i++){
            Composition composition = (Composition)Composition.builder()
                                                              .availablePlayers(List.of(playerA, playerB, playerC, playerD, best1, best2, worst1, worst2))
                                                              .build().generateRandomComposition(configuration);
            assertEquals(4, composition.getTeamA().getPlayers().size());
            assertEquals(4, composition.getTeamB().getPlayers().size());
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerA.getId()) || composition.getTeamB().isPlayerOnTeam(playerA.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerB.getId()) || composition.getTeamB().isPlayerOnTeam(playerB.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerC.getId()) || composition.getTeamB().isPlayerOnTeam(playerC.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(playerD.getId()) || composition.getTeamB().isPlayerOnTeam(playerD.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(best1.getId()) || composition.getTeamB().isPlayerOnTeam(best1.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(best2.getId()) || composition.getTeamB().isPlayerOnTeam(best2.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(worst1.getId()) || composition.getTeamB().isPlayerOnTeam(worst1.getId()));
            assertTrue(composition.getTeamA().isPlayerOnTeam(worst2.getId()) || composition.getTeamB().isPlayerOnTeam(worst2.getId()));
        }
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
