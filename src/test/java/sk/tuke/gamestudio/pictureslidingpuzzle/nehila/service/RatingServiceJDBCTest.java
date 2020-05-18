package sk.tuke.gamestudio.pictureslidingpuzzle.nehila.service;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.jdbc.RatingServiceJDBC;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class RatingServiceJDBCTest {
	private final String GAME = "puzzleRating";
	Calendar cal = Calendar.getInstance();
	private RatingService service = new RatingServiceJDBC();
	
	@Test
	public void addRating() throws RatingException {
		String tmpGame = GAME + "Add";
		int newRating = 5;
		
		Rating rating = new Rating("Zuzka", tmpGame, newRating, cal.getTime());
		service.setRating(rating);
		
		Integer rate = service.getRating(tmpGame, "Zuzka");
		
		assertEquals(newRating, (long) rate);
	}
	
	@Test
	public void changeRating() throws RatingException {
		String tmpGame = GAME + "Change";
		int newRating = 5;
		int afterChangeRating = 2;
		
		Rating rating = new Rating("Zuzka", tmpGame, newRating, cal.getTime());
		service.setRating(rating);
		
		// change rating again to check if update on conflict works
		rating = new Rating("Zuzka", tmpGame, afterChangeRating, cal.getTime());
		service.setRating(rating);
		
		Integer rate = service.getRating(tmpGame, "Zuzka");
		
		assertEquals(afterChangeRating, (long) rate);
	}
	
	@Test
	public void averageRating() throws RatingException {
		String tmpGame = GAME + "Avg";
		int[] ratings = {1, 2, 3, 4, 5};
		int avgRatingExpected = 0;
		for(int i = 0; i < ratings.length; i++) {
			avgRatingExpected += ratings[i];
		}
		avgRatingExpected /= ratings.length;
		
		for(int i = 0; i < ratings.length; i++) {
			Rating rating = new Rating("Zuzka" + i, tmpGame, ratings[i], cal.getTime());
			service.setRating(rating);
		}
		
		int averageRating = service.getAverageRating(tmpGame);
		assertEquals(avgRatingExpected, averageRating);
	}
}
