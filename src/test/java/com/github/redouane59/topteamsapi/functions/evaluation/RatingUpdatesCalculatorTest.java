package com.github.redouane59.topteamsapi.functions.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.github.redouane59.topteamsapi.model.Game;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.Score;
import com.github.redouane59.topteamsapi.model.Team;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RatingUpdatesCalculatorTest {

    private Composition getIdenticalComposition(){
        Player p1 = Player.builder().id("player1").rating(50).nbGamesPlayed(1).build();
        Player p2 = Player.builder().id("player2").rating(50).nbGamesPlayed(1).build();
        Player p3 = Player.builder().id("player3").rating(50).nbGamesPlayed(1).build();
        Player p4 = Player.builder().id("player4").rating(50).nbGamesPlayed(1).build();
        Player p5 = Player.builder().id("player5").rating(50).nbGamesPlayed(1).build();
        Player p6 = Player.builder().id("player6").rating(50).nbGamesPlayed(1).build();
        Player p7 = Player.builder().id("player7").rating(50).nbGamesPlayed(1).build();
        Player p8 = Player.builder().id("player8").rating(50).nbGamesPlayed(1).build();
        Player p9 = Player.builder().id("player9").rating(50).nbGamesPlayed(1).build();
        Player p10 = Player.builder().id("player10").rating(50).nbGamesPlayed(1).build();
        Team   teamA = Team.builder().players(List.of(p1, p2, p3, p4, p5)).build();
        Team   teamB = Team.builder().players(List.of(p6, p7, p8, p9, p10)).build();
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    private Composition getCompositionWithDifferentNbGames(){
        Player p1 = Player.builder().id("player1").rating(50).nbGamesPlayed(1).build();
        Player p2 = Player.builder().id("player2").rating(50).nbGamesPlayed(10).build();
        Player p3 = Player.builder().id("player3").rating(50).nbGamesPlayed(20).build();
        Player p4 = Player.builder().id("player4").rating(50).nbGamesPlayed(30).build();
        Player p5 = Player.builder().id("player5").rating(50).nbGamesPlayed(40).build();
        Player p6 = Player.builder().id("player6").rating(50).nbGamesPlayed(5).build();
        Player p7 = Player.builder().id("player7").rating(50).nbGamesPlayed(15).build();
        Player p8 = Player.builder().id("player8").rating(50).nbGamesPlayed(25).build();
        Player p9 = Player.builder().id("player9").rating(50).nbGamesPlayed(35).build();
        Player p10   = Player.builder().id("player10").rating(50).nbGamesPlayed(45).build();
        Team   teamA = Team.builder().players(List.of(p1, p2, p3, p4, p5)).build();
        Team   teamB = Team.builder().players(List.of(p6, p7, p8, p9, p10)).build();
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    private Composition getdUnbalancedComposition(){
        Player p1 = Player.builder().id("player1").rating(50).nbGamesPlayed(1).build();
        Player p2 = Player.builder().id("player2").rating(50).nbGamesPlayed(1).build();
        Player p3 = Player.builder().id("player3").rating(50).nbGamesPlayed(1).build();
        Player p4 = Player.builder().id("player4").rating(50).nbGamesPlayed(1).build();
        Player p5 = Player.builder().id("player5").rating(50).nbGamesPlayed(1).build();
        Player p6 = Player.builder().id("player6").rating(50).nbGamesPlayed(1).build();
        Player p7 = Player.builder().id("player7").rating(50).nbGamesPlayed(1).build();
        Player p8 = Player.builder().id("player8").rating(50).nbGamesPlayed(1).build();
        Player p9 = Player.builder().id("player9").rating(50).nbGamesPlayed(1).build();
        Team   teamA = Team.builder().players(List.of(p1, p2, p3, p4, p5)).build();
        Team   teamB = Team.builder().players(List.of(p6, p7, p8, p9)).build();;
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    @Test
    public void testWinAndLoosePointsAwins(){
        Composition composition = this.getIdenticalComposition();

        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        System.out.println(game);
        Map<String, Double> result = calculator.getRatingUpdates(game);
        for(Map.Entry<String, Double> entry : result.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if(key.equals("player1") || key.equals("player2") || key.equals("player3") || key.equals("player4")
                    || key.equals("player5")){
                assertTrue(value>0);
            }else{
                assertTrue(value<0);
            }
        }
    }

    @Test
    public void testWinAndLoosePointsBWins(){
        Composition composition = this.getIdenticalComposition();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(0,10)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        for(Map.Entry<String, Double> entry : result.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if(key.equals("player1") || key.equals("player2") || key.equals("player3") || key.equals("player4")
                    || key.equals("player5")){
                assertTrue(value<0);
            }else{
                assertTrue(value>0);
            }
        }
    }

    @Test
    public void testRelativeDistributionNone(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game                    game       = Game.builder().composition(composition).score(new Score(10, 0)).build();
        Map<String, Double>     result     = calculator.getRatingUpdates(game);
        assertEquals(result.get("player1"), result.get("player2"));
        assertEquals(result.get("player2"), result.get("player3"));
        assertEquals(result.get("player3"), result.get("player4"));
        assertEquals(result.get("player4"), result.get("player5"));
        assertEquals(result.get("player6"), result.get("player7"));
        assertEquals(result.get("player7"), result.get("player8"));
        assertEquals(result.get("player8"), result.get("player9"));
        assertEquals(result.get("player9"), result.get("player10"));
    }

    @Test
    public void testRelativeDistributionLow(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.LOW);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10, 0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        assertTrue(result.get("player1")>result.get("player2"));
        assertTrue(result.get("player2")>result.get("player3"));
        assertTrue(result.get("player3")>result.get("player4"));
        assertTrue(result.get("player4")>result.get("player5"));
        assertTrue(result.get("player6")<result.get("player3"));
        assertTrue(result.get("player7")<result.get("player4"));
        assertTrue(result.get("player8")<result.get("player5"));
        assertTrue(result.get("player9")<result.get("player10"));
    }

    @Test
    public void testRelativeDistributionMedium(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.MEDIUM);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        assertTrue(result.get("player1")>result.get("player2"));
        assertTrue(result.get("player2")>result.get("player3"));
        assertTrue(result.get("player3")>result.get("player4"));
        assertTrue(result.get("player4")>result.get("player5"));
        assertTrue(result.get("player6")<result.get("player3"));
        assertTrue(result.get("player7")<result.get("player4"));
        assertTrue(result.get("player8")<result.get("player5"));
        assertTrue(result.get("player9")<result.get("player10"));
    }

    @Test
    public void testRelativeDistributionHigh(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.HIGH);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        assertTrue(result.get("player1")>result.get("player2"));
        assertTrue(result.get("player2")>result.get("player3"));
        assertTrue(result.get("player3")>result.get("player4"));
        assertTrue(result.get("player4")>result.get("player5"));
        assertTrue(result.get("player6")<result.get("player3"));
        assertTrue(result.get("player7")<result.get("player4"));
        assertTrue(result.get("player8")<result.get("player5"));
        assertTrue(result.get("player9")<result.get("player10"));
    }

    @Test
    public void testSplitPointFalse(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setSplitPointsByTeam(false);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        double modifA = 0;
        double modifB = 0;
        for(Map.Entry<String, Double> entry : result.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if(key.equals("player1") || key.equals("player2") || key.equals("player3") || key.equals("player4")
                    || key.equals("player5")){
                modifA+=value;
            }else{
                modifB+=value;
            }
        }
        assertNotEquals(Math.abs(modifA), Math.abs(modifB), 0.0);
    }

    @Test
    public void testSplitPointTrue(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setSplitPointsByTeam(true);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        double modifA = 0;
        double modifB = 0;
        for(Map.Entry<String, Double> entry : result.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if(key.equals("player1") || key.equals("player2") || key.equals("player3") || key.equals("player4")
                    || key.equals("player5")){
                modifA+=value;
            }else{
                modifB+=value;
            }
        }
        assertTrue(Math.abs(modifA)-Math.abs(modifB)<0.001);
    }

    @Test
    public void testUnbalancedCompositionSameOdd(){
        Composition composition = this.getdUnbalancedComposition();
        composition.setNbPlayersOnField(5);
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getNbPlayersOnField());
        configuration.setSplitPointsByTeam(true);
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = Game.builder().composition(composition).score(new Score(20,0)).build();
        Map<String, Double> result = calculator.getRatingUpdates(game);
        double modifA = 0;
        double modifB = 0;
        for(Map.Entry<String, Double> entry : result.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if(key.equals("player1") || key.equals("player2") || key.equals("player3") || key.equals("player4")
                    || key.equals("player5")){
                modifA+=value;
            }else{
                modifB+=value;
            }
        }
        assertTrue(Math.abs(modifA)-Math.abs(modifB)<0.001);
        assertTrue(Math.abs(result.get("player1"))<Math.abs(result.get("player6")));
    }

    @Test
    public void testPredictionAfterModif(){
        Composition composition = this.getIdenticalComposition();

        Game game = Game.builder().composition(composition).score(new Score(10,0)).build();
        double kf = CalculatorConfiguration.calculateKf(composition.getTeamA().getPlayers().size());

        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(CalculatorConfiguration.builder().kf(kf).build());
        Map<String, Double> result = calculator.getRatingUpdates(game);

        assertEquals(0, composition.getPrediction(kf));

        for(int i=0; i<composition.getTeamA().getPlayers().size(); i++) {
            Player p = composition.getTeamA().getPlayers().get(i);
            p.setRating(p.getRating() + result.get(p.getId()));
        }
        for(int i=0; i<composition.getTeamB().getPlayers().size(); i++) {
            Player p = composition.getTeamB().getPlayers().get(i);
            p.setRating(p.getRating() + result.get(p.getId()));
        }
        assertEquals(10, composition.getPrediction(kf));
    }


}
