package com.tank.game;

import java.util.ArrayList;
import java.util.List;

public class Game  {

	private static Game instance = null;
	private List<Player> players;
	private long startTime;

	private Game() {
		players = new ArrayList<Player>();
	}

	public static Game getInstance() {
		if(instance == null) {
			instance = new Game();
		}
		return instance;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void stop() {

	}
	
	public boolean isDone() {
		long endingTime = System.currentTimeMillis();
		double delta = (endingTime - startTime) * 1000;
		if(delta >= 180) {
			return true;
		}
		return false;
	}
	
 	public void addPlayer(Player player){
		players.add(new Player(player.getName(),player.getColor()));
	}
	
	public void createNewPlayer(String name, String txtColor){ 
		players.add(new Player(name, txtColor));
	}
	
	public List<Player> getAllPlayers(){
		return this.players;
	}

	public void removePlayerMP(String username) {
		int index = 0;
		for(Player p : players){
			if(p.getName().equals(username)){
				break;
			}
			index++;
		}
		this.players.remove(index);
	}
	private int getPlayerMPIndex(String username){
		int index = 0;
		for(Player p : players){
			if(p.getName().equals(username)){
				break;
			}
			index++;
		}
		return index;
	}
	public void movePlayer(String username,int x,int y, int direction){
		int index = getPlayerMPIndex(username);
		this.players.get(index).getTank().setPositionX(x);
		this.players.get(index).getTank().setPositionY(y);
		this.players.get(index).getTank().checkIfRelaodingFinished();
		ArrayList<Bullet> nonActiveBullets = this.players.get(index).getTank().getListOfNonActive();
		for (int i = 0; i < nonActiveBullets.size(); i++) {
			nonActiveBullets.get(i).move();
		}
	}
	public void playerShoot(String username){
		int index = getPlayerMPIndex(username);
		this.players.get(index).getTank().shoot();
		ArrayList<Bullet> nonActiveBullets = this.players.get(index).getTank().getListOfNonActive();
		for (int i = 0; i < nonActiveBullets.size(); i++) {
			nonActiveBullets.get(i).move();
		}
		
	}

	public void updateGame(Game game) {
		instance = game ;
	}

	public void playerReload(String username) {
		int index= getPlayerMPIndex(username);
		this.players.get(index).getTank().reloadBullet();
		
	}

}
