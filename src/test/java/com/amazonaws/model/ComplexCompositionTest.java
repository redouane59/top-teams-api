package com.amazonaws.model;

import com.amazonaws.model.complex.ComplexComposition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplexCompositionTest {

    private Player playerA = new Player("playerA",30);
    private Player playerB = new Player("playerB",35);
    private Player playerC = new Player("playerC",40);
    private Player playerD = new Player("playerD",45);
    private Player playerE = new Player("playerE",50);
    private Player playerF = new Player("playerF",55);
    private Player playerG = new Player("playerG",60);
    private Player playerH = new Player("playerH",65);
    private Player playerI = new Player("playerI",70);

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        players.add(playerA);
        players.add(playerB);
        players.add(playerC);
        players.add(playerD);
        players.add(playerE);
        players.add(playerF);
        players.add(playerG);
        players.add(playerH);
        players.add(playerI);
        return players;
    }

    @Test
    public void testRatingDifference() {
        ComplexComposition compo = new ComplexComposition();
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(playerA, playerD, playerG));
        teams.add(new Team(playerB, playerE, playerH));
        teams.add(new Team(playerC, playerF, playerI));
        compo.setTeams(teams);
        assertTrue(compo.getRatingAverageDifference() == 5);
    //    assertTrue(compo.getRatingDifference() == 5);
    }

    @Test
    public void testEqualsOK(){
        ComplexComposition c1 = new ComplexComposition(null);
        c1.getTeams().add(new Team(playerA, playerB, playerC));
        c1.getTeams().add(new Team(playerD, playerE, playerF));
        c1.getTeams().add(new Team(playerG, playerH, playerI));
        ComplexComposition c2 = new ComplexComposition(null);
        c2.getTeams().add(new Team(playerC, playerA, playerB));
        c2.getTeams().add(new Team(playerE, playerF, playerD));
        c2.getTeams().add(new Team(playerI, playerG, playerH));
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
    }

    @Test
    public void testEqualsOKopposite(){
        ComplexComposition c1 = new ComplexComposition(null);
        c1.getTeams().add(new Team(playerA, playerB, playerC));
        c1.getTeams().add(new Team(playerD, playerE, playerF));
        c1.getTeams().add(new Team(playerG, playerH, playerI));
        ComplexComposition c2 = new ComplexComposition(null);
        c2.getTeams().add(new Team(playerC, playerA, playerB));
        c2.getTeams().add(new Team(playerH, playerG, playerI));
        c2.getTeams().add(new Team(playerE, playerF, playerD));
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
    }

    @Test
    public void testEqualsKO(){
        ComplexComposition c1 = new ComplexComposition(null);
        c1.getTeams().add(new Team(playerA, playerB, playerC));
        c1.getTeams().add(new Team(playerD, playerE, playerF));
        c1.getTeams().add(new Team(playerG, playerH, playerI));
        ComplexComposition c2 = new ComplexComposition(null);
        c2.getTeams().add(new Team(playerC, playerA, playerB));
        c2.getTeams().add(new Team(playerH, playerG, new Player("intru", 10)));
        c2.getTeams().add(new Team(playerE, playerF, playerD));
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));
    }

}
