package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.game.puzzle.nehila.consoleUI.ConsolePrinter;
import sk.tuke.gamestudio.game.puzzle.nehila.consoleUI.ConsoleUI;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.restclient.CommentServiceRestClient;
import sk.tuke.gamestudio.service.restclient.RatingServiceRestClient;
import sk.tuke.gamestudio.service.restclient.ScoreServiceRestClient;
/*
@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
	
	public static void main(String[] args) {
		
		//SpringApplication.run(SpringClient.class, args);
		
		new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
	}
	
	@Bean
	public CommandLineRunner runner(ConsoleUI ui) {
		return args -> ui.start();
	}
	
	@Bean
	public ConsoleUI ConsoleUI() {
		return new ConsoleUI();
	}
	
	@Bean
	public ScoreService scoreService() {
		// return new ScoreServiceJPA();
		return new ScoreServiceRestClient();
	}
	
	@Bean
	public RatingService ratingService() {
		//return new RatingServiceJPA();
		return new RatingServiceRestClient();
	}
	
	@Bean
	public CommentService commentService() {
		//return new CommentServiceJPA();
		return new CommentServiceRestClient();
	}
	
	@Bean
	public ConsolePrinter consolePrinter() {
		return new ConsolePrinter();
	}
}
*/