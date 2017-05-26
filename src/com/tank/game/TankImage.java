package com.tank.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;

public class TankImage implements Serializable {
	private URL tankURL;
	private static int width;
	private static int height;
	private transient BufferedImage image;
	
	public TankImage(String name, String color){
		try {
			tankURL = ClassLoader.getSystemResource("images/" + color + "Tank.png");
			image = ImageIO.read(tankURL);
			width = image.getWidth();
			height = image.getHeight();
			addName(name);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void addName(String name){
		Graphics g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.setFont(g.getFont());
		g.drawString(name, 5 , height - 2);
		g.dispose();
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public void setImage(BufferedImage buffImg){
		this.image = buffImg;
	}
	
	
	public static int getHeight(){
		return height;
	}
	
	public static int getWidth(){
		return width;
	}
}
