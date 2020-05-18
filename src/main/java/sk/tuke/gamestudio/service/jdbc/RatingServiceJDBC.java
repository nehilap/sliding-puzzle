package sk.tuke.gamestudio.service.jdbc;


import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import java.sql.*;

/*
    create table rating(
        player varchar(64) not null,
        game varchar(64) not null,
        rating int not null,
        ratedon timestamp not null,
        primary key(player, game)
    );
*/

public class RatingServiceJDBC implements RatingService {
	public static final String SET_RATING = "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)" + " ON CONFLICT (player, game) DO UPDATE SET rating = ?, ratedon = ?";
	public static final String SELECT_RATING_BY_PLAYER = "SELECT rating FROM rating WHERE game = ? AND player = ?;";
	public static final String SELECT_AVG_RATING = "SELECT AVG(rating) FROM rating WHERE game = ?;";
	private final JDBCManager jdbc;
	
	public RatingServiceJDBC() {
		jdbc = JDBCManager.INSTANCE;
	}
	
	@Override
	public void setRating(Rating rating) throws RatingException {
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(SET_RATING)) {
				ps.setString(1, rating.getPlayer());
				ps.setString(2, rating.getGame());
				ps.setInt(3, rating.getRating());
				ps.setDate(4, new Date(rating.getRatedon().getTime()));
				ps.setInt(5, rating.getRating());
				ps.setTimestamp(6, new Timestamp(rating.getRatedon().getTime()));
				
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RatingException("Error saving rating", e);
		}
	}
	
	@Override
	public int getAverageRating(String game) throws RatingException {
		int average_rating = 0;
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(SELECT_AVG_RATING)) {
				ps.setString(1, game);
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						average_rating = rs.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			throw new RatingException("Error loading rating", e);
		}
		
		return average_rating;
	}
	
	@Override
	public int getRating(String game, String player) throws RatingException {
		int rating = 0;
		try (Connection connection = jdbc.getNewConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING_BY_PLAYER)) {
				ps.setString(1, game);
				ps.setString(2, player);
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						rating = rs.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			throw new RatingException("Error loading rating", e);
		}
		
		return rating;
	}
}
