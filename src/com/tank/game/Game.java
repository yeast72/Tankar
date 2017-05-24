package com.tank.game;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Game {

	private static Game instance = null;
	private List<Player> players;
	private long startTime;

	private Game() {
		players = new ArrayList<Player>();
		/*
		tank = new Tank(Window.BORDER, Window.BORDER);
		enemy = new Tank(100, 100);*/
	}

	public static Game getInstance() {
		if(instance == null) {
			instance = new Game();
		}
		return instance;
	}
	
	/*public Tank getTank() {
		return players.get(arg0);
	}*/

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
	
	/* This part will maintain with 1 player and 1 enemy*/
	public Player getEnemy(){
			return players.get(1);
	}
	
	public void createNewPlayer(String name, String txtColor){
		System.out.println(txtColor);
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(txtColor);
			color = (Color) field.get(null);
		  
		} catch (Exception e) {
			color = Color.GRAY;
		}
		 System.out.println(color.toString()+"jiii");
		 
		players.add(new Player(name, Color.BLACK));
		players.add(new Player("E", Color.RED));
	}
	
	public List<Player> getAllPlayers(){
		return this.players;
	}
	

}
