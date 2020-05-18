package sk.tuke.gamestudio.game.puzzle.nehila.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import java.text.SimpleDateFormat;
import java.util.List;

public class ConsolePrinter {
	private final SimpleDateFormat dateFormat;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private CommentService commentService;
	
	public ConsolePrinter() {
		dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	}
	
	public void printWelcome() {
		divider("****************************************");
		System.out.println("Vitaj!!! Toto je hra 'Sliding Puzzle'!");
		divider("****************************************");
	}
	
	public void printMenuOptions() {
		divider("________________________________________");
		System.out.println("Pre ukončenie zadaj '0'!");
		System.out.println("Pre hranie zadaj '1'!");
		System.out.println("Pre krátky návod zadaj '2'!");
		System.out.println("Pre výpis top hráčov zadaj '3'!");
		System.out.println("Pre zobrazenie priemerného hodnotenia zadaj '4'!");
		System.out.println("Pre zobrazenie hodnotenia podľa hráča zadaj '5'!");
		System.out.println("Pre zobrazenie komentárov zadaj '6'!");
		divider("________________________________________");
	}
	
	public void printPostGameMenuOptions() {
		divider("________________________________________");
		System.out.println("Pre prechod do hlavného menu zadaj '0'!");
		System.out.println("Pre ohodnotenie hry zadaj '1'!");
		System.out.println("Pre pridanie komentára zadaj '2'!");
		divider("________________________________________");
	}
	
	public void printTutorial() {
		System.out.println("Vašou úlohou je usporiadanie políčok puzzle, ");
		System.out.println("tak, aby tvorili obrázok alebo aby boli v číselnom poradí, ");
		System.out.println("pričom môžete hýbať políčka vertikálne alebo horizontálne, ");
		System.out.println("ak susedia s prázdnym políčkom.");
		System.out.println("Systém bodov funguje podľa vzorca: ");
		System.out.println("(počet akcií / počet políčok) * 10");
		System.out.println("Čím menej bodov máte, tým ste lepší!");
	}
	
	public void printGameResults(int actionCount, int points) {
		System.out.println(ConsoleColors.ANSI_BG_YELLOW + "----------------------------------------");
		System.out.println("Gratulujem, vyriešil si puzzle úspešne!!");
		System.out.printf("Počet pohybov: %d!\n", actionCount);
		System.out.printf("Počet zarátaných bodov: %d!\n", points);
		System.out.println("----------------------------------------\n" + ConsoleColors.ANSI_RESET);
	}
	
	public void error(String output) {
		System.out.println(ConsoleColors.ANSI_RED + output + ConsoleColors.ANSI_RESET);
	}
	
	public void info(String output) {
		System.out.println(ConsoleColors.ANSI_BLUE + output + ConsoleColors.ANSI_RESET);
	}
	
	public void divider(String output) {
		System.out.println(ConsoleColors.ANSI_GREEN + output + ConsoleColors.ANSI_RESET);
	}
	
	public void printTopScores() {
		System.out.printf("#  %6s  %6s %6s\n", "meno", "#body", "dátum");
		try {
			List<Score> scores = scoreService.getBestScores(ConsoleUI.GAME);
			
			for(int i = 0; i < scores.size(); i++) {
				Score score = scores.get(i);
				System.out.printf("%d. %6s, %6d, %s\n", i + 1, score.getPlayer(), score.getPoints(), dateFormat.format(score.getPlayedOn()));
			}
			System.out.println();
		} catch (ScoreException e) {
			System.out.println("Nepodarilo sa získať skóre!");
			e.printStackTrace();
		}
	}
	
	public void printAvgRating() {
		try {
			System.out.printf("Priemerné hodnotenie hry 'Sliding Puzzle' je: %d!\n", ratingService.getAverageRating(ConsoleUI.GAME));
		} catch (RatingException rt) {
			System.out.println("Nepodarilo sa získať priemerné hodnotenie!");
			rt.printStackTrace();
		}
	}
	
	public void printRatingByPlayer(String playerName) {
		try {
			System.out.printf("Vaše hodnotenie je: %d!\n", ratingService.getRating(ConsoleUI.GAME, playerName));
		} catch (RatingException rt) {
			System.out.println("Nepodarilo sa získať hodnotenie!");
			rt.printStackTrace();
		}
	}
	
	public void printComments() {
		try {
			List<Comment> comments = commentService.getComments(ConsoleUI.GAME);
			
			for(int i = 0; i < comments.size(); i++) {
				Comment comment = comments.get(i);
				System.out.printf("%d. %6s, %s, %s\n", i + 1, comment.getPlayer(), dateFormat.format(comment.getCommentedOn()), comment.getComment());
			}
			System.out.println();
			
		} catch (CommentException e) {
			System.out.println("Nepodarilo sa získať komentáre!");
			e.printStackTrace();
		}
	}
}
