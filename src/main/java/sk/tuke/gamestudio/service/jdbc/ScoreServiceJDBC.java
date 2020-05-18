package sk.tuke.gamestudio.service.jdbc;


import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
    CREATE TABLE score (
        player VARCHAR(64) NOT NULL,
        game VARCHAR(64) NOT NULL,
        points INTEGER NOT NULL,
        playedon TIMESTAMP NOT NULL
    );
     */

//INSERT INTO score (player, game, points, playedon) VALUES ('jaro', 'mines', 200, '2017-03-02 14:30')

//SELECT player, game, points, playedon FROM score WHERE game = 'mines' ORDER BY points DESC LIMIT 10;

public class ScoreServiceJDBC implements ScoreService {
	public static final String INSERT_SCORE = "INSERT INTO score (player, game, points, playedon) VALUES (?, ?, ?, ?);";
	public static final String SELECT_SCORE = "SELECT game, player, points, playedon FROM score WHERE game = ? ORDER BY points ASC, playedon LIMIT 10;";
	private final JDBCManager jdbc;
	
	public ScoreServiceJDBC() {
		jdbc = JDBCManager.INSTANCE;
	}
	
	@Override
	public void addScore(Score score) throws ScoreException {
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(INSERT_SCORE)) {
				ps.setString(1, score.getPlayer());
				ps.setString(2, score.getGame());
				ps.setInt(3, score.getPoints());
				ps.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
				
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new ScoreException("Error saving score", e);
		}
	}
	
	@Override
	public List<Score> getBestScores(String game) throws ScoreException {
		List<Score> scores = new ArrayList<>();
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(SELECT_SCORE)) {
				ps.setString(1, game);
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						Score score = new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4));
						scores.add(score);
					}
				}
			}
		} catch (SQLException e) {
			throw new ScoreException("Error loading score", e);
		}
		return scores;
	}
}
