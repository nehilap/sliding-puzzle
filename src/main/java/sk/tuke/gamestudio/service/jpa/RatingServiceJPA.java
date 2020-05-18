package sk.tuke.gamestudio.service.jpa;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class RatingServiceJPA implements RatingService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void setRating(Rating rating) throws RatingException {
		// https://vladmihalcea.com/jpa-persist-merge-hibernate-save-update-saveorupdate/
		/*
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(rating);
		*/
		
		if(getRating(rating.getGame(), rating.getPlayer()) == 0) {
			System.out.println("Inserting new rating");
			entityManager.persist(rating);
		} else {
			System.out.println("Updating rating");
			entityManager.merge(rating);
		}
	}
	
	@Override
	public int getAverageRating(String game) throws RatingException {
		try {
			return (entityManager.createNamedQuery("Rating.getAverageRating", Double.class).setParameter("game", game).getSingleResult()).intValue();
		} catch (Exception ex) {
			System.out.printf("Error receiving avg rating for game: %s\n--------------------------------------------------------\n", game);
			ex.printStackTrace();
			System.out.println("--------------------------------------------------------");
		}
		
		return 0;
	}
	
	@Override
	public int getRating(String game, String player) throws RatingException {
		try {
			return entityManager.createNamedQuery("Rating.getRating", Integer.class).setParameter("game", game).setParameter("player", player).getSingleResult();
		} catch (Exception ex) {
			System.out.println("--------------------------------------------------------");
			System.out.printf("Did not find rating for player: %s, game: %s\n", player, game);
			System.out.println("--------------------------------------------------------");
		}
		
		return 0;
	}
}