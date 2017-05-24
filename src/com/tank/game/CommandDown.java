package com.tank.game;

public class CommandDown implements Command {
	
	private int position;
	
	public CommandDown(int position) {
		this.position = position;
	}

	@Override
	public int execute() {
		position += Tank.velocity;
		if(position > Window.HEIGHT - TankImage.getHeight()) position = Window.HEIGHT - TankImage.getHeight();
		return position;
	}
	
}
