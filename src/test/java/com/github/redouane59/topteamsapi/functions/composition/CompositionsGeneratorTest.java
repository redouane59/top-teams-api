package com.github.redouane59.topteamsapi.functions.composition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.topteamsapi.model.Team;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.github.redouane59.topteamsapi.model.Player;
import com.github.redouane59.topteamsapi.model.PlayerPosition;
import com.github.redouane59.topteamsapi.model.composition.Composition;
import com.github.redouane59.topteamsapi.model.composition.CompositionType;

public class CompositionsGeneratorTest {

	private final int                  nbRandomTests = 10;
	private CompositionGenerator generator;
	private final Player               best1         = Player.builder().id("best1").rating(90).build();
	private final Player               best2         = Player.builder().id("best2").rating(89).build();
	private final Player               worst1        = Player.builder().id("worst1").rating(20).build();
	private final Player               worst2        = Player.builder().id("worst2").rating(21).build();
	private final Player               playerA       = Player.builder().id("playerA").rating(30).build();
	private final Player               playerB       = Player.builder().id("playerB").rating(50).build();
	private final Player               playerC       = Player.builder().id("playerC").rating(50).build();
	private final Player               playerD       = Player.builder().id("playerD").rating(50).build();
	private final Player               playerE       = Player.builder().id("playerE").rating(50).build();
	private final Player               playerF       = Player.builder().id("playerF").rating(55).build();

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
		players.add(Player.builder().id("Player1").rating(40).build());
		players.add(Player.builder().id("Player2").rating(40).build());
		players.add(Player.builder().id("Player3").rating(50).build());
		players.add(Player.builder().id("Player4").rating(50).build());
		players.add(Player.builder().id("Player5").rating(60).build());
		players.add(Player.builder().id("Player6").rating(60).build());
		players.add(Player.builder().id("Player7").rating(70).build());
		players.add(Player.builder().id("Player8").rating(70).build());
		players.add(Player.builder().id("Player9").rating(80).build());
		return players;
	}

	@Test
	public void testWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers());
		System.out.println(resultCompo);
		assertTrue(playerOnTheSameTeam(resultCompo, best1, best2));
		assertTrue(resultCompo.getRatingAverageDifference()<4);
	}


	@Test
	public void testSplittingBest() {
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
	public void testSplittingWorst() {
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
	public void testSplittingWorstAndBest() {
		generator = new CompositionGenerator(new GeneratorConfiguration());
		for(int i=0;i<nbRandomTests;i++){
			Composition randomCompo = Composition.builder().availablePlayers(this.getPlayers()).build();
			randomCompo = (Composition)randomCompo.generateRandomComposition(new GeneratorConfiguration());
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	public void testCompoWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers());
		assertTrue(resultCompo.getRatingAverageDifference()<1.1);
	}

	@Test
	public void testFreeOddComposition(){
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
	public void testSameNbTeamPlayersOddComposition(){
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
	public void testSameNbFieldPlayersOddComposition(){
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
	public void testSplitting2GK() {
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
	public void testSplitting2GKAndBestAndWorst() {
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
	public void testSplittingAllByPosition() {
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
	public void testSplittingAllByPositionWitOddNumber() {
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
	public void testBestCompoSplittingAllByPosition() {
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
	public void testGenerateRandomTeam(){
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
	public void testGenerateRandomTeamFromOtherTeam(){
		generator = new CompositionGenerator(new GeneratorConfiguration());
		Team teamA = Team.builder().players(List.of(playerA)).build();
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
	public void testGenerateCompositionFromOtherComposition(){
		Team teamA = Team.builder().players(List.of(playerA)).build();
		Team teamB = Team.builder().players(List.of(playerB)).build();
		Composition initCompo = Composition.builder().teamA(teamA).teamB(teamB)
																			 .availablePlayers(List.of(playerC, playerD, playerE, playerF))
																			 .build();
		GeneratorConfiguration configuration = GeneratorConfiguration.builder()
																																 .splitBestPlayers(false)
																																 .splitDefenders(false)
																																 .splitStrikers(false)
																																 .splitGoalKeepers(false)
																																 .splitBestPlayers(false)
																																 .splitWorstPlayers(false)
																																 .build();
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
