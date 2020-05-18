package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/player")
public class UserController {
	@Autowired
	PlayerService playerService;
	
	private String loggedUser;
	
	private boolean logged;
	
	@RequestMapping
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody Player player, Model model) {
		if(playerService.addUser(player)) {
			return "fragments :: success";
		}
		
		return "fragments :: error";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody Player player, Model model) {
		if(playerService.findPlayer(player.getName(), player.getPassword())) {
			loggedUser = player.getName();
			logged = true;
			
			return "redirect:/";
		}
		
		
		return "fragments :: error";
	}
	
	@RequestMapping("/logout")
	public String logout(Model model) {
		logged = false;
		loggedUser = null;
		return "login";
	}
	
	public String getLoggedUser() {
		return loggedUser;
	}
	
	public boolean isLogged() {
		return logged;
	}
}