package com.tank.game;

public class CommandUp implements Command {
	
	private int position;
	
	public CommandUp(int position) {
		this.position = position;
	}

	@Override
	public int execute() {
		position -= Tank.velocity;
		if(position < 0) position = 0;
		return position;
	}

}
