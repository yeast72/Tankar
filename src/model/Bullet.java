package model;

public class Bullet {
	private int xPos;
	private int yPos;

	public Bullet(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public boolean hitEnermy(int enermyXPos, int enermyYPos){
		return true; // logic
	}
}
