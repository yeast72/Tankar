package model;

import java.awt.Color;

public class User {
	
	private String name;
	private int score;
	private Tank tank;
	
	public User(String name){
		this.name = name;
		this.score = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public int getScore(){
		return score;
	}

}
