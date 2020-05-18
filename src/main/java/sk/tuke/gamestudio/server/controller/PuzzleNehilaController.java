package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.puzzle.nehila.core.Field;
import sk.tuke.gamestudio.game.puzzle.nehila.core.Fragment;
import sk.tuke.gamestudio.game.puzzle.nehila.core.GameState;
import sk.tuke.gamestudio.game.puzzle.nehila.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/puzzle-nehila")
public class PuzzleNehilaController {
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private UserController userController;
	
	private Field field;
	
	private String imgSource;
	private String imgUrl;
	
	public PuzzleNehilaController() {
		this.imgSource = "default.jpg";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String puzzle_nehila(Model model) {
		if(field == null) {
			newGame(3);
		}
		
		
		prepareModel(model);
		return "puzzle-nehila";
	}
	
	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public String move(int row, int column, Model model) {
		if(field == null) {
			newGame(3);
		}
		
		if(field.getGameState() == GameState.PLAYING) {
			field.moveFragment(row, column);
			if(userController.isLogged() && field.getGameState() == GameState.SOLVED) {
				scoreService.addScore(new Score("puzzle-nehila", userController.getLoggedUser(), field.calculateScore(), new Date()));
			}
		}
		
		prepareModel(model);
		
		// page is sneaky fragment that covers whole page so we can use AJAX on front-end to update view without refresh
		// this kind of refresh requires less data to be sent over network (no resources are sent)
		// doesn't work as intended on Chrome, works everywhere else
		return "puzzle-nehila :: page";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newGame(int fieldSize, String img, String imgUrl, Model model) {
		newGame(fieldSize);
		
		if(imgUrl == null || imgUrl.isEmpty()) {
			this.imgSource = img;
			this.imgUrl = null;
		} else {
			this.imgSource = "";
			this.imgUrl = imgUrl;
		}
		
		prepareModel(model);
		return "puzzle-nehila";
	}
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String addComment(@RequestBody Comment comment, Model model) {
		if(userController.isLogged()) {
			commentService.addComment(comment);
		}
		
		prepareModel(model);
		
		return "puzzle-nehila :: comments";
	}
	
	@RequestMapping(value = "/rating", method = RequestMethod.POST)
	public String setRating(@RequestBody Rating rating, Model model) {
		if(userController.isLogged()) {
			ratingService.setRating(rating);
		}
		
		prepareModel(model);
		
		return "puzzle-nehila :: ratings";
	}
	
	public String getHtmlField() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<div class='flex-holder field %s %s'>", numberToWord(field.getFieldSize()), field.getGameState().toString().toLowerCase()));
		
		for(int row = 0; row < field.getFieldSize(); row++) {
			for(int column = 0; column < field.getFieldSize(); column++) {
				
				String tileClassList = "tile ";
				if(this.imgSource.equals("numbers")) {
					tileClassList += "num ";
				}
				
				Tile tile = field.getTile(row, column);
				tileClassList += getTileClass(tile, field);
				String tileLink = String.format("<span class='%s' onclick='fieldAction(%s, %s)'>\n", tileClassList, row, column);
				sb.append(tileLink);
				if(this.imgSource.equals("numbers")) {
					sb.append(getTileIndex(tile));
				}
				sb.append("</span>\n");
				
			}
		}
		sb.append("</div>");
		
		return sb.toString();
	}
	
	public String getAvgRatingStars() {
		int avgRating = ratingService.getAverageRating("puzzle-nehila");
		
		String avgStars = "";
		for(int i = 0; i < 5; i++) {
			avgStars += "<span";
			if(i < avgRating) {
				avgStars += " class='checked'";
			}
			avgStars += ">&#9733;</span>";
		}
		
		return avgStars;
	}
	
	public String getUserRatingStars() {
		int userRating = ratingService.getRating("puzzle-nehila", userController.getLoggedUser());
		
		String userStars = "";
		for(int i = 0; i < 5; i++) {
			userStars += "<span";
			if(i < userRating) {
				userStars += " class='checked'";
			}
			int index = i + 1;
			userStars += String.format(" starIndex='%d' onclick='setRate(%d)' onmouseover='handleStarHover(%d)' onmouseout='handleStarMouseOut()'>&#9733;</span>", index, index, index);
		}
		
		return userStars;
	}
	
	public GameState getGameState() {
		return field.getGameState();
	}
	
	public String getGameGuide() {
		return field.getGameGuide();
	}
	
	public String getImgSource() {
		return imgSource;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	private void newGame(int fieldSize) {
		field = new Field(fieldSize);
	}
	
	private void prepareModel(Model model) {
		model.addAttribute("scores", scoreService.getBestScores("puzzle-nehila"));
		model.addAttribute("comments", commentService.getComments("puzzle-nehila"));
		model.addAttribute("actionCount", this.field.getActionCount());
		
		model.addAttribute("avgRating", ratingService.getAverageRating("puzzle-nehila"));
		
		if(userController.isLogged()) {
			model.addAttribute("userRating", ratingService.getRating("puzzle-nehila", userController.getLoggedUser()));
		}
	}
	
	private String getTileClass(Tile tile, Field field) {
		if(tile instanceof Fragment) {
			return "part-" + (((Fragment) tile).getIndex() + 1);
		} else if(field.getGameState() == GameState.SOLVED) {
			return "part-" + (field.getFieldSize() * field.getFieldSize());
		} else {
			return "empty";
		}
	}
	
	private String getTileIndex(Tile tile) {
		if(tile instanceof Fragment) {
			return "" + (((Fragment) tile).getIndex() + 1);
		} else {
			return "";
		}
	}
	
	private String numberToWord(int num) {
		String[] numNames = {"", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"
		};
		
		return numNames[num];
	}
}
