package com.tank.game;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tank {

	private static BufferedImage image;
	private ArrayList<Bullet> activeBullets;
	private ArrayList<Bullet> nonActiveBullets;
	private final int maxBullet = 5;
	private int xPosition;
	private int yPosition;
	private int direction;
	private long startReloadingTime;
	private boolean reloadingState;
	private boolean isDie;

	public static int velocity;

	public static int WEST = 3;
	public static int EAST = 1;
	public static int NORTH = 0;
	public static int SOUTH = 2;
	public static int NORMAL_VELOCITY = 2;
	public static int REDUCED_VELOCITY = 1;
	
	private String name;
	private Color color;

	public Tank(String name, Color color) {
		this.name = name;
		this.color = color;
		image = TankImage.getImage(this.color);
		System.out.println("ki");
		xPosition = (int) (Math.random() * (Window.WIDTH - 3 * Window.BORDER)) + Window.BORDER;
		yPosition = (int) (Math.random() * (Window.HEIGHT - 3 * Window.BORDER)) + Window.BORDER;
		System.out.println(xPosition +" "+ yPosition);
		direction = WEST;
		velocity = NORMAL_VELOCITY;
		isDie = false;
		reloadingState = false;
		activeBullets = new ArrayList<Bullet>();
		nonActiveBullets = new ArrayList<Bullet>();
		activeBullets.add(new Bullet(xPosition + image.getWidth()/2, yPosition + image.getHeight()/2));
		for(int i=0; i<maxBullet-1; i++){
			nonActiveBullets.add(new Bullet(xPosition + image.getWidth()/2, yPosition + image.getHeight()/2));		
		}
	}

	public boolean isReloading() {
		return reloadingState;
	}
	

	public void rotate(int previousDirection, int nowDirection) {
		direction = nowDirection;
		int delta = nowDirection-previousDirection;
		AffineTransform tx = new AffineTransform();
		if(delta > 0) {
			tx.rotate(Math.PI/2*Math.abs(delta), image.getWidth()/2, image.getHeight()/2);
		} else {
			tx.rotate(-1*Math.PI/2*Math.abs(delta), image.getWidth()/2, image.getHeight()/2);
		}
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		image = op.filter(image, null);
	}

	public void moveUp() {
		rotate(direction, NORTH);
		if(canMove(NORTH)) {
			Command c = new CommandUp(yPosition);
			yPosition = c.execute();
		}
	}

	public void moveDown() {
		rotate(direction, SOUTH);
		if(canMove(SOUTH)) {
			Command c = new CommandDown(yPosition);
			yPosition = c.execute();
		}
	}

	public void moveLeft() {
		rotate(direction, WEST);
		if(canMove(WEST)) {
			Command c = new CommandLeft(xPosition);
			xPosition = c.execute();	
		}
	}

	public void moveRight() {
		rotate(direction, EAST);
		if(canMove(EAST)) {
			Command c = new CommandRight(xPosition);
			xPosition = c.execute();
		}
	}
	
	public BufferedImage getImage(){
		return this.image;
	}

	public Bullet getActiveBullet() {
		if(!activeBullets.isEmpty()){
			return activeBullets.get(0);
		}
		return null;
	}

	public ArrayList<Bullet> getListOfNonActive(){
		return nonActiveBullets;
	}

	public int getPositionX() {
		return xPosition;
	}

	public int getPositionY() {
		return yPosition;
	}

	public void setPositionX(int posX) {
		this.xPosition = posX;
	}

	public void setPositionY(int posY) {
		this.yPosition = posY;
	}

	public int getDirection() {
		return direction;
	}

	public boolean canMove(int direction) {
		if(direction == NORTH && yPosition-1 > Window.BORDER){
			return true;
		} else if(direction == SOUTH && yPosition+1 < Window.HEIGHT - Window.BORDER - image.getHeight()){
			return true;
		} else if(direction == WEST && xPosition-1 > Window.BORDER){
			return true;
		} else if(direction == EAST && xPosition+1 < Window.WIDTH - Window.BORDER - image.getWidth()){
			return true;
		} else {
			return false;
		}
	}

	public void setVelocity(int v) {
		velocity = v;
	}

	public int getVelocity() {
		return velocity;
	}

	public void shoot() {
		Bullet bullet = getActiveBullet();
		if(bullet != null){
			bullet.deactivate();
			bullet.setShooted(true);
			nonActiveBullets.add(bullet);
			activeBullets.remove(bullet);
			bullet.setDirection(direction);
			changeBulletDirection(bullet);
		}
	}
	
	public void changeBulletDirection(Bullet bullet) {
		if(direction == EAST) {
			bullet.setPositionx(xPosition + image.getWidth());
			bullet.setPositiony(yPosition - 5 + image.getHeight()/2 );
		} else if(direction == WEST) {
			bullet.setPositionx(xPosition - bullet.getImage().getWidth());
			bullet.setPositiony(yPosition - 5 + image.getHeight()/2 );
		} else if(direction == NORTH) {
			bullet.setPositionx(xPosition - 5 + image.getWidth()/2);
			bullet.setPositiony(yPosition - bullet.getImage().getHeight());
		} else if(direction == SOUTH) {
			bullet.setPositionx(xPosition - 5 + image.getWidth() / 2);
			bullet.setPositiony(yPosition + image.getHeight());
		}
	}

	public void reloadBullet() {
		if(!isReloading()){
			startReloadingTime = System.currentTimeMillis();
			reloadingState = true;
			setVelocity(REDUCED_VELOCITY);
		}
	}

	public boolean checkIfRelaodingFinished() {
		if(isReloading()) {
			long now = System.currentTimeMillis();
			double delta = (now - startReloadingTime)/1000;
			if(delta >= 5) {
				reloadingState = false;
				setVelocity(NORMAL_VELOCITY);
				Bullet bullet = nonActiveBullets.get(0);
				bullet.activate();
				nonActiveBullets.remove(bullet);
				activeBullets.add(bullet);			
				return true;
			} 
		}
		return false;
	}
	
	public boolean checkHitEnemy(Tank enemy) {
		for(Bullet b : nonActiveBullets ) {
			if(b.hitEnermy(enemy.getPositionX(), enemy.getPositionY()) && b.isShooted()) {
				enemy.die();
				System.out.println("Enemy dies");
				return true;
			}
		}
		return false;
	}
	
	public void die() {
		isDie = true;
	}

}
