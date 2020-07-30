package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.github.redouane59.topteamsapi.model.composition.ComplexComposition;

class ComplexCompositionTest {

    private Player playerA = new Player().withId("playerA").withRating(30);
    private Player playerB = new Player().withId("playerB").withRating(35);
    private Player playerC = new Player().withId("playerC").withRating(40);
    private Player playerD = new Player().withId("playerD").withRating(45);
    private Player playerE = new Player().withId("playerE").withRating(50);
    private Player playerF = new Player().withId("playerF").withRating(55);
    private Player playerG = new Player().withId("playerG").withRating(60);
    private Player playerH = new Player().withId("playerH").withRating(65);
    private Player playerI = new Player().withId("playerI").withRating(70);

    @Test
    void testRatingDifference() {
        ComplexComposition compo = ComplexComposition.builder().build();
        List<Team>         teams = new ArrayList<>();
        teams.add(new Team().withPlayers(List.of(playerA, playerD, playerG)));
        teams.add(new Team().withPlayers(List.of(playerB, playerE, playerH)));
        teams.add(new Team().withPlayers(List.of(playerC, playerF, playerI)));
        compo.setTeams(teams);
        assertEquals(5, compo.getRatingAverageDifference());
    }

    @Test
    void testEqualsOK(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(new Team().withPlayers(List.of(playerA, playerB, playerC)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerD, playerE, playerF)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerG, playerH, playerI)));
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(new Team().withPlayers(List.of(playerC, playerA, playerB)));
        c2.getTeams().add(new Team().withPlayers(List.of(playerE, playerF, playerD)));
        c2.getTeams().add(new Team().withPlayers(List.of(playerI, playerG, playerH)));
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    void testEqualsOKopposite(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(new Team().withPlayers(List.of(playerA, playerB, playerC)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerD, playerE, playerF)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerG, playerH, playerI)));
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(new Team().withPlayers(List.of(playerC, playerA, playerB)));
        c2.getTeams().add(new Team().withPlayers(List.of(playerH, playerG, playerI)));
        c2.getTeams().add(new Team().withPlayers(List.of(playerE, playerF, playerD)));
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    void testEqualsKO(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(new Team().withPlayers(List.of(playerA, playerB, playerC)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerD, playerE, playerF)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerG, playerH, playerI)));
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(new Team().withPlayers(List.of(playerC, playerA, playerB)));
        c2.getTeams().add(new Team().withPlayers(List.of(playerH, playerG, new Player().withId("intru").withRating(10))));
        c2.getTeams().add(new Team().withPlayers(List.of(playerE, playerF, playerD)));
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    void testGetAllPlayers(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(new Team().withPlayers(List.of(playerA, playerB, playerC)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerD, playerE, playerF)));
        c1.getTeams().add(new Team().withPlayers(List.of(playerG, playerH, playerI)));
        assertEquals(9, c1.getAllPlayers().size());
        assertTrue(c1.getAllPlayers().contains(playerA));
        assertTrue(c1.getAllPlayers().contains(playerD));
        assertTrue(c1.getAllPlayers().contains(playerG));
    }

}
