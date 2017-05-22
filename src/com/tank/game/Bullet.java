package com.tank.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Bullet {
	
	public static BufferedImage image;
	private int xPosition;
	private int yPosition;
	private long firedAt;
	private boolean active;
	private int direction;
	private boolean shooted;
	
	private final int velocity = 3;


	public Bullet(int xPos, int yPos){
		active = true;
		this.xPosition = xPos;
		this.yPosition = yPos;
		try {
			
			URL bulletURL = ClassLoader.getSystemResource("images/bullet.png");
			image = ImageIO.read(bulletURL);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		shooted = false;
	}
	
	public boolean isShooted() {
		return shooted;
	}
	
	public void setShooted(boolean a) {
		shooted = a;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void move() {
		if(!hitWall() && shooted) {
			if(direction == Tank.EAST) {
				xPosition += velocity;
			} else if(direction == Tank.WEST) {
				xPosition -= velocity;
			} else if(direction == Tank.NORTH) {
				yPosition -= velocity;
			} else if(direction == Tank.SOUTH) {
				yPosition += velocity;
			}
		} else {
			setShooted(false);
			xPosition = xPosition + Tank.image.getWidth()/2;
			yPosition = yPosition + Tank.image.getHeight()/2;
		}
	}
	
	public void activate() {
		active = true;
	}
	
	public boolean hitWall() {
		if(xPosition < Window.BORDER || xPosition > Window.width - Window.BORDER -image.getWidth()) {
			return true;
		}
		if(yPosition < Window.BORDER || yPosition > Window.height - Window.BORDER -image.getHeight()) {
			return true;
		}
		
		return false;
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
		return xPosition;
	}

	public int getPositiony() {
		return yPosition;
	}

	public void setPositionx(int positionx) {
		this.xPosition = positionx;
	}

	public void setPositiony(int positiony) {
		this.yPosition = positiony;
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

