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
    private Player best1   = new Player().withId("best1").withRating(90);
    private Player best2   = new Player().withId("best2").withRating(89);
    private Player worst1  = new Player().withId("worst1").withRating(20);
    private Player worst2  = new Player().withId("worst2").withRating(21);
    private Player playerA = new Player().withId("playerA").withRating(30);
    private Player playerB = new Player().withId("playerB").withRating(50);
    private Player playerC = new Player().withId("playerC").withRating(50);
    private Player playerD = new Player().withId("playerD").withRating(50);
    private Player playerE = new Player().withId("playerE").withRating(50);
    private Player playerF = new Player().withId("playerF").withRating(55);

    @Test
    public void testEqualsOK(){
        Composition c1 = Composition.builder()
                                    .teamA(new Team().withPlayers(List.of(playerA, playerB, playerC)))
                                    .teamB(new Team().withPlayers(List.of(playerD, playerE, playerF)))
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamA(new Team().withPlayers(List.of(playerC, playerA, playerB)))
                                    .teamB(new Team().withPlayers(List.of(playerE, playerF, playerD)))
                                    .build();
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsOKopposite(){
        Composition c1 = Composition.builder()
                                    .teamA(new Team().withPlayers(List.of(playerA, playerB, playerC)))
                                    .teamB(new Team().withPlayers(List.of(playerD, playerE, playerF)))
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamB(new Team().withPlayers(List.of(playerC, playerA, playerB)))
                                    .teamA(new Team().withPlayers(List.of(playerE, playerF, playerD)))
                                    .build();
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsKO(){
        Composition c1 = Composition.builder()
                                    .teamA(new Team().withPlayers(List.of(playerA, playerB, playerC)))
                                    .teamB(new Team().withPlayers(List.of(playerD, playerE, playerF)))
                                    .build();
        Composition c2 = Composition.builder()
                                    .teamB(new Team().withPlayers(List.of(playerC, playerA, playerB)))
                                    .teamA(new Team().withPlayers(List.of(playerE, playerF, worst1)))
                                    .build();
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testRatingDifferenceRegularMode(){
        Player player1 = new Player().withId("player1").withRating(10);
        Player player2 = new Player().withId("player2").withRating(20);
        Player player3 = new Player().withId("player").withRating(30);
        Team teamA = new Team().withPlayers(List.of(player1, player2, player3));
        Player player4 = new Player().withId("player4").withRating(10);
        Player player5 = new Player().withId("player5").withRating(20);
        Player player6 = new Player().withId("player6").withRating(30);
        Team teamB = new Team().withPlayers(List.of(player4, player5, player6));
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(3);
        assertEquals(composition.getRatingAverageDifference(),
                     (teamA.getRatingAverage(composition.getNbPlayersOnField()) - teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertEquals(composition.getRatingDifference(),
                     ((teamA.getRatingSum(composition.getNbPlayersOnField()) - teamB.getRatingSum(composition.getNbPlayersOnField()))));
    }

    @Test
    public void testRatingDifferenceSubstituteMode(){
        Player player1 = new Player().withId("player1").withRating(10);
        Player player2 = new Player().withId("player2").withRating(20);
        Player player3 = new Player().withId("player3").withRating(30);
        Player player4 = new Player().withId("player4").withRating(40);
        Team teamA = new Team().withPlayers(List.of(player1, player2, player3, player4));
        Player player5 = new Player().withId("player5").withRating(10);
        Player player6 = new Player().withId("player6").withRating(20);
        Player player7 = new Player().withId("player7").withRating(30);
        Team teamB = new Team().withPlayers(List.of(player5, player6, player7));
        Composition composition = Composition.builder().teamA(teamA).teamB(teamB).build();
        composition.setNbPlayersOnField(3);
        assertEquals(composition.getRatingAverageDifference(),
                     (teamA.getRatingAverage(composition.getNbPlayersOnField()) - teamB.getRatingAverage(composition.getNbPlayersOnField()))); // -15
        assertEquals(composition.getRatingDifference(),
                     ((teamA.getRatingSum(composition.getNbPlayersOnField())) - teamB.getRatingSum(composition.getNbPlayersOnField())));
    }

    @Test
    public void testRatingDifferenceFreeMode(){
        Player player1 = new Player().withId("player1").withRating(10);
        Player player2 = new Player().withId("player2").withRating(20);
        Player player3 = new Player().withId("player3").withRating(30);
        Player player4 = new Player().withId("player4").withRating(40);
        Team teamA = new Team().withPlayers(List.of(player1, player2, player3, player4));
        Player player5 = new Player().withId("player5").withRating(10);
        Player player6 = new Player().withId("player6").withRating(20);
        Player player7 = new Player().withId("player7").withRating(30);
        Team teamB = new Team().withPlayers(List.of(player5, player6, player7));
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
        GeneratorConfiguration configuration = new GeneratorConfiguration()
            .withSplitBestPlayers(false)
            .withSplitDefenders(false)
            .withSplitStrikers(false)
            .withSplitGoalKeepers(false)
            .withSplitBestPlayers(false)
            .withSplitWorstPlayers(false);
        for(int i=0; i<10; i++){
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
