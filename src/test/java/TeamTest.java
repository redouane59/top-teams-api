import com.amazonaws.model.Player;
import com.amazonaws.model.Team;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TeamTest {

	private Player best1 = new Player("Best1",90);
	private Player best2 = new Player("Best1",89);
	private Player worst1 = new Player("Best1",20);
	private Player worst2 = new Player("Best1",21);
	private Player playerA = new Player("Best1",30);
	private Player playerB = new Player("Best1",50);
	private Player playerC = new Player("Best1",50);
	private Player playerD = new Player("Best1",50);
	private Player playerE = new Player("Best1",50);
	private Player playerF = new Player("Best1",55);

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


	@Test
	public void testRatingValue() {
		Team team = new Team();
		team.addPlayer(playerA);
		assertTrue(team.getRatingAverage()==playerA.getRatingValue());
		assertTrue(team.getRatingSum()==(playerA.getRatingValue()));
		team.addPlayer(playerB);
		assertTrue(team.getRatingAverage()==(playerA.getRatingValue()+playerB.getRatingValue())/2);
		assertTrue(team.getRatingSum()==(playerA.getRatingValue()+playerB.getRatingValue()));
	}

	@Test
	public void testRatingValueWithNbPlayersOnField() {
		Team team = new Team();
		Player p1 = new Player("a",30);
		Player p2 = new Player("a",40);
		Player p3 = new Player("a",50);
		team.addPlayer(p1);
		team.addPlayer(p2);
		team.addPlayer(p3);
		double ratingSum = p1.getRatingValue()+p2.getRatingValue()+p3.getRatingValue();
		team.setNbPlayersOnField(3);
		assertTrue(team.getRatingAverage() == ratingSum/3);
		assertTrue(team.getRatingSum() == ratingSum);
		team.setNbPlayersOnField(2);
		assertTrue(team.getRatingAverage() == ratingSum/3);
		assertTrue(team.getRatingSum() == ratingSum*2/3);
		team.setNbPlayersOnField(4);
		assertTrue(team.getRatingAverage() == ratingSum/4);
		assertTrue(team.getRatingSum() == ratingSum);
		team.setNbPlayersOnField(2);
		assertTrue(team.getRatingAverage() == ratingSum/3);
		assertTrue(team.getRatingSum() == ratingSum*2/3);
		team.setNbPlayersOnField(3);
		assertTrue(team.getRatingAverage() == ratingSum/3);
		assertTrue(team.getRatingSum() == ratingSum);
	}

	@Test
	public void testEqualOk(){
		Player p1 = new Player("p1", 5);
		Player p2 = new Player("p2", 5);
		Player p3 = new Player("p3", 5);
		Player p4 = new Player("p4", 5);
		Player p5 = new Player("p5", 5);

		Team teamA = new Team(p1,p2,p3,p4,p5);
		Team teamB = new Team(p5,p4,p3,p2,p1);

		assertTrue(teamA.equals(teamB));
		assertTrue(teamB.equals(teamA));
	}

	@Test
	public void testEqualKO(){
		Player p1 = new Player("p1", 5);
		Player p2 = new Player("p2", 5);
		Player p3 = new Player("p3", 5);
		Player p4 = new Player("p4", 5);
		Player p5 = new Player("p5", 5);
		Player p6 = new Player("p6", 5);

		Team teamA = new Team(p1,p2,p3,p4,p5);
		Team teamB = new Team(p1,p2,p3,p4,p6);
		assertFalse(teamA.equals(teamB));
		assertFalse(teamB.equals(teamA));
	}




}
