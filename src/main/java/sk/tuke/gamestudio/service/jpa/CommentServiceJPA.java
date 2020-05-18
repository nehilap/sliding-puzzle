package sk.tuke.gamestudio.service.jpa;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addComment(Comment comment) throws CommentException {
		entityManager.persist(comment);
	}
	
	@Override
	public List<Comment> getComments(String game) throws CommentException {
		return entityManager.createNamedQuery("Comment.getComments", Comment.class).setParameter("game", game).getResultList();
	}
}