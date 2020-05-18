package sk.tuke.gamestudio.service.jpa;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Transactional
public class PlayerServiceJPA implements PlayerService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public boolean addUser(Player player) {
		if(!findPlayerByName(player.getName())) {
			entityManager.persist(player);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean findPlayer(String name, String password) {
		try {
			// we try to execute query, if we find we happy, if not we happy still
			entityManager.createNamedQuery("Player.findPlayer", Player.class).setParameter("name", name).setParameter("password", password).getSingleResult();
			
			return true;
		} catch (NoResultException ex) {
			return false;
		}
	}
	
	@Override
	public boolean findPlayerByName(String name) {
		try {
			// we try to execute query, if we find we happy, if not we happy still
			entityManager.createNamedQuery("Player.findByName", Player.class).setParameter("name", name).getSingleResult();
			
			return true;
		} catch (NoResultException ex) {
			return false;
		}
	}
}