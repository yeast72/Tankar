package com.tank.game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class TankImage {
	private static URL tankURL;
	public static BufferedImage image;
	
	public static BufferedImage getImage(Color c){
		System.out.println(c.toString()+"lll");
		try {
			tankURL = ClassLoader.getSystemResource("images/tank.png");
			image = ImageIO.read(tankURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	
	public static int getHeight(){
		return image.getHeight();
	}
	
	public static int getWidth(){
		return image.getWidth();
	}

}
