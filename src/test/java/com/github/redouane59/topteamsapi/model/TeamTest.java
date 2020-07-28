package com.github.redouane59.topteamsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TeamTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void testRatingValue() {
		Player playerA = new Player().withId("playerA").withRating(30);
		Player playerB = new Player().withId("playerB").withRating(50);
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
		Player p1 = new Player().withId("a").withRating(30).withPosition(PlayerPosition.GK);
		Player p2 = new Player().withRating(40);
		Player p3 = new Player().withRating(50);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		Team team = new Team().withPlayers(players);
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
		Player p1 = new Player().withId("p1").withRating(5);
		Player p2 = new Player().withId("p2").withRating(5);
		Player p3 = new Player().withId("p3").withRating(5);
		Player p4 = new Player().withId("p4").withRating(5);
		Player p5 = new Player().withId("p5").withRating(5);
		Team teamA = new Team().withPlayers(List.of(p1,p2,p3,p4,p5));
		Team teamB = new Team().withPlayers(List.of(p5,p4,p3,p2,p1));
		assertEquals(teamA, teamB);
		assertEquals(teamB, teamA);
	}

	@Test
	public void testEqualKO(){
		Player p1 = new Player().withId("p1").withRating(5);
		Player p2 = new Player().withId("p2").withRating(5);
		Player p3 = new Player().withId("p3").withRating(5);
		Player p4 = new Player().withId("p4").withRating(5);
		Player p5 = new Player().withId("p5").withRating(5);
		Player p6 = new Player().withId("p6").withRating(5);

		Team teamA = new Team().withPlayers(List.of(p1,p2,p3,p4,p5));
		Team teamB = new Team().withPlayers(List.of(p5,p4,p3,p2,p6));
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
