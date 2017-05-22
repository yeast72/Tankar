package com.tank.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

	private Key up = new Key();
	private Key down = new Key();
	private Key left = new Key();
	private Key right = new Key();
	private Key space = new Key();
	private Key reload = new Key();

	public InputHandler(Window window) {
		window.requestFocus();
		window.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	private void toggleKey(int keyCode, boolean isPressed) {
		if(keyCode == KeyEvent.VK_UP) {
			up.toggle(isPressed);
		} else if(keyCode == KeyEvent.VK_DOWN) {
			down.toggle(isPressed);
		} else if(keyCode == KeyEvent.VK_LEFT) {
			left.toggle(isPressed);
		} else if(keyCode == KeyEvent.VK_RIGHT) {
			right.toggle(isPressed);
		} else if(keyCode == KeyEvent.VK_SPACE) {
			space.toggle(isPressed);
		} else if(keyCode == KeyEvent.VK_R) {
			reload.toggle(isPressed);
		}
	}
	
	public Key getReload() {
		return reload;
	}
	
	public Key getSpace() {
		return space;
	}
	
	public Key getUp() {
		return up;
	}

	public Key getDown() {
		return down;
	}

	public Key getLeft() {
		return left;
	}

	public Key getRight() {
		return right;
	}

	protected class Key {
		private boolean pressed = false;

		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
		}
	}
}
