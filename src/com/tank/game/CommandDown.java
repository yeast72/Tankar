package com.tank.game;

public class CommandDown implements Command {
	
	private int position;
	
	public CommandDown(int position) {
		this.position = position;
	}

	@Override
	public int execute() {
		position += Tank.velocity;
		if(position > Window.height-Tank.image.getHeight()) position = Window.height-Tank.image.getHeight();
		return position;
	}
	
}
