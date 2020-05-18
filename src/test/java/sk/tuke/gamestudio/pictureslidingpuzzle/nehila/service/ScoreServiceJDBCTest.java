package sk.tuke.gamestudio.pictureslidingpuzzle.nehila.service;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.jdbc.ScoreServiceJDBC;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScoreServiceJDBCTest {
	private final String GAME = "puzzleScore";
	private ScoreService service = new ScoreServiceJDBC();
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	// bestScore is low, because it means number of actions
	// e.g less moves == better player
	private int bestScore = 5;
	
	@Test
	public void addScore() {
		String tmpGame = GAME + "Add";
		
		Score score = new Score(tmpGame, "Zuzka", bestScore, cal.getTime());
		service.addScore(score);
		
		List<Score> scores = service.getBestScores(tmpGame);
		
		Calendar newCal = Calendar.getInstance();
		
		int lastIndex = scores.size() - 1;
		assertEquals("Zuzka", scores.get(lastIndex).getPlayer());
		assertEquals(bestScore, scores.get(lastIndex).getPoints());
		assertEquals(tmpGame, scores.get(lastIndex).getGame());
		assertEquals(format.format(newCal.getTime()), format.format(scores.get(lastIndex).getPlayedOn()));
	}
	
	@Test
	public void bestScores() {
		String tmpGame = GAME + "Best";
		
		for(int i = 0; i < 10; i++) {
			Score score = new Score(tmpGame, "Zuzka" + i, bestScore + i, cal.getTime());
			service.addScore(score);
		}
		
		List<Score> scores = service.getBestScores(tmpGame);
		
		assertTrue(scores.size() >= 10);
	}
}
