package model;

public class Bullet {
	private int xPos;
	private int yPos;
	private long firedAt;
	private boolean active;


	public Bullet(int xPos, int yPos){
		firedAt = System.currentTimeMillis();
		active = true;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public boolean hitWall() {
		long time = System.currentTimeMillis();
		return time - firedAt > 1000;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean hitEnermy(int enermyXPos, int enermyYPos){
		return true; // logic
	}
}
