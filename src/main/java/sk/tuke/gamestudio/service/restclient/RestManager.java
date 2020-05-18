package sk.tuke.gamestudio.service.restclient;

public enum RestManager {
	INSTANCE;
	
	private final String api = "http://localhost:8080/api/";
	
	public String getApi() {
		return api;
	}
}
