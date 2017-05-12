package UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Game;


public class GameUI extends JFrame{
	
	private int width = 800;
	private int height = 800;
	private JPanel background;
	private Graphics g;
	private URL tankURL;
	
	private Game game;
	
	public GameUI(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
		setVisible(true);
		pack();
		game = Game.getInstance();
	}

	public void start() {
		game.start();
	}

	private void initComponents() {
		background = new JPanel();		
		background.setPreferredSize(new Dimension(width, height));
		background.setLayout(null);
		add(background);
		drawTank(40,50);
		drawTank(500,600);
	}
	
	public void drawTank(int x, int y){
		tankURL = this.getClass().getClassLoader().getResource("images/tank.png");
		RedTankUI t = new RedTankUI(new JLabel(new ImageIcon(tankURL)));
		t.setBounds(x, y, 100, 100);
		background.add(t);
	}
	
	public static void main(String [] args){
		GameUI g = new GameUI();
	}
}
