package com.tank.game;

import java.awt.Color;

public class Player {
	private String name;
	private Color color;
	private int score;
	private Tank tank;
	
	public Player(String name, Color color){
		this.name = name;
		this.color = color;
		this.score = 0;
		this.tank = new Tank(name,color);
	}
	
	public void increasingScore(){
		this.score++;
	}
	
	public Tank getTank(){
		return this.tank;
	}
}
