package com.tank.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
	
	public static BufferedImage image;
	private int positionx;
	private int positiony;
	private long firedAt;
	private boolean active;
	private int direction;
	
	private final int velocity = 2;


	public Bullet(int xPos, int yPos){
		active = true;
		this.positionx = xPos;
		this.positiony = yPos;
		try {
			image = ImageIO.read( new File("/Users/Piromsurang/Documents/workspace-java/TankGame/src/com/tank/images/bullet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void move() {
		if(!hitWall()) {
			if(direction == Tank.EAST) {
				positionx += velocity;
			} else if(direction == Tank.WEST) {
				positionx -= velocity;
			} else if(direction == Tank.NORTH) {
				positiony -= velocity;
			} else if(direction == Tank.SOUTH) {
				positiony += velocity;
			}
		} 
	}
	
	public void activate() {
		active = true;
	}
	
	public boolean hitWall() {
		long time = System.currentTimeMillis();
		return (time - firedAt) > 1000;
//		if(positionx < 0 || positionx > Window.width-image.getWidth()) {
//			return true;
//		}
//		if(positiony < 0 || positiony > Window.height-image.getHeight()) {
//			return true;
//		}
//		return false;
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
	
	public int getPositionx() {
		return positionx;
	}

	public int getPositiony() {
		return positiony;
	}

	public void setPositionx(int positionx) {
		this.positionx = positionx;
	}

	public void setPositiony(int positiony) {
		this.positiony = positiony;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setFiredAt(long t) {
		firedAt = t;
	}
	
}

