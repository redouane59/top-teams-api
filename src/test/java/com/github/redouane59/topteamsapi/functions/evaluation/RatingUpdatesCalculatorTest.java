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
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

@Log
class RatingUpdatesCalculatorTest {

    private Composition getIdenticalComposition(){
        Player p1 = new Player().withId("player1").withRating(50).withNbGamesPlayed(1);
        Player p2 = new Player().withId("player2").withRating(50).withNbGamesPlayed(1);
        Player p3 = new Player().withId("player3").withRating(50).withNbGamesPlayed(1);
        Player p4 = new Player().withId("player4").withRating(50).withNbGamesPlayed(1);
        Player p5 = new Player().withId("player5").withRating(50).withNbGamesPlayed(1);
        Player p6 = new Player().withId("player6").withRating(50).withNbGamesPlayed(1);
        Player p7 = new Player().withId("player7").withRating(50).withNbGamesPlayed(1);
        Player p8 = new Player().withId("player8").withRating(50).withNbGamesPlayed(1);
        Player p9 = new Player().withId("player9").withRating(50).withNbGamesPlayed(1);
        Player p10 = new Player().withId("player10").withRating(50).withNbGamesPlayed(1);
        Team   teamA = new Team().withPlayers(List.of(p1, p2, p3, p4, p5));
        Team   teamB = new Team().withPlayers(List.of(p6, p7, p8, p9, p10));
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    private Composition getCompositionWithDifferentNbGames(){
        Player p1 = new Player().withId("player1").withRating(50).withNbGamesPlayed(1);
        Player p2 = new Player().withId("player2").withRating(50).withNbGamesPlayed(10);
        Player p3 = new Player().withId("player3").withRating(50).withNbGamesPlayed(20);
        Player p4 = new Player().withId("player4").withRating(50).withNbGamesPlayed(30);
        Player p5 = new Player().withId("player5").withRating(50).withNbGamesPlayed(40);
        Player p6 = new Player().withId("player6").withRating(50).withNbGamesPlayed(5);
        Player p7 = new Player().withId("player7").withRating(50).withNbGamesPlayed(15);
        Player p8 = new Player().withId("player8").withRating(50).withNbGamesPlayed(25);
        Player p9 = new Player().withId("player9").withRating(50).withNbGamesPlayed(35);
        Player p10   = new Player().withId("player10").withRating(50).withNbGamesPlayed(45);
        Team   teamA = new Team().withPlayers(List.of(p1, p2, p3, p4, p5));
        Team   teamB = new Team().withPlayers(List.of(p6, p7, p8, p9, p10));
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    private Composition getdUnbalancedComposition(){
        Player p1 = new Player().withId("player1").withRating(50).withNbGamesPlayed(1);
        Player p2 = new Player().withId("player2").withRating(50).withNbGamesPlayed(1);
        Player p3 = new Player().withId("player3").withRating(50).withNbGamesPlayed(1);
        Player p4 = new Player().withId("player4").withRating(50).withNbGamesPlayed(1);
        Player p5 = new Player().withId("player5").withRating(50).withNbGamesPlayed(1);
        Player p6 = new Player().withId("player6").withRating(50).withNbGamesPlayed(1);
        Player p7 = new Player().withId("player7").withRating(50).withNbGamesPlayed(1);
        Player p8 = new Player().withId("player8").withRating(50).withNbGamesPlayed(1);
        Player p9 = new Player().withId("player9").withRating(50).withNbGamesPlayed(1);
        Team   teamA = new Team().withPlayers(List.of(p1, p2, p3, p4, p5));
        Team   teamB = new Team().withPlayers(List.of(p6, p7, p8, p9));
        return Composition.builder().teamA(teamA).teamB(teamB).build();
    }

    @Test
    void testWinAndLoosePointsAwins(){
        Composition composition = this.getIdenticalComposition();

        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
        log.fine(game.toString());
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
    void testWinAndLoosePointsBWins(){
        Composition composition = this.getIdenticalComposition();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(0,10));
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
    void testRelativeDistributionNone(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game                    game       = new Game().withComposition(composition).withScore(new Score(10, 0));
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
    void testRelativeDistributionLow(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.LOW);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10, 0));
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
    void testRelativeDistributionMedium(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.MEDIUM);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
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
    void testRelativeDistributionHigh(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setRelativeDistribution(RelativeDistribution.HIGH);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
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
    void testSplitPointFalse(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setSplitPointsByTeam(false);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
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
    void testSplitPointTrue(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        configuration.setSplitPointsByTeam(true);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
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
    void testUnbalancedCompositionSameOdd(){
        Composition composition = this.getdUnbalancedComposition();
        composition.setNbPlayersOnField(5);
        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getNbPlayersOnField());
        configuration.setSplitPointsByTeam(true);
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(20,0));
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
    void testPredictionAfterModif(){
        Composition composition = this.getIdenticalComposition();

        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
        double kf = CalculatorConfiguration.calculateKf(composition.getTeamA().getPlayers().size());

        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(new CalculatorConfiguration().withKf(kf));
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

    @Test
    void testWinAndLoosePointsAwinsWithGetUpdatedPlayers(){
        Composition composition = this.getIdenticalComposition();

        CalculatorConfiguration configuration = new CalculatorConfiguration(composition.getTeamA().getPlayers().size());
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game().withComposition(composition).withScore(new Score(10,0));
        log.fine(game.toString());
        List<Player> players = calculator.getUpdatedPlayers(game);
        for(Player player : players) {
            if(player.getId().equals("player1") || player.getId().equals("player2") || player.getId().equals("player3")
               || player.getId().equals("player4") || player.getId().equals("player5")){
                assertTrue(player.getRating()>player.getPreviousRating());
            }else{
                assertTrue(player.getRating()<player.getPreviousRating());
            }
        }
    }


}
