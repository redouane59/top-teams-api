package com.amazonaws.model;

import com.amazonaws.functions.ratingUpdatesCalculator.CalculatorConfiguration;
import com.amazonaws.functions.ratingUpdatesCalculator.RatingUpdatesCalculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Launcher {

	public static void main(String[] args) throws IOException {

		List<Player> players = new ArrayList<>();
		Player p1 = new Player("player1", 39, 20);
		Player p2 = new Player("player2", 50, 20);
		Player p3 = new Player("player3", 50, 20);
		Player p4 = new Player("player4", 50, 20);
		Player p5 = new Player("player5", 60, 20);
		Player p6 = new Player("player6", 60, 1);
		Player p7 = new Player("player7", 69, 1);
		Player p8 = new Player("player8", 71, 1);
		Player p9 = new Player("player9", 80, 1);
		Player p10 = new Player("player10", 80, 1);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		players.add(p6);
		players.add(p7);
		players.add(p8);
		players.add(p9);
		players.add(p10);

		Team teamA = new Team(p1, p2, p3, p4, p5);
		Team teamB = new Team(p6, p7, p8, p9, p10);
		Composition composition = new Composition(teamA, teamB);
		Score score = new Score(4,0);
		Game game = new Game(composition, score);

		CalculatorConfiguration calculatorConfiguration = new CalculatorConfiguration(teamA.getPlayers().size());
		calculatorConfiguration.setSplitPointsByTeam(false);
		RatingUpdatesCalculator calculator = new RatingUpdatesCalculator(calculatorConfiguration);
		calculator.setConfiguration(calculatorConfiguration);
		Map<String, Double> ratingUpdates = calculator.getRatingUpdates(game);
		System.out.println(ratingUpdates);
	}
}
