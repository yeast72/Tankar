package com.tank.game;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Tank {

	public static BufferedImage image;
	private ArrayList<Bullet> activeBullets;
	private ArrayList<Bullet> nonActiveBullets;
	private final int maxBullet = 5;
	private int positionx;
	private int positiony;
	private int direction;
	public static boolean shooted;

	public static int WEST = 3;
	public static int EAST = 1;
	public static int NORTH = 0;
	public static int SOUTH = 2;

	public Tank() {
		positionx = Window.BORDER;
		positiony = Window.BORDER;
		direction = WEST;
		try {
			URL tankURL = ClassLoader.getSystemResource("images/tank.png");
			image = ImageIO.read(tankURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		shooted = false;
		
		activeBullets = new ArrayList<Bullet>();
		nonActiveBullets = new ArrayList<Bullet>();
		for(int i=0; i<maxBullet; i++){
			activeBullets.add(new Bullet(positionx + image.getWidth(), positiony + image.getHeight()));		
		}
	}
	

	public BufferedImage getImage() {
		return image;
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
			Command c = new CommandUp(positiony);
			positiony = c.execute();
		}
	}

	public void moveDown() {
		rotate(direction, SOUTH);
		if(canMove(SOUTH)) {
			Command c = new CommandDown(positiony);
			positiony = c.execute();
		}
	}

	public void moveLeft() {
		//		AffineTransform tx = new AffineTransform();
		//		tx.rotate(Math.PI/4, image.getWidth()/2, image.getHeight()/2);
		//		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		//		image = op.filter(image, null);
		rotate(direction, WEST);
		if(canMove(WEST)) {
			Command c = new CommandLeft(positionx);
			positionx = c.execute();	
		}
	}

	public void moveRight() {
		rotate(direction, EAST);
		if(canMove(EAST)) {
			Command c = new CommandRight(positionx);
			positionx = c.execute();
		}
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

	public boolean canMove(int direction) {
		if(direction == NORTH && positiony-1 > Window.BORDER){
			return true;
		} else if(direction == SOUTH && positiony+1 < Window.height - Window.BORDER - image.getHeight()){
			return true;
		} else if(direction == WEST && positionx-1 > Window.BORDER){
			return true;
		} else if(direction == EAST && positionx+1 < Window.width - Window.BORDER - image.getWidth()){
			return true;
		} else {
			return false;
		}
		
	}
	
	public void shoot() {
		Bullet bullet = getActiveBullet();
		if(bullet != null){
			bullet.deactivate();
			nonActiveBullets.add(bullet);
			activeBullets.remove(bullet);
			
			bullet.setFiredAt(System.currentTimeMillis());
			bullet.setDirection(direction);
			if(direction == EAST) {
				bullet.setPositionx(positionx + image.getWidth());
				bullet.setPositiony(positiony - 5 + image.getHeight()/2 );
			} else if(direction == WEST) {
				bullet.setPositionx(positionx - bullet.getImage().getWidth());
				bullet.setPositiony(positiony - 5 + image.getHeight()/2 );
			} else if(direction == NORTH) {
				bullet.setPositionx(positionx - 5 + image.getWidth()/2);
				bullet.setPositiony(positiony - bullet.getImage().getHeight());
			} else if(direction == SOUTH) {
				bullet.setPositionx(positionx - 5 + image.getWidth() / 2);
				bullet.setPositiony(positiony + image.getHeight());
			}
			shooted = true;
		}
	}
	
	public boolean reloadBullet() {
		for(int i=0; i<nonActiveBullets.size(); i++){
			Bullet bullet = nonActiveBullets.get(i);
			if(bullet.hitWall()){
				activeBullets.add(bullet);
				nonActiveBullets.remove(bullet);
				bullet.setPositionx(positionx+image.getWidth()/2);
				bullet.setPositiony(positiony + image.getHeight()/2);
				bullet.activate();
				return true;
			}
		}
		return false;
	}
}
