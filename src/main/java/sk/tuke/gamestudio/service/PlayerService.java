package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

public interface PlayerService {
	boolean addUser(Player player);
	
	boolean findPlayer(String name, String password);
	
	boolean findPlayerByName(String name);
}
