package com.tank.game;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tank {

	public static BufferedImage image;
	private Bullet bullet;
	private int positionx;
	private int positiony;
	private int direction;
	public static boolean shooted;

	public static int WEST = 3;
	public static int EAST = 1;
	public static int NORTH = 0;
	public static int SOUTH = 2;

	public Tank() {
		positionx = 0;
		positiony = 0;
		direction = WEST;
		try {
			image = ImageIO.read( new File("/Users/Piromsurang/Documents/workspace-java/TankGame/src/com/tank/images/tank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		shooted = false;
		bullet = new Bullet(positionx + image.getWidth(), positiony + image.getHeight());
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
		if(!isHitWall()) {
			Command c = new CommandUp(positiony);
			positiony = c.execute();
		}
	}

	public void moveDown() {
		rotate(direction, SOUTH);
		if(!isHitWall()) {
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
		if(!isHitWall()) {
			Command c = new CommandLeft(positionx);
			positionx = c.execute();	
		}
	}

	public void moveRight() {
		rotate(direction, EAST);
		if(!isHitWall()) {
			Command c = new CommandRight(positionx);
			positionx = c.execute();
		}
	}

	public Bullet getBullet() {
		return bullet;
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

	public boolean isHitWall() {
		if(positionx < 0 || positionx > Window.width-image.getWidth()) return true;
		if(positiony < 0 || positiony > Window.height-image.getHeight()) return true;
		return false;
	}
	
	public void shoot() {
		if(bullet.isActive()) {
			//bullet.deactivate();
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
		if(bullet.hitWall()) {
			shooted = false;
			bullet.setPositionx(positionx+image.getWidth()/2);
			bullet.setPositiony(positiony + image.getHeight()/2);
			bullet.activate();
			return true;
		}
		return false;
	}
}
