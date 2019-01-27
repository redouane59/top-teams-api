package com.amazonaws.functions.approxAlgorithm;

import com.amazonaws.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ApproxCalculatorTest {

    private List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("player1", 0, 1); // 50
        Player p2 = new Player("player2", 0, 1); // 60
        Player p3 = new Player("player3", 0, 1); // 70
        Player p4 = new Player("player4", 0, 1); // 80
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        return players;
    }

    @Test
    public void test1(){
        List<Player> players = this.getPlayers();
        List<Game> games = new ArrayList<>();
        // game 1
        Team tA = new Team(players.get(0), players.get(1));
        Team tB = new Team(players.get(2), players.get(3));
        Game game = new Game(new Composition(tA, tB), new Score(0,4));
        games.add(game);
        // game 2
        tA = new Team(players.get(0), players.get(2));
        tB = new Team(players.get(1), players.get(3));
        game = new Game(new Composition(tA, tB), new Score(0,2));
        games.add(game);
        // game 3
        tA = new Team(players.get(0), players.get(3));
        tB = new Team(players.get(1), players.get(2));
        game = new Game(new Composition(tA, tB), new Score(2,2));
        games.add(game);
        ApproxCalculator calculator = new ApproxCalculator(games,players);
        calculator.launch();
    }
}
