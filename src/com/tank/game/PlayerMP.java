package com.tank.game;

import java.net.InetAddress;

public class PlayerMP extends Player{
	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(String name, String color,InetAddress ipAddress,int port) {
		super(name, color);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	public PlayerMP(String name,String color, int x,int y,InetAddress ipAddress,int port){
		super(name,color);
		this.ipAddress = ipAddress;
		this.port = port;
		getTank().setPositionX(x);
		getTank().setPositionY(y);
	}
	

}
