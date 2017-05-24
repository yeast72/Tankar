package com.tank.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int width = 1080;
	public static final int height = 720;
	public static final int SCALE = 3;
	public static final int BORDER = 25;
	public static final String NAME = "Tankar.io";
	
	private int viewOffset = 10;

	private JPanel drawPanel;
	private JDialog dialog;
	
	public boolean running;
	public int tickCount = 0;
	
	private Game game;
	private InputHandler inputHandler;
	
	public Window(){
		super(NAME);
		

		game = Game.getInstance();
		inputHandler = new InputHandler(this);
	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		initComponents();
		this.pack();
	}
	
	public void initComponents() {
		createDialog();
		drawPanel = new JPanel() {
			{
				setPreferredSize(new Dimension(width , height));
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				paintBackground(g);
				drawBackground(g);
				drawCharacter(g);
			}

		};
		add(drawPanel);
	}
	
	private void createDialog(){
		JTextField username = new JTextField(10);
		dialog = new JDialog();
		dialog.setSize(new Dimension(500, 80));
		dialog.setTitle("Welcome to Tankar.io");
		dialog.setLayout(new FlowLayout());
		dialog.add(new JLabel("Username: "));
		dialog.add(username);
		dialog.add(new JLabel("Color: "));
		
		String[] color = {"Red","Orange","Green", "Gray", "Blue", "Yellow", "Violet"};
		JComboBox colorChoice = new JComboBox(color);
		dialog.add(colorChoice);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//game.getTank().setInfo(username.getText().toString(), colorChoice.getSelectedItem());
				
			}});
		dialog.add(submit);
		dialog.setVisible(true);
	}
	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	private void drawBackground(Graphics g){
		URL bgURL = ClassLoader.getSystemResource("images/bg.png");
		Image image = null;
		try {
			image = ImageIO.read(bgURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	private void drawCharacter(Graphics g) {
		g.setColor(Color.black);
		g.drawImage(game.getTank().getImage(), game.getTank().getPositionx(), game.getTank().getPositiony(), null);
		
		ArrayList<Bullet> nonActiveBullets = game.getTank().getListOfNonActive();
		for(int i=0; i<nonActiveBullets.size(); i++){
			Bullet b = nonActiveBullets.get(i);
			g.drawImage(b.getImage(),b.getPositionx(),b.getPositiony(),null);
		}
		
		/*if(game.getTank().getBullet().isActive() && Tank.shooted) {
			g.drawImage(game.getTank().getBullet().getImage(), game.getTank().getBullet().getPositionx(), game.getTank().getBullet().getPositiony(), null);
		}*/
	}

	public synchronized void start() {
		running = true;
		game.start();
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while(delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
//				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(shouldRender) {
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				//System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	/**
	 * update all internal variable, and game logic
	 */
	public void tick() {
		tickCount++;
		running = game.isDone();
		if(inputHandler.getUp().isPressed()) {
			game.getTank().moveUp();
		} else if(inputHandler.getDown().isPressed()) {
			game.getTank().moveDown();
		} else if(inputHandler.getLeft().isPressed()) {
			game.getTank().moveLeft();
		} else if(inputHandler.getRight().isPressed()) {
			game.getTank().moveRight();
		} 
		if(inputHandler.getSpace().isPressed()) {
			inputHandler.getSpace().toggle(false);
			game.getTank().shoot();
		}
		
		ArrayList<Bullet> nonActiveBullets = game.getTank().getListOfNonActive();
		for(int i=0; i<nonActiveBullets.size(); i++){
			nonActiveBullets.get(i).move();
		}
		
		game.getTank().reloadBullet();
		
		
		/*if(Tank.shooted) {
			//game.getTank().reloadBullet();
			game.getTank().getBullet().move();
		}*/
	}
	
	/**
	 * output from the tick method
	 */
	public void render() {
		repaint();
	}
	
	public static void main(String [] args){
		Window g = new Window();
		g.start();
	}


}
