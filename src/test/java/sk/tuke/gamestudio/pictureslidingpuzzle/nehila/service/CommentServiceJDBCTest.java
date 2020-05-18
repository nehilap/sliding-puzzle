package sk.tuke.gamestudio.pictureslidingpuzzle.nehila.service;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.jdbc.CommentServiceJDBC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommentServiceJDBCTest {
	private final String GAME = "puzzleComment";
	Calendar cal = Calendar.getInstance();
	private CommentService service = new CommentServiceJDBC();
	private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	@Test
	public void addComment() throws CommentException, ParseException {
		String tmpGame = GAME + "Add";
		String commentText = "ahoj ako sa mas hra";
		
		Comment comment = new Comment("Zuzka", tmpGame, commentText, cal.getTime());
		service.addComment(comment);
		
		List<Comment> comments = service.getComments(tmpGame);
		Comment lastComment = comments.get(comments.size() - 1);
		
		assertEquals(comment.getComment(), lastComment.getComment());
		assertEquals(comment.getPlayer(), lastComment.getPlayer());
		assertEquals(comment.getGame(), lastComment.getGame());
		assertEquals(format.format(comment.getCommentedOn()), format.format(lastComment.getCommentedOn()));
	}
	
	@Test
	public void getComments() throws CommentException, ParseException {
		String tmpGame = GAME + "Get";
		String commentText = "ahoj ako sa mas hra";
		List<Comment> expectedComments = new ArrayList<>();
		
		for(int i = 0; i < 5; i++) {
			Comment comment = new Comment("Zuzka" + i, tmpGame, commentText + i, cal.getTime());
			service.addComment(comment);
			expectedComments.add(comment);
		}
		
		List<Comment> comments = service.getComments(tmpGame);
		int size = comments.size();
		for(int i = 0; i < 5; i++) {
			Comment tmpComment = comments.get(size - (5 - i));
			assertEquals(expectedComments.get(i).getComment(), tmpComment.getComment());
			assertEquals(expectedComments.get(i).getPlayer(), tmpComment.getPlayer());
			assertEquals(expectedComments.get(i).getGame(), tmpComment.getGame());
			assertEquals(format.format(expectedComments.get(i).getCommentedOn()), format.format(tmpComment.getCommentedOn()));
		}
	}
}
