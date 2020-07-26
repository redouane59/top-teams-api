package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TeamTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void testRatingValue() {
		Player playerA = Player.builder().id("playerA").rating(30).build();
		Player playerB = Player.builder().id("playerB").rating(50).build();
		Team team = new Team();
		team.getPlayers().add(playerA);
		assertEquals(team.getRatingAverage(), playerA.getRating());
		assertEquals(team.getRatingSum(), (playerA.getRating()));
		team.getPlayers().add(playerB);
		assertEquals(team.getRatingAverage(), (playerA.getRating() + playerB.getRating()) / 2);
		assertEquals(team.getRatingSum(), (playerA.getRating() + playerB.getRating()));
	}

	@Test
	public void testRatingValueWithNbPlayersOnField() {
		List<Player> players = new ArrayList<>();
		Player p1 = Player.builder().id("a").rating(30).position(PlayerPosition.GK).build();
		Player p2 = new Player();
		p2.setRating(40);
		Player p3 = new Player();
		p3.setRating(50);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		Team team = Team.builder().players(players).build();
		double ratingSum = p1.getRating() + p2.getRating() + p3.getRating();
		assertEquals(team.getRatingAverage(3), ratingSum / 3);
		assertEquals(team.getRatingSum(3), ratingSum);
		assertEquals(team.getRatingAverage(2), ratingSum / 3);
		assertEquals(team.getRatingSum(2), ratingSum * 2 / 3);
		assertEquals(team.getRatingAverage(4), ratingSum / 4);
		assertEquals(team.getRatingSum(4), ratingSum);
		assertEquals(team.getRatingAverage(2), ratingSum / 3);
		assertEquals(team.getRatingSum(2), ratingSum * 2 / 3);
		assertEquals(team.getRatingAverage(3), ratingSum / 3);
		assertEquals(team.getRatingSum(3), ratingSum);
	}

	@Test
	public void testEqualOk(){
		Player p1 = Player.builder().id("p1").rating(5).build();
		Player p2 = Player.builder().id("p2").rating(5).build();
		Player p3 = Player.builder().id("p3").rating(5).build();
		Player p4 = Player.builder().id("p4").rating(5).build();
		Player p5 = Player.builder().id("p5").rating(5).build();
		Team teamA = Team.builder().players(List.of(p1,p2,p3,p4,p5)).build();
		Team teamB = Team.builder().players(List.of(p5,p4,p3,p2,p1)).build();
		assertEquals(teamA, teamB);
		assertEquals(teamB, teamA);
	}

	@Test
	public void testEqualKO(){
		Player p1 = Player.builder().id("p1").rating(5).build();
		Player p2 = Player.builder().id("p2").rating(5).build();
		Player p3 = Player.builder().id("p3").rating(5).build();
		Player p4 = Player.builder().id("p4").rating(5).build();
		Player p5 = Player.builder().id("p5").rating(5).build();
		Player p6 = Player.builder().id("p6").rating(5).build();

		Team teamA = Team.builder().players(List.of(p1,p2,p3,p4,p5)).build();
		Team teamB = Team.builder().players(List.of(p5,p4,p3,p2,p6)).build();
		assertNotEquals(teamA, teamB);
		assertNotEquals(teamB, teamA);
	}

	@Test
	public void testDeserialization() throws JsonProcessingException {
		String teamJson = "{\"players\":[{\"id\":\"player3\",\"position\":\"GK\",\"rating\":59.0,\"nb_games_played\":0},{\"id\":\"player10\",\"rating\":88.0,\"nb_games_played\":0},{\"id\":\"player8\",\"rating\":45.0,\"nb_games_played\":0}]}";
		Team team = MAPPER.readValue(teamJson, Team.class);
		assertEquals(3, team.getPlayers().size());
		assertEquals(64, team.getRatingAverage());
		assertEquals(192, team.getRatingSum());
	}




}
