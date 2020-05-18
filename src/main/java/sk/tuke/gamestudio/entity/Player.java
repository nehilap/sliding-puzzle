package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQueries({@NamedQuery(name = "Player.findPlayer", query = "SELECT u FROM Player u WHERE u.name = :name AND u.password = :password"), @NamedQuery(name = "Player.findByName", query = "SELECT u FROM Player u where u.name = :name")
})
public class Player implements Serializable {
	@Id
	private String name;
	
	private String password;
	
	public Player(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public Player() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
