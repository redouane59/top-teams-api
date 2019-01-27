package com.amazonaws.functions.CompositionGenerator;

import com.amazonaws.functions.compositionGenerator.ComplexCompositionGenerator;
import com.amazonaws.functions.compositionGenerator.GeneratorConfiguration;
import com.amazonaws.model.AbstractComposition;
import com.amazonaws.model.Player;
import com.amazonaws.model.PlayerPosition;
import com.amazonaws.model.Team;
import com.amazonaws.model.complex.ComplexComposition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ComplexCompositionGeneratorTest {

    private int nbRandomTests = 3;
    private ComplexCompositionGenerator generator;
    private Player worst1 = new Player("worst1",30);
    private Player worst2 = new Player("worst2",35);
    private Player worst3 = new Player("worst3",40);
    private Player worst4 = new Player("worst4",45);
    private Player playerE = new Player("playerE",50);
    private Player best4 = new Player("best4",55);
    private Player best3 = new Player("best3",60);
    private Player best2 = new Player("best2",65);
    private Player best1 = new Player("best1",70);

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
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
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
            assertTrue(randomCompo.getTeams().get(0).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(1).getPlayers().size()==3);
            assertTrue(randomCompo.getTeams().get(2).getPlayers().size()==3);
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
            players.add(new Player("player J", 50));
            players.add(new Player("player k", 50));
            players.add(new Player("player L", 50));
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
            players.add(new Player("player J", 50));
            players.add(new Player("player k", 50));
            players.add(new Player("player L", 50));
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
        generator = new ComplexCompositionGenerator();
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
