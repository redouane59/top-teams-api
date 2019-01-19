import com.amazonaws.compositionGenerator.CompositionGenerator;
import com.amazonaws.model.*;
import com.amazonaws.compositionGenerator.GeneratorConfiguration;
import com.amazonaws.model.Composition;
import com.amazonaws.model.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompositionsGeneratorTest {

	private int nbRandomTests = 3;
	private CompositionGenerator generator = new CompositionGenerator();
	private Player best1 = new Player("best1",90);
	private Player best2 = new Player("best2",89);
	private Player worst1 = new Player("worst1",20);
	private Player worst2 = new Player("worst2",21);
	private Player playerA = new Player("playerA",30);
	private Player playerB = new Player("playerB",50);
	private Player playerC = new Player("playerC",50);
	private Player playerD = new Player("playerD",50);
	private Player playerE = new Player("playerE",50);
	private Player playerF = new Player("playerF",55);

	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		players.add(best1);
		players.add(best2);
		players.add(worst1);
		players.add(worst2);
		players.add(playerA);
		players.add(playerB);
		players.add(playerC);
		players.add(playerD);
		players.add(playerE);
		players.add(playerF);
		return players;
	}

	public List<Player> getOddPlayers(){
		List<Player> players = new ArrayList<>();
		players.add(new Player("Player1",40));
		players.add(new Player("Player2",40));
		players.add(new Player("Player3",50));
		players.add(new Player("Player4",50));
		players.add(new Player("Player5",60));
		players.add(new Player("Player6",60));
		players.add(new Player("Player7",70));
		players.add(new Player("Player8",70));
		players.add(new Player("Player9",80));
		return players;
	}

	@Test
	public void testWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers(), config);
		assertTrue(playerOnTheSameTeam(resultCompo, best1, best2));
		assertTrue(resultCompo.getRatingAverageDifference()<4);
	}

	@Test
	public void testSplittingBest() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(true);
		config.setSplitWorstPlayers(false);
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers(), config);
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
		}
	}

	@Test
	public void testSplittingWorst() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(true);
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers(), config);
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	public void testSplittingWorstAndBest() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(true);
		config.setSplitWorstPlayers(true);
		Composition randomCompo;
		for(int i=0;i<nbRandomTests;i++){
			randomCompo = (Composition)generator.buildRandomComposition(this.getPlayers(), config);
			assertFalse(playerOnTheSameTeam(randomCompo, best1, best2));
			assertFalse(playerOnTheSameTeam(randomCompo, worst1, worst2));
		}
	}

	@Test
	public void testCompoWithoutSplitting() {
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getPlayers(), config);
		assertTrue(resultCompo.getRatingAverageDifference()<1.1);
	}

	@Test
	public void testFreeOddComposition(){
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		config.setGameType(GameType.FREE);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers(), config);
		assertTrue(resultCompo.getTeamA().getPlayers().size()!=resultCompo.getTeamB().getPlayers().size());
		assertTrue(resultCompo.getRatingAverageDifference()<4);
		//assertTrue(resultCompo.getRatingDifference()<4);
		List<Player> playersA = resultCompo.getTeamA().getPlayers();
		List<Player> playersB = resultCompo.getTeamB().getPlayers();
		assertTrue(resultCompo.getTeamA().getRatingAverage() ==
				(playersA.get(0).getRatingValue()+playersA.get(1).getRatingValue()+playersA.get(2).getRatingValue()
						+ playersA.get(3).getRatingValue() + playersA.get(4).getRatingValue())/playersA.size());
		assertTrue(resultCompo.getTeamB().getRatingAverage() ==
				(playersB.get(0).getRatingValue()+playersB.get(1).getRatingValue()+playersB.get(2).getRatingValue()
						+ playersB.get(3).getRatingValue())/(playersB.size()+1));
	}

	@Test
	public void testSameNbTeamPlayersOddComposition(){
		GeneratorConfiguration config = new GeneratorConfiguration();
		config.setSplitBestPlayers(false);
		config.setSplitWorstPlayers(false);
		config.setGameType(GameType.SAME_NB_OF_PLAYERS_PER_TEAM);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers(), config);
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
		config.setGameType(GameType.SAME_NB_OF_PLAYERS_ON_FIELD);
		Composition resultCompo = (Composition)generator.getBestComposition(this.getOddPlayers(), config);
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(players, config);
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
		Composition compo;
		for(int i=0;i<nbRandomTests;i++){
			compo = (Composition)generator.buildRandomComposition(players, config);
			assertFalse(playerOnTheSameTeam(compo, p1,p2));
			assertFalse(playerOnTheSameTeam(compo, best1,best2));
			assertFalse(playerOnTheSameTeam(compo, worst1,worst2));
		}
	}

	private boolean playerOnTheSameTeam(Composition compo, Player p1, Player p2){
		return (compo.getTeamA().getPlayers().contains(p1) && compo.getTeamA().getPlayers().contains(p2))
				|| (compo.getTeamB().getPlayers().contains(p1) && compo.getTeamB().getPlayers().contains(p2));
	}
}
