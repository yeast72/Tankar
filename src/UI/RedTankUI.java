package UI;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class RedTankUI extends JComponent{
	ImageIcon image;
	URL tankURL;
	JComponent component;
	
	public RedTankUI(JComponent component){
		this.setLayout(new FlowLayout());
		this.add(component);
				
	}
	
	public void paint(Graphics g){
		int height = this.getHeight();
		int width = this.getWidth();
			
		g.setColor(Color.RED);
		g.fillRect(0,0, width, height);
		super.paint(g);
	}
	
}
