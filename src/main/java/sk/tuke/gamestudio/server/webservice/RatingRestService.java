package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingRestService {
	@Autowired
	private RatingService ratingService;
	
	@GetMapping("/{game}/{player}")
	public int getRating(@PathVariable String game, @PathVariable String player) {
		return ratingService.getRating(game, player);
	}
	
	@GetMapping("/avg/{game}")
	public int getAverageRatng(@PathVariable String game) {
		return ratingService.getAverageRating(game);
	}
	
	@PostMapping
	public void setRating(@RequestBody Rating rating) {
		ratingService.setRating(rating);
	}
}