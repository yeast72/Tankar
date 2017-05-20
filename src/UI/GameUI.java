package UI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Game;


public class GameUI extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Tankar.io";
	
	private JFrame frame;
	//private JPanel background;
	
	public boolean running;
	public int tickCount = 0;
	
	private Graphics g;
	private URL tankURL;
	
	private Game game;
	
	public GameUI(){
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game = Game.getInstance();
		initComponents();
	}

	public synchronized void start() {
		running = true;
		game.start();
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}

	private void initComponents() {
//		background = new JPanel();		
//		background.setPreferredSize(new Dimension(width, height));
//		background.setLayout(null);
//		add(background);
//		drawTank(40,50);
//		drawTank(500,600);
	}
	
	public void drawTank(int x, int y){
		tankURL = this.getClass().getClassLoader().getResource("images/tank.png");
		RedTankUI t = new RedTankUI(new JLabel(new ImageIcon(tankURL)));
		t.setBounds(x, y, 100, 100);
		frame.add(t, BorderLayout.CENTER);
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
				System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void tick() {
		tickCount++;
		
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		BufferedImage img = null;
		try{
		    img = ImageIO.read( new File("/Users/Piromsurang/Documents/workspace-java/Tankar/src/images/tank.png"));
		}
		catch ( IOException exc ){
		}
		g.drawImage(img, 0, 0, null);
	
		g.dispose();
		bs.show();
		
	}
	
	public static void main(String [] args){
		GameUI g = new GameUI();
		g.start();
	}
	
}
