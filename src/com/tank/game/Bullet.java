package com.tank.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;

public class Bullet {
	
	public static BufferedImage image;
	private int xPosition;
	private int yPosition;
	private boolean active;
	private int direction;
	private boolean shooted;
	private boolean isHitEnemy;
	
	public static final int velocity = 3;


	public Bullet(int xPos, int yPos){
		active = true;
		this.xPosition = xPos;
		this.yPosition = yPos;
		isHitEnemy = false;
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
		if(!hitWall() && shooted && !isHitEnemy) {
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
			xPosition = xPosition + TankImage.getWidth()/2;
			yPosition = yPosition + TankImage.getHeight()/2;
		}
	}
	
	public void activate() {
		active = true;
	}
	
	public boolean hitWall() {

		if(xPosition < Window.BORDER || xPosition > Window.WIDTH - Window.BORDER -image.getWidth()) {
			shooted = false;
			return true;
		}
		if(yPosition < Window.BORDER || yPosition > Window.HEIGHT - Window.BORDER -image.getHeight()) {
			shooted = false;
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
	
	public boolean hitEnermy(int x, int y){
		if(xPosition >= x && xPosition <= x + TankImage.getWidth() && yPosition >= y && yPosition <= y + TankImage.getHeight() && isShooted()) {
			isHitEnemy = true;
			return true;
		}
		return false;
	}
	
	public boolean isHitEnemy() {
		return isHitEnemy;
	}
	
	public void changeIsHitEnemyStatus(int x, int y) {
		if(xPosition >= x + TankImage.getWidth() && yPosition >= y + TankImage.getHeight() ) {
			isHitEnemy = false;
		}
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

	
}

