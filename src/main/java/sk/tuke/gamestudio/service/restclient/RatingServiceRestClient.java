package sk.tuke.gamestudio.service.restclient;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import java.util.Objects;

public class RatingServiceRestClient implements RatingService {
	
	private RestManager restManager = RestManager.INSTANCE;
	
	private String postfix = "rating";
	
	private final String URL = restManager.getApi() + postfix;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void setRating(Rating rating) throws RatingException {
		restTemplate.postForEntity(URL, rating, Rating.class);
	}
	
	@Override
	public int getAverageRating(String game) throws RatingException {
		return Objects.requireNonNull(restTemplate.getForEntity(URL + "/avg/" + game, int.class).getBody());
	}
	
	@Override
	public int getRating(String game, String player) throws RatingException {
		return Objects.requireNonNull(restTemplate.getForEntity(URL + "/" + game + "/" + player, int.class).getBody());
	}
}