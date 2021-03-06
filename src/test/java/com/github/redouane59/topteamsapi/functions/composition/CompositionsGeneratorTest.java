package com.github.redouane59.topteamsapi.functions.composition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.github.redouane59.topteamsapi.model.Team;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;

@Log
class CompositionsGeneratorTest {

	private final int                  nbRandomTests = 10;
	private CompositionGenerator generator;
	private final Player               best1         = new Player().withId("best1").withRating(90);
	private final Player               best2         = new Player().withId("best2").withRating(89);
	private final Player               worst1        = new Player().withId("worst1").withRating(20);
	private final Player               worst2        = new Player().withId("worst2").withRating(21);
	private final Player               playerA       = new Player().withId("playerA").withRating(30);
	private final Player               playerB       = new Player().withId("playerB").withRating(50);
	private final Player               playerC       = new Player().withId("playerC").withRating(50);
	private final Player               playerD       = new Player().withId("playerD").withRating(50);
	private final Player               playerE       = new Player().withId("playerE").withRating(50);
	private final Player               playerF       = new Player().withId("playerF").withRating(55);

	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		players.add(new Player(best1));
		players.add(new Player(best2));
		players.add(new Player(worst1));
		players.add(new Player(worst2));
		players.add(new Player(playerA));
		players.add(new Player(playerB));
		players.add(new Player(playerC));
		players.add(new Player(playerD));
		players.add(new Player(playerE));
		players.add(new Player(playerF));
		return players;
	}

	public List<Player> getOddPlayers(){
		List<Player> players = new ArrayList<>();
		players.add(new Player().withId("Player1").withRating(40));
		players.add(new Player().withId("Player2").withRating(40));
		players.add(new Player().withId("Player3").withRating(50));
		players.add(new Player().withId("Player4").withRating(50));
		players.add(new Player().withId("Player5").withRating(60));
		players.add(new Player().withId("Player6").withRating(60));
		players.add(new Player().withId("Player7").withRating(70));
		players.add(new Player().withId("Player8").withRating(70));
		players.add(new Player().withId("Player9").withRating(80));
		return players;
	}

	@Test
	void testWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers());
		log.fine(resultCompo.toString());
		assertTrue(playerOnTheSameTeam(resultCompo, best1, best2));
		assertTrue(resultCompo.getRatingAverageDifference()<4);
	}


	@Test
	void testSplittingBest() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(true);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition randomCompo = Composition.builder().availablePlayers(this.getPlayers()).build();
			randomCompo = (Composition)randomCompo.generateRandomComposition(config);
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
		}
	}

	@Test
	void testSplittingWorst() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(true);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition randomCompo = Composition.builder().availablePlayers(this.getPlayers()).build();
			randomCompo = (Composition)randomCompo.generateRandomComposition(config);
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	void testSplittingWorstAndBest() {
		generator = new CompositionGenerator(new GeneratorConfiguration());
		for(int i=0;i<nbRandomTests;i++){
			Composition randomCompo = Composition.builder().availablePlayers(this.getPlayers()).build();
			randomCompo = (Composition)randomCompo.generateRandomComposition(new GeneratorConfiguration());
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	void testCompoWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers());
		assertTrue(resultCompo.getRatingAverageDifference()<1.1);
	}

	@Test
	void testFreeOddComposition(){
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		config.setCompositionType(CompositionType.ODD);
		int maxPlayerOnField = 5;
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers());
		assertNotEquals(resultCompo.getTeamA().getPlayers().size(),resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getRatingAverageDifference()<4);
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertEquals(resultCompo.getTeamA().getRatingAverage(maxPlayerOnField),
								 (playersA.get(0).getRating() + playersA.get(1).getRating() + playersA.get(2).getRating()
									+ playersA.get(3).getRating() + playersA.get(4).getRating()) / playersA.size());
		assertEquals(resultCompo.getTeamB().getRatingAverage(maxPlayerOnField),
								 (playersB.get(0).getRating() + playersB.get(1).getRating() + playersB.get(2).getRating()
									+ playersB.get(3).getRating()) / (playersB.size() + 1));
	}

	@Test
	void testSameNbTeamPlayersOddComposition(){
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		config.setCompositionType(CompositionType.REGULAR);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers());
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertEquals(resultCompo.getTeamA().getPlayers().size(), resultCompo.getTeamB().getPlayers().size());
		assertEquals(resultCompo.getTeamA().getRatingAverage(), (playersA.get(0).getRating() + playersA.get(1).getRating() + playersA.get(2).getRating()
																														 + playersA.get(3).getRating()) / playersA.size());
		assertEquals(resultCompo.getTeamB().getRatingAverage(), (playersB.get(0).getRating() + playersB.get(1).getRating() + playersB.get(2).getRating()
																														 + playersB.get(3).getRating()) / playersB.size());
		assertTrue(resultCompo.getRatingDifference()<4);
	}

	@Test
	void testSameNbFieldPlayersOddComposition(){
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		config.setCompositionType(CompositionType.SUBSTITUTION);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers());
		assertNotEquals(resultCompo.getTeamA().getPlayers().size(),resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getRatingDifference()<4);
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertEquals(resultCompo.getTeamA().getRatingAverage(), (playersA.get(0).getRating() + playersA.get(1).getRating() + playersA.get(2).getRating()
																														 + playersA.get(3).getRating() + playersA.get(4).getRating()) / playersA.size());
		assertEquals(resultCompo.getTeamB().getRatingAverage(), (playersB.get(0).getRating() + playersB.get(1).getRating() + playersB.get(2).getRating()
																														 + playersB.get(3).getRating()) / (playersB.size()));
	}

	@Test
	void testSplitting2GK() {
		List<Player> players = getPlayers();
		Player p1 = players.get(0);
		p1.setPosition(PlayerPosition.GK);
		Player p2 = players.get(1);
		p2.setPosition(PlayerPosition.GK);
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitGoalKeepers(true);
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition compo = (Composition)Composition.builder().availablePlayers(generator.getClonedPlayers(players)).build()
																									.generateRandomComposition(config);
			assertFalse(playerOnTheSameTeam(compo, p1,p2));
		}
	}

	@Test
	void testSplitting2GKAndBestAndWorst() {
		List<Player> players = getPlayers();
		Player p1 = players.get(0);
		p1.setPosition(PlayerPosition.GK);
		Player p2 = players.get(1);
		p2.setPosition(PlayerPosition.GK);
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitGoalKeepers(true);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition compo = (Composition)Composition.builder().availablePlayers(generator.getClonedPlayers(players)).build()
																									.generateRandomComposition(config);
			assertFalse(playerOnTheSameTeam(compo, p1,p2));
			assertFalse(playerOnTheSameTeam(compo, best1,best2));
			assertFalse(playerOnTheSameTeam(compo, worst1,worst2));
		}
	}

	@Test
	void testSplittingAllByPosition() {
		List<Player> players = getPlayers();
		Player p1 = players.get(0);
		p1.setPosition(PlayerPosition.DEF);
		Player p2 = players.get(1);
		p2.setPosition(PlayerPosition.DEF);
		Player p3 = players.get(2);
		p3.setPosition(PlayerPosition.DEF);
		Player p4 = players.get(3);
		p4.setPosition(PlayerPosition.DEF);
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitDefenders(true);
		generator = new CompositionGenerator(config);

		for(int i=0;i<nbRandomTests;i++){
			Composition compo = (Composition)Composition.builder().availablePlayers(generator.getClonedPlayers(players)).build()
					.generateRandomComposition(config);
			assertEquals(compo.getTeamA().getPlayersByPosition(PlayerPosition.DEF).size(),
									 compo.getTeamB().getPlayersByPosition(PlayerPosition.DEF).size());
		}
	}

	@Test
	void testSplittingAllByPositionWitOddNumber() {
		List<Player> players = getPlayers();
		Player p1 = players.get(0);
		p1.setPosition(PlayerPosition.DEF);
		Player p2 = players.get(1);
		p2.setPosition(PlayerPosition.DEF);
		Player p3 = players.get(2);
		p3.setPosition(PlayerPosition.DEF);
		Player p4 = players.get(3);
		p4.setPosition(PlayerPosition.DEF);
		Player p5 = players.get(4);
		p5.setPosition(PlayerPosition.DEF);
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitDefenders(true);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition compo = (Composition)Composition.builder().availablePlayers(generator.getClonedPlayers(players)).build()
																									.generateRandomComposition(config);
			assertEquals(1, Math.abs(compo.getTeamA().getPlayersByPosition(PlayerPosition.DEF).size()-
															 compo.getTeamB().getPlayersByPosition(PlayerPosition.DEF).size()));
		}
	}

	@Test
	void testBestCompoSplittingAllByPosition() {
		List<Player> players = getPlayers();
		Player p1 = players.get(0);
		p1.setPosition(PlayerPosition.DEF);
		Player p2 = players.get(1);
		p2.setPosition(PlayerPosition.DEF);
		Player p3 = players.get(2);
		p3.setPosition(PlayerPosition.DEF);
		Player p4 = players.get(3);
		p4.setPosition(PlayerPosition.DEF);
		Player p5 = players.get(4);
		p5.setPosition(PlayerPosition.ATT);
		Player p6 = players.get(5);
		p6.setPosition(PlayerPosition.ATT);
		Player p7 = players.get(6);
		p7.setPosition(PlayerPosition.ATT);
		Player p8 = players.get(7);
		p8.setPosition(PlayerPosition.ATT);
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitDefenders(true);
		generator = new CompositionGenerator(config);
		for(int i=0;i<nbRandomTests;i++){
			Composition compo = (Composition)Composition.builder().availablePlayers(generator.getClonedPlayers(players)).build()
																									.generateRandomComposition(config);
			assertEquals(compo.getTeamA().getPlayersByPosition(PlayerPosition.DEF).size(),
									 compo.getTeamB().getPlayersByPosition(PlayerPosition.DEF).size());
			assertEquals(compo.getTeamA().getPlayersByPosition(PlayerPosition.ATT).size(),
									 compo.getTeamB().getPlayersByPosition(PlayerPosition.ATT).size());
		}
	}
