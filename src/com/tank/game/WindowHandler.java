package com.tank.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import packets.PacketDisconnect;

public class WindowHandler implements WindowListener{
	
	private final Window game;
	
	public WindowHandler(Window game) {
		this.game = game;
		game.addWindowListener(this);
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("name window Handler " + this.game.playerName);
		PacketDisconnect packet = new PacketDisconnect(this.game.playerName, this.game.playerColor);
		packet.writeData(game.gameClientSocket);
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
