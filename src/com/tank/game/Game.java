package com.tank.game;

public class Game {

	private static Game instance = null;
	private Tank tank;
	private Tank enemy;
	private long startTime;

	private Game() {
		tank = new Tank(Window.BORDER, Window.BORDER);
		enemy = new Tank(100, 100);
	}

	public static Game getInstance() {

		if(instance == null) {
			if(instance == null) {
				instance = new Game();
			}
		}

		return instance;
	}
	
	public Tank getTank() {
		return tank;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void stop() {

	}
	
	public boolean isDone() {
		long endingTime = System.currentTimeMillis();
		double delta = (endingTime - startTime) * 1000;
		if(delta >= 180) {
			return true;
		}
		return false;
	}
	
	public Tank getEnemy() {
		return enemy;
	}

}
