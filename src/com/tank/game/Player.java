package com.tank.game;


public class Player {
	private String name;
	private String color;
	private int score;
	private Tank tank;
	
	public Player(String name, String color){
		this.name = name;
		this.color = color;
		this.score = 0;
		this.tank = new Tank(name, color);
	}
	
	public String getName(){
		return name;
	}
	
	public String getColor(){
		return color;
	}
	
	public void increasingScore(){
		this.score++;
	}
	
	public Tank getTank(){
		return this.tank;
	}

	public void updateTank(Tank tank) {
		this.tank = tank;
	}
}
