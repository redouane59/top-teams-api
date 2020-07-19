package org.redouane59.topteamsapi.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.redouane59.topteamsapi.functions.compositionGenerator.ComplexCompositionGenerator;
import org.redouane59.topteamsapi.functions.compositionGenerator.GeneratorConfiguration;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.PlayerPosition;
import org.redouane59.topteamsapi.model.Team;
import org.redouane59.topteamsapi.model.composition.AbstractComposition;
import org.redouane59.topteamsapi.model.composition.ComplexComposition;

public class ComplexCompositionGeneratorTest {

    private int                         nbRandomTests = 3;
    private ComplexCompositionGenerator generator;
    private Player                      worst1        = Player.builder().id("worst1").ratingValue(30).build();
    private Player                      worst2        = Player.builder().id("worst2").ratingValue(35).build();
    private Player                      worst3        = Player.builder().id("worst3").ratingValue(40).build();
    private Player                      worst4        = Player.builder().id("worst4").ratingValue(45).build();
    private Player                      playerE       = Player.builder().id("playerE").ratingValue(50).build();
    private Player                      best4         = Player.builder().id("best4").ratingValue(55).build();
    private Player best3 = Player.builder().id("best3").ratingValue(60).build();
    private Player best2 = Player.builder().id("best2").ratingValue(65).build();
    private Player best1 = Player.builder().id("best1").ratingValue(70).build();

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        players.add(worst1);
        players.add(worst2);
        players.add(worst3);
        players.add(worst4);
        players.add(playerE);
        players.add(best4);
        players.add(best3);
        players.add(best2);
        players.add(best1);
        return players;
    }

    @Test
    public void test3teamsSplitBest(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(3);
        config.setSplitBestPlayers(true);
        config.setSplitWorstPlayers(false);
        config.setSplitGoalKeepers(false);
        generator = new ComplexCompositionGenerator(config);

        ComplexComposition randomCompo;
        for(int i=0; i<this.nbRandomTests;i++){
            randomCompo = generator.buildRandomComposition(getPlayers());
            assertEquals(3, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, best1, best2));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best2, best3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best1, best3));
            assertEquals(3, randomCompo.getTeams().get(0).getPlayers().size());
            assertEquals(3, randomCompo.getTeams().get(1).getPlayers().size());
            assertEquals(3, randomCompo.getTeams().get(2).getPlayers().size());
        }
    }

    @Test
    public void test3teamsSplitWorst(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(3);
        config.setSplitBestPlayers(false);
        config.setSplitWorstPlayers(true);
        config.setSplitGoalKeepers(false);
        generator = new ComplexCompositionGenerator(config);
        ComplexComposition randomCompo;
        for(int i=0; i<this.nbRandomTests;i++){
            randomCompo = generator.buildRandomComposition(getPlayers());
            assertEquals(3, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst1, worst2));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst1, worst3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, worst3));
            assertEquals(3, randomCompo.getTeams().get(0).getPlayers().size());
            assertEquals(3, randomCompo.getTeams().get(1).getPlayers().size());
            assertEquals(3, randomCompo.getTeams().get(2).getPlayers().size());
        }
    }

    @Test
    public void test4teamsSplitBest(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(4);
        config.setSplitBestPlayers(true);
        config.setSplitWorstPlayers(false);
        config.setSplitGoalKeepers(false);
        generator = new ComplexCompositionGenerator(config);
        ComplexComposition randomCompo;
        for(int i=0; i<this.nbRandomTests;i++){
            List<Player> players = getPlayers();
            players.add(Player.builder().id("player J").ratingValue(50).build());
            players.add(Player.builder().id("player k").ratingValue(50).build());
            players.add(Player.builder().id("player L").ratingValue(50).build());
            randomCompo = generator.buildRandomComposition(players);
            assertEquals(4, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, best1, best2));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best1, best3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best1, best4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best2, best3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best2, best4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best3, best4));
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
        }

    }

    @Test
    public void test4teamsSplitWorst(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(4);
        config.setSplitBestPlayers(false);
        config.setSplitWorstPlayers(true);
        config.setSplitGoalKeepers(false);
        generator = new ComplexCompositionGenerator(config);
        ComplexComposition randomCompo;
        for(int i=0; i<this.nbRandomTests;i++) {
            List<Player> players = getPlayers();
            players.add(Player.builder().id("player J").ratingValue(50).build());
            players.add(Player.builder().id("player k").ratingValue(50).build());
            players.add(Player.builder().id("player L").ratingValue(50).build());
            randomCompo = generator.buildRandomComposition(players);
            assertEquals(4, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst1, worst2));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst1, worst3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst1, worst4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, worst3));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, worst4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst3, worst4));
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
        }
    }

    @Test
    public void test3teamsSplitAsMuchGKasTeams(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(3);
        config.setSplitBestPlayers(false);
        config.setSplitWorstPlayers(true);
        config.setSplitGoalKeepers(true);
        generator = new ComplexCompositionGenerator(config);
        worst2.setPosition(PlayerPosition.GK);
        worst4.setPosition(PlayerPosition.GK);
        best4.setPosition(PlayerPosition.GK);
        ComplexComposition randomCompo;

        for(int i=0; i<this.nbRandomTests;i++){
            randomCompo = generator.buildRandomComposition(getPlayers());
            assertEquals(3, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, worst4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, best4));
            assertFalse(this.playerOnTheSameTeam(randomCompo, best4, worst4));
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
        }
    }

    @Test
    public void test3teamsSplitLessGKthanTeams(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(3);
        config.setSplitBestPlayers(false);
        config.setSplitWorstPlayers(true);
        config.setSplitGoalKeepers(true);
        generator = new ComplexCompositionGenerator(config);
        worst2.setPosition(PlayerPosition.GK);
        worst4.setPosition(PlayerPosition.GK);
        ComplexComposition randomCompo;

        for(int i=0; i<this.nbRandomTests;i++){
            randomCompo = generator.buildRandomComposition(getPlayers());
            assertEquals(3, randomCompo.getTeams().size());
            assertFalse(this.playerOnTheSameTeam(randomCompo, worst2, worst4));
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
        }
    }

    @Test
    public void test3teamsSplitMoreGKthanTeams(){
        GeneratorConfiguration config = new GeneratorConfiguration();
        config.setNbTeamsNeeded(3);
        config.setSplitBestPlayers(false);
        config.setSplitWorstPlayers(true);
        config.setSplitGoalKeepers(true);
        generator = new ComplexCompositionGenerator(config);
        worst2.setPosition(PlayerPosition.GK);
        worst4.setPosition(PlayerPosition.GK);
        best2.setPosition(PlayerPosition.GK);
        best4.setPosition(PlayerPosition.GK);
        ComplexComposition randomCompo;

        for(int i=0; i<this.nbRandomTests;i++){
            randomCompo = generator.buildRandomComposition(getPlayers());
            assertEquals(3, randomCompo.getTeams().size());
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(0).isPlayerOnTeam(worst2.getId())
                    || randomCompo.getTeams().get(0).isPlayerOnTeam(worst4.getId())
                    || randomCompo.getTeams().get(0).isPlayerOnTeam(best4.getId())
                    || randomCompo.getTeams().get(0).isPlayerOnTeam(best2.getId()));
            assertTrue(randomCompo.getTeams().get(1).isPlayerOnTeam(worst2.getId())
                    || randomCompo.getTeams().get(1).isPlayerOnTeam(worst4.getId())
                    || randomCompo.getTeams().get(1).isPlayerOnTeam(best4.getId())
                    || randomCompo.getTeams().get(1).isPlayerOnTeam(best2.getId()));
            assertTrue(randomCompo.getTeams().get(2).isPlayerOnTeam(worst2.getId())
                    || randomCompo.getTeams().get(2).isPlayerOnTeam(worst4.getId())
                    || randomCompo.getTeams().get(2).isPlayerOnTeam(best4.getId())
                    || randomCompo.getTeams().get(2).isPlayerOnTeam(best2.getId()));
        }
    }

    @Test
    public void testNBestResult(){
        generator = new ComplexCompositionGenerator(new GeneratorConfiguration());
        generator.getConfiguration().setNbCompositionsNeeded(5);
        List<AbstractComposition> result = generator.getNBestCompositions(getPlayers());
        assertTrue(Math.abs(result.get(0).getRatingDifference())<=Math.abs(result.get(1).getRatingDifference()));
        assertTrue(Math.abs(result.get(1).getRatingDifference())<=Math.abs(result.get(2).getRatingDifference()));
        assertTrue(Math.abs(result.get(2).getRatingDifference())<=Math.abs(result.get(3).getRatingDifference()));
        assertTrue(Math.abs(result.get(3).getRatingDifference())<=Math.abs(result.get(4).getRatingDifference()));
    }

    private boolean playerOnTheSameTeam(ComplexComposition compo, Player p1, Player p2){
        for(Team team : compo.getTeams()){
            if(team.getPlayers().contains(p1) && team.getPlayers().contains(p2)){
                return true;
            }
        }
        return false;
    }
}
