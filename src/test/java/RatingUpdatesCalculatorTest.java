import com.amazonaws.model.*;
import com.amazonaws.ratingUpdateCalculator.CalculatorConfiguration;
import com.amazonaws.ratingUpdateCalculator.RatingUpdatesCalculator;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RatingUpdatesCalculatorTest {

    private Composition getIdenticalComposition(){
        Player p1 = new Player("player1", 50, 1);
        Player p2 = new Player("player2", 50, 1);
        Player p3 = new Player("player3", 50, 1);
        Player p4 = new Player("player4", 50, 1);
        Player p5 = new Player("player5", 50, 1);
        Player p6 = new Player("player6", 50, 1);
        Player p7 = new Player("player7", 50, 1);
        Player p8 = new Player("player8", 50, 1);
        Player p9 = new Player("player9", 50, 1);
        Player p10 = new Player("player10", 50, 1);
        Team teamA = new Team(p1, p2, p3, p4, p5);
        Team teamB = new Team(p6, p7, p8, p9, p10);
        return new Composition(teamA, teamB);
    }

    private Composition getCompositionWithDifferentNbGames(){
        Player p1 = new Player("player1", 50, 1);
        Player p2 = new Player("player2", 50, 10);
        Player p3 = new Player("player3", 50, 20);
        Player p4 = new Player("player4", 50, 30);
        Player p5 = new Player("player5", 50, 40);
        Player p6 = new Player("player6", 50, 5);
        Player p7 = new Player("player7", 50, 15);
        Player p8 = new Player("player8", 50, 25);
        Player p9 = new Player("player9", 50, 35);
        Player p10 = new Player("player10", 50, 45);
        Team teamA = new Team(p1, p2, p3, p4, p5);
        Team teamB = new Team(p6, p7, p8, p9, p10);
        return new Composition(teamA, teamB);
    }

    private Composition getdUnbalancedComposition(){
        Player p1 = new Player("player1", 50, 1);
        Player p2 = new Player("player2", 50, 1);
        Player p3 = new Player("player3", 50, 1);
        Player p4 = new Player("player4", 50, 1);
        Player p5 = new Player("player5", 50, 1);
        Player p6 = new Player("player6", 50, 1);
        Player p7 = new Player("player7", 50, 1);
        Player p8 = new Player("player8", 50, 1);
        Player p9 = new Player("player9", 50, 1);
        Team teamA = new Team(p1, p2, p3, p4, p5);
        Team teamB = new Team(p6, p7, p8, p9);
        return new Composition(teamA, teamB);
    }

    @Test
    public void testWinAndLoosePointsAwins(){
        Composition composition = this.getIdenticalComposition();
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(0,10));
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
        Map<String, Double> result = calculator.getRatingUpdates(game);
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setRelativeDistribution(RelativeDistribution.LOW);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setRelativeDistribution(RelativeDistribution.MEDIUM);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setRelativeDistribution(RelativeDistribution.HIGH);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setSplitPointsByTeam(false);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
        assertFalse(Math.abs(modifA)==Math.abs(modifB));
    }

    @Test
    public void testSplitPointTrue(){
        Composition composition = this.getCompositionWithDifferentNbGames();
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setSplitPointsByTeam(true);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(10,0));
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
    public void testUnbalancedCompositionSameFree(){
        Composition composition = this.getdUnbalancedComposition();
        composition.getTeamA().setNbPlayersOnField(5);
        composition.getTeamB().setNbPlayersOnField(4);
        CalculatorConfiguration configuration = new CalculatorConfiguration();
        configuration.setSplitPointsByTeam(true);
        configuration.setRelativeDistribution(RelativeDistribution.NONE);
        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(configuration);
        Game game = new Game(composition, new Score(20,0));
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

        Game game = new Game(composition, new Score(10,0));


        RatingUpdatesCalculator calculator = new RatingUpdatesCalculator();
        Map<String, Double> result = calculator.getRatingUpdates(game);

        double kf = calculator.getConfiguration().getKf(composition.getTeamA().getPlayers().size());

        assertTrue(composition.getPrediction(kf)==0);

        for(int i=0; i<composition.getTeamA().getPlayers().size(); i++) {
            Player p = composition.getTeamA().getPlayers().get(i);
            p.setRatingValue(p.getRatingValue()+result.get(p.getId()));
        }
        composition.getTeamA().updateRating();

        for(int i=0; i<composition.getTeamB().getPlayers().size(); i++) {
            Player p = composition.getTeamB().getPlayers().get(i);
            p.setRatingValue(p.getRatingValue()+result.get(p.getId()));
        }
        composition.getTeamB().updateRating();
        assertTrue(composition.getPrediction(kf)==10);
    }


}