/*
	@Test
	void testGenerateRandomTeam(){
		generator = new CompositionGenerator(new GeneratorConfiguration());
		for(int i=0;i<nbRandomTests;i++) {
			List<Player> availablePlayers = List.of(playerC, playerD, playerE);
			Team team = generator.buildRandomTeam(availablePlayers,3);
			assertEquals(3,team.getPlayers().size());
			assertTrue(team.isPlayerOnTeam(playerC.getId()));
			assertTrue(team.isPlayerOnTeam(playerD.getId()));
			assertTrue(team.isPlayerOnTeam(playerE.getId()));
		}
	}

	@Test
	void testGenerateRandomTeamFromOtherTeam(){
		generator = new CompositionGenerator(new GeneratorConfiguration());
		Team teamA = new Team().withPlayers(List.of(playerA)).build();
		for(int i=0;i<nbRandomTests;i++) {
			List<Player> availablePlayers = List.of(playerB, playerC);
			Team team = generator.buildRandomTeam(teamA, availablePlayers,3);
			assertEquals(3,team.getPlayers().size());
			assertTrue(team.isPlayerOnTeam(playerA.getId()));
			assertTrue(team.isPlayerOnTeam(playerB.getId()));
			assertTrue(team.isPlayerOnTeam(playerC.getId()));
		}
	} */

	@Test
	void testGenerateCompositionFromOtherComposition(){
		Team teamA = new Team().withPlayers(List.of(playerA));
		Team teamB = new Team().withPlayers(List.of(playerB));
		Composition initCompo = Composition.builder().teamA(teamA).teamB(teamB)
																			 .availablePlayers(List.of(playerC, playerD, playerE, playerF))
																			 .build();
		GeneratorConfiguration configuration = new GeneratorConfiguration()
																																 .withSplitBestPlayers(false)
																																 .withSplitDefenders(false)
																																 .withSplitStrikers(false)
																																 .withSplitGoalKeepers(false)
																																 .withSplitBestPlayers(false)
																																 .withSplitWorstPlayers(false);
		generator = new CompositionGenerator(configuration);
		for(int i=0;i<nbRandomTests;i++) {
			Composition resultCompo = (Composition)initCompo.generateRandomComposition(configuration);
			assertFalse(this.playerOnTheSameTeam(resultCompo, playerA, playerB));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerA.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerA.getId()));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerB.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerB.getId()));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerC.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerC.getId()));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerD.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerD.getId()));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerE.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerE.getId()));
			assertTrue(resultCompo.getTeamA().isPlayerOnTeam(playerF.getId()) || resultCompo.getTeamB().isPlayerOnTeam(playerF.getId()));
			assertEquals(3,resultCompo.getTeamA().getPlayers().size());
			assertEquals(3,resultCompo.getTeamB().getPlayers().size());
		}
	}

	private boolean playerOnTheSameTeam(Composition compo, Player p1, Player p2){
		return (compo.getTeamA().getPlayers().contains(p1) && compo.getTeamA().getPlayers().contains(p2))
					 || (compo.getTeamB().getPlayers().contains(p1) && compo.getTeamB().getPlayers().contains(p2));
	}
}
