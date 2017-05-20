package model;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game {
	
	private static Game instance = null;
	
	private Game() {

	}

	public static Game getInstance() {
		
		if(instance == null) {
			if(instance == null) {
				instance = new Game();
			}
		}
		
		return instance;
	}

	public synchronized void start() {
	
	}
	
	public synchronized void stop() {

	}
	

}
