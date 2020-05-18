package sk.tuke.gamestudio.service.jdbc;


import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
    create table comment (
        player varchar(64) not null,
        game varchar(64) not null,
        comment varchar(64) not null,
        commentedon timestamp not null
    );
*/

public class CommentServiceJDBC implements CommentService {
	public static final String INSERT_COMMENT = "INSERT INTO comment (player, game, comment, commentedon) VALUES (?, ?, ?, ?);";
	public static final String SELECT_COMMENTS = "SELECT player, game, comment, commentedon FROM comment WHERE game = ? ORDER BY commentedon;";
	private final JDBCManager jdbc;
	
	public CommentServiceJDBC() {
		jdbc = JDBCManager.INSTANCE;
	}
	
	@Override
	public void addComment(Comment comment) throws CommentException {
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT)) {
				ps.setString(1, comment.getPlayer());
				ps.setString(2, comment.getGame());
				ps.setString(3, comment.getComment());
				ps.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
				
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new CommentException("Error saving comment", e);
		}
	}
	
	@Override
	public List<Comment> getComments(String game) throws CommentException {
		List<Comment> comments = new ArrayList<>();
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(SELECT_COMMENTS)) {
				ps.setString(1, game);
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						Comment comment = new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
						comments.add(comment);
					}
				}
			}
		} catch (SQLException e) {
			throw new CommentException("Error loading comment", e);
		}
		return comments;
	}
}
