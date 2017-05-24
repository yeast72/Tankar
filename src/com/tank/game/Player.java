package com.tank.game;

import java.awt.Color;

public class Player {
	private String name;
	private Color color;
	private int score;
	
	public Player(String name, String color){
		this.name = name;
		this.color = Color.getColor(color);
		this.score = 0;
	}
	
	public void increasingScore(){
		this.score++;
	}
}
