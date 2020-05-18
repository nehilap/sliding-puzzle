package sk.tuke.gamestudio.game.puzzle.nehila.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.puzzle.nehila.core.Field;
import sk.tuke.gamestudio.game.puzzle.nehila.core.GameState;
import sk.tuke.gamestudio.service.*;

import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsoleUI {
	public static String GAME = "slidingPuzzle";
	private final Scanner sc;
	private final Pattern INPUT_PATTERN;
	private Field field;
	private int digitCount;
	private int charCountNeeded;
	private String playerName;
	@Autowired
	private ConsolePrinter printer;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private CommentService commentService;
	
	public ConsoleUI() {
		sc = new Scanner(System.in);
		INPUT_PATTERN = Pattern.compile("([a-zA-Z]+)(\\d+)");
		
		printer = new ConsolePrinter();
	}
	
	public void start() {
		printer.printWelcome();
		
		int menuInput;
		do {
			printer.printMenuOptions();
			menuInput = handleMenuInput();
			
			switch (menuInput) {
				case 0:
					printer.info("Ďakujeme, že ste si zahrali.  Dovidenia!!");
					System.exit(0);
					break;
				case 1:
					handleSettingsAndPlay();
					break;
				case 2:
					printer.printTutorial();
					break;
				case 3:
					printer.printTopScores();
					break;
				case 4:
					printer.printAvgRating();
					break;
				case 5:
					printer.printRatingByPlayer(getPlayerName());
					break;
				case 6:
					printer.printComments();
					break;
				case -1:
					printer.error("Nezadali ste správny vstup.");
					break;
				default:
					break;
			}
		} while(menuInput != 0);
	}
	
	private void handleSettingsAndPlay() {
		
		playerName = getPlayerName();
		
		System.out.print("Zadaj veľkost hracieho poľa (>= 3 <= 100) alebo '0' pre návrat späť:");
		String line = sc.nextLine().replaceAll(" ", "");
		
		if(line.matches("\\d+")) {
			int fieldSize = Integer.parseInt(line);
			
			if(fieldSize == 0) {
				return;
			} else if(fieldSize > 2 && fieldSize < 101) {
				play(new Field(fieldSize));
			} else {
				printer.error("Zadal si nesprávnu hodnotu veľkosti poľa!!!");
			}
		} else {
			printer.error("Zadal si vstup v zlom formáte!!!");
		}
	}
	
	private int handleMenuInput() {
		String line = sc.nextLine().replaceAll(" ", "");
		
		if(line.matches("\\d+")) {
			return Integer.parseInt(line);
		}
		return -1;
	}
	
	private int handleGameInput() {
		System.out.print("Vyber políčko na presun v tvare 'riadokstlpec' (napr.: A1): ");
		
		String line = sc.nextLine().replaceAll(" ", "");
		if(line.equals("0")) {
			return 0;
		}
		
		Matcher m = INPUT_PATTERN.matcher(line);
		if(m.matches()) {
			int row = processRowToIndex(m.group(1).toUpperCase()) - 1;
			int col = Integer.parseInt(m.group(2)) - 1;
			
			if(row < 0 || row >= field.getFieldSize() || col < 0 || col >= field.getFieldSize()) {
				printer.error("Zadal si hodnoty. ktoré su mimo rozmedzia poľa");
			} else {
				field.moveFragment(row, col);
			}
		} else {
			printer.error("Zadal si vstup v zlom formáte!!!");
		}
		
		return 1;
	}
	
	private void handlePostGameMenu() {
		
		int menuInput;
		do {
			printer.printPostGameMenuOptions();
			menuInput = handleMenuInput();
			
			switch (menuInput) {
				case 0:
					break;
				case 1:
					handleRatingMenu();
					break;
				case 2:
					System.out.println("Zadaj komentár:");
					addComment(sc.nextLine().trim());
					break;
				case -1:
					printer.error("Nezadali ste správny vstup.");
				default:
					break;
			}
		} while(menuInput != 0);
	}
	
	private void handleRatingMenu() {
		System.out.println("Zadaj hodnotenie v rozmedzí 1-5");
		
		String line;
		do {
			line = sc.nextLine();
			if(!line.matches("[1-5]")) {
				printer.error("Nezadali ste správny vstup.");
			}
		} while(!line.matches("[1-5]"));
		
		setRating(Integer.parseInt(line));
	}
	
	private void play(Field field) {
		// for testing
		field.generateSolved();
		
		printer.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		printer.info("Pre ukončenie hry zadaj kedykoľvek počas hry '0'.");
		
		this.field = field;
		digitCount = getDigitCount(field.getFieldSize() * field.getFieldSize() - 2);
		charCountNeeded = (int) Math.floor(Math.log(25 * (field.getFieldSize() + 1)) / Math.log(26));
		
		int inputCode;
		do {
			showGameBoard();
			inputCode = handleGameInput();
		} while(inputCode == 1 && field.getGameState() == GameState.PLAYING);
		
		if(inputCode == 0) {
			return;
		}
		
		showGameBoard();
		
		if(field.getGameState() == GameState.SOLVED) {
			printer.printGameResults(field.getActionCount(), field.calculateScore());
			
			saveScore();
			
			handlePostGameMenu();
		}
	}
	
	private int processRowToIndex(String rowString) {
		int row = 0;
		for(int i = 0; i < rowString.length(); i++) {
			row += (rowString.charAt(i) - 64) * Math.pow(26, rowString.length() - i - 1);
		}
		
		return row;
	}
	
	private void showGameBoard() {
		System.out.println();
		printHeader();
		printField();
	}
	
	private void printHeader() {
		for(int i = 0; i < charCountNeeded; i++) {
			System.out.print(" ");
		}
		System.out.print("|");
		
		for(int i = 0; i < field.getFieldSize(); i++) {
			System.out.printf("%" + digitCount + "d ", i + 1);
		}
		System.out.println();
		
		for(int i = 0; i < charCountNeeded; i++) {
			System.out.print("-");
		}
		System.out.print("+");
		for(int i = 0; i < (field.getFieldSize() * digitCount) + field.getFieldSize() - 1; i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private void printField() {
		for(int row = 0; row < field.getFieldSize(); row++) {
			System.out.print(getStringByCount(row + 1, charCountNeeded) + "|");
			
			for(int column = 0; column < field.getFieldSize(); column++) {
				System.out.printf("%" + digitCount + "s ", field.getTile(row, column));
			}
			System.out.println();
		}
	}
	
	private void saveScore() {
		Calendar cal = Calendar.getInstance();
		try {
			scoreService.addScore(new Score(GAME, playerName, field.calculateScore(), cal.getTime()));
		} catch (ScoreException e) {
			e.printStackTrace();
		}
	}
	
	private void addComment(String text) {
		Calendar cal = Calendar.getInstance();
		try {
			commentService.addComment(new Comment(playerName, GAME, text, cal.getTime()));
		} catch (CommentException e) {
			e.printStackTrace();
		}
	}
	
	private void setRating(int rating) {
		Calendar cal = Calendar.getInstance();
		try {
			ratingService.setRating(new Rating(playerName, GAME, rating, cal.getTime()));
		} catch (RatingException e) {
			e.printStackTrace();
		}
	}
	
	private String getPlayerName() {
		System.out.print("Zadajte meno hráča:");
		return sc.nextLine().replaceAll(" ", "");
	}
	
	private int getDigitCount(int num) {
		int count = 0;
		
		while(num != 0) {
			num /= 10;
			count++;
		}
		
		return count;
	}
	
	private String getStringByCount(int n, int charCount) {
		char[] buf = new char[(int) Math.floor(Math.log(25 * (n + 1)) / Math.log(26))];
		for(int i = buf.length - 1; i >= 0; i--) {
			n--;
			buf[i] = (char) ('A' + n % 26);
			n /= 26;
		}
		
		String result = "";
		for(int i = 0; i < charCount - buf.length; i++) {
			result += " ";
		}
		result += new String(buf);
		return result;
	}
}
