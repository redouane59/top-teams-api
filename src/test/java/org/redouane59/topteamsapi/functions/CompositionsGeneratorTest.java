package org.redouane59.topteamsapi.functions;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.redouane59.topteamsapi.functions.compositionGenerator.CompositionGenerator;
import org.redouane59.topteamsapi.functions.compositionGenerator.GeneratorConfiguration;
import org.redouane59.topteamsapi.model.Player;
import org.redouane59.topteamsapi.model.PlayerPosition;
import org.redouane59.topteamsapi.model.composition.Composition;
import org.redouane59.topteamsapi.model.composition.CompositionType;

public class CompositionsGeneratorTest {

	private final int                  nbRandomTests = 10;
	private CompositionGenerator generator;
	private final Player               best1         = Player.builder().id("best1").ratingValue(90).build();
	private final Player               best2         = Player.builder().id("best2").ratingValue(89).build();
	private final Player               worst1        = Player.builder().id("worst1").ratingValue(20).build();
	private final Player               worst2        = Player.builder().id("worst2").ratingValue(21).build();
	private final Player               playerA       = Player.builder().id("playerA").ratingValue(30).build();
	private final Player               playerB       = Player.builder().id("playerB").ratingValue(50).build();
	private final Player               playerC       = Player.builder().id("playerC").ratingValue(50).build();
	private final Player               playerD       = Player.builder().id("playerD").ratingValue(50).build();
	private final Player               playerE       = Player.builder().id("playerE").ratingValue(50).build();
	private final Player               playerF       = Player.builder().id("playerF").ratingValue(55).build();

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
		players.add(Player.builder().id("Player1").ratingValue(40).build());
		players.add(Player.builder().id("Player2").ratingValue(40).build());
		players.add(Player.builder().id("Player3").ratingValue(50).build());
		players.add(Player.builder().id("Player4").ratingValue(50).build());
		players.add(Player.builder().id("Player5").ratingValue(60).build());
		players.add(Player.builder().id("Player6").ratingValue(60).build());
		players.add(Player.builder().id("Player7").ratingValue(70).build());
		players.add(Player.builder().id("Player8").ratingValue(70).build());
		players.add(Player.builder().id("Player9").ratingValue(80).build());
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

	private void assertTrue(final boolean playerOnTheSameTeam) {
	}

	@Test
	public void testSplittingBest() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(true);
		config.setSplitWorstPlayers(false);
		generator = new CompositionGenerator(config);
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers());
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
		}
	}

	@Test
	public void testSplittingWorst() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(true);
		generator = new CompositionGenerator(config);
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers());
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	public void testSplittingWorstAndBest() {
		generator = new CompositionGenerator(new GeneratorConfiguration());
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers());
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
		assertTrue(resultCompo.getTeamA().getPlayers().size()!=resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getRatingAverageDifference()<4);
		//assertTrue(resultCompo.getRatingDifference()<4);
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertTrue(resultCompo.getTeamA().getRatingAverage(maxPlayerOnField) ==
				(playersA.get(0).getRatingValue()+playersA.get(1).getRatingValue()+playersA.get(2).getRatingValue()
						+ playersA.get(3).getRatingValue() + playersA.get(4).getRatingValue())/playersA.size());
		assertTrue(resultCompo.getTeamB().getRatingAverage(maxPlayerOnField) ==
				(playersB.get(0).getRatingValue()+playersB.get(1).getRatingValue()+playersB.get(2).getRatingValue()
						+ playersB.get(3).getRatingValue())/(playersB.size()+1));
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
		assertTrue(resultCompo.getTeamA().getPlayers().size()==resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getTeamA().getRatingAverage() ==
				(playersA.get(0).getRatingValue()+playersA.get(1).getRatingValue()+playersA.get(2).getRatingValue()
						+ playersA.get(3).getRatingValue())/playersA.size());
		assertTrue(resultCompo.getTeamB().getRatingAverage() ==
				(playersB.get(0).getRatingValue()+playersB.get(1).getRatingValue()+playersB.get(2).getRatingValue()
						+ playersB.get(3).getRatingValue())/playersB.size());
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
		assertTrue(resultCompo.getTeamA().getPlayers().size()!=resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getRatingDifference()<4);
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertTrue(resultCompo.getTeamA().getRatingAverage() ==
				(playersA.get(0).getRatingValue()+playersA.get(1).getRatingValue()+playersA.get(2).getRatingValue()
						+ playersA.get(3).getRatingValue() + playersA.get(4).getRatingValue())/playersA.size());
		assertTrue(resultCompo.getTeamB().getRatingAverage() ==
				(playersB.get(0).getRatingValue()+playersB.get(1).getRatingValue()+playersB.get(2).getRatingValue()
						+ playersB.get(3).getRatingValue())/(playersB.size()));
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(generator.getClonedPlayers(players));
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(generator.getClonedPlayers(players));
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

		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(generator.getClonedPlayers(players));
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(generator.getClonedPlayers(players));
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(generator.getClonedPlayers(players));
			assertEquals(compo.getTeamA().getPlayersByPosition(PlayerPosition.DEF).size(),
					compo.getTeamB().getPlayersByPosition(PlayerPosition.DEF).size());
			assertEquals(compo.getTeamA().getPlayersByPosition(PlayerPosition.ATT).size(),
					compo.getTeamB().getPlayersByPosition(PlayerPosition.ATT).size());
		}
	}

	private boolean playerOnTheSameTeam(Composition compo, Player p1, Player p2){
		return (compo.getTeamA().getPlayers().contains(p1) && compo.getTeamA().getPlayers().contains(p2))
				|| (compo.getTeamB().getPlayers().contains(p1) && compo.getTeamB().getPlayers().contains(p2));
	}
}
