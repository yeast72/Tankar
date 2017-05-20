package model;

import java.util.ArrayList;
import java.util.List;

public class BulletPool {
	
private List<Bullet> bullets;
	
	private static BulletPool instance;
	
	private BulletPool() {
		bullets = new ArrayList<Bullet>();
		for( int i = 0 ; i < 20 ; i++ ) {
			bullets.add( new Bullet(0,0) ); //assume posx = 0 and posy = 0
		}
	}
	
	public static BulletPool getInstance() {
		if( instance == null ) {
			instance = new BulletPool();
		}
		return instance;
	}
	
	public Bullet nextAvailableBullet() {
		for( Bullet b : bullets ) {
			if( !b.isActive() ) {
				return b;
			}
		}
		Bullet b = new Bullet(0,0); //assume posx = 0 and posy = 0
		bullets.add(b);
		return null;
	}

}
