package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.github.redouane59.topteamsapi.model.composition.ComplexComposition;

public class ComplexCompositionTest {

    private Player playerA = Player.builder().id("playerA").rating(30).build();
    private Player playerB = Player.builder().id("playerB").rating(35).build();
    private Player playerC = Player.builder().id("playerC").rating(40).build();
    private Player playerD = Player.builder().id("playerD").rating(45).build();
    private Player playerE = Player.builder().id("playerE").rating(50).build();
    private Player playerF = Player.builder().id("playerF").rating(55).build();
    private Player playerG = Player.builder().id("playerG").rating(60).build();
    private Player playerH = Player.builder().id("playerH").rating(65).build();
    private Player playerI = Player.builder().id("playerI").rating(70).build();

    @Test
    public void testRatingDifference() {
        ComplexComposition compo = ComplexComposition.builder().build();
        List<Team>         teams = new ArrayList<>();
        teams.add(Team.builder().players(List.of(playerA, playerD, playerG)).build());
        teams.add(Team.builder().players(List.of(playerB, playerE, playerH)).build());
        teams.add(Team.builder().players(List.of(playerC, playerF, playerI)).build());
        compo.setTeams(teams);
        assertEquals(5, compo.getRatingAverageDifference());
    }

    @Test
    public void testEqualsOK(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerG, playerH, playerI)).build());
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.getTeams().add(Team.builder().players(List.of(playerE, playerF, playerD)).build());
        c2.getTeams().add(Team.builder().players(List.of(playerI, playerG, playerH)).build());
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsOKopposite(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerG, playerH, playerI)).build());
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.getTeams().add(Team.builder().players(List.of(playerH, playerG, playerI)).build());
        c2.getTeams().add(Team.builder().players(List.of(playerE, playerF, playerD)).build());
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsKO(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerG, playerH, playerI)).build());
        ComplexComposition c2 = ComplexComposition.builder().build();
        c2.getTeams().add(Team.builder().players(List.of(playerC, playerA, playerB)).build());
        c2.getTeams().add(Team.builder().players(List.of(playerH, playerG, Player.builder().id("intru").rating(10).build())).build());
        c2.getTeams().add(Team.builder().players(List.of(playerE, playerF, playerD)).build());
        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testGetAllPlayers(){
        ComplexComposition c1 = ComplexComposition.builder().build();
        c1.getTeams().add(Team.builder().players(List.of(playerA, playerB, playerC)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerD, playerE, playerF)).build());
        c1.getTeams().add(Team.builder().players(List.of(playerG, playerH, playerI)).build());
        assertEquals(9, c1.getAllPlayers().size());
        assertTrue(c1.getAllPlayers().contains(playerA));
        assertTrue(c1.getAllPlayers().contains(playerD));
        assertTrue(c1.getAllPlayers().contains(playerG));
    }

}
