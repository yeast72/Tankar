package com.tank.game;

public class CommandRight implements Command {
	
	private int position;
	
	public CommandRight(int position) {
		this.position = position;
	}

	@Override
	public int execute() {
		position += Tank.velocity;
		if(position > Window.WIDTH - TankImage.getWidth()) position = Window.WIDTH - TankImage.getWidth();
		return position;
	}

}
