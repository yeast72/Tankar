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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import packets.Packet00Login;
import packets.Packet02Move;
import packets.Packet03Shoot;
import packets.Packet04UpdateGame;
import server.GameClient;
import server.GameServer;

public class Window extends JFrame implements Runnable {

	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;
	public static final int SCALE = 3;
	public static final int BORDER = 25;
	public static final String NAME = "Tankar.io";
	
	private JPanel drawPanel;
	private JDialog dialog;
	
	public boolean running;
	public int tickCount = 0;

	private Game game;
	private InputHandler inputHandler;
	public WindowHandler windowHandler;
	private Player player;
	
	public GameClient gameClientSocket;
	public GameServer gameServerSocket;
	
	public String playerName;
	public String playerColor;
	
	public Window() {
		super(NAME);

		game = Game.getInstance();
		inputHandler = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		
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
				setPreferredSize(new Dimension(Window.WIDTH, Window.HEIGHT));
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				drawBackground(g);
				drawCharacter(g);
			}

		};
		add(drawPanel);
	}

	private void createDialog() {
		JTextField username = new JTextField(10);
		dialog = new JDialog();
		dialog.setSize(new Dimension(500, 80));
		dialog.setTitle("Welcome to Tankar.io");
		dialog.setLayout(new FlowLayout());
		dialog.add(new JLabel("Username: "));
		dialog.add(username);
		dialog.add(new JLabel("Color: "));

		String[] color = { "Red", "Orange", "Green", "Gray", "Pink","Blue" };
		JComboBox colorChoice = new JComboBox(color);
		dialog.add(colorChoice);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerName = username.getText();
				playerColor = colorChoice.getSelectedItem().toString();
				
				player = new PlayerMP(playerName , playerColor , gameClientSocket.getIPAddress(),1);
				game.addPlayer(player);
				Packet00Login loginPacket = new Packet00Login(player.getName(),player.getColor(),player.getTank().getPositionX(),player.getTank().getPositionY());
				
				if(gameServerSocket != null){
					gameServerSocket.addConnection((PlayerMP)player, loginPacket);
				}
				loginPacket.writeData(gameClientSocket);
				
				dialog.setVisible(false);
			}
		});
		dialog.add(submit);
		dialog.setVisible(true);
	}

	private void drawBackground(Graphics g) {
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
		List<Player> allPlayer = game.getAllPlayers();
		if (!allPlayer.isEmpty()) {
			for (Player p : allPlayer) {
				g.drawImage(p.getTank().getImage().getImage(), p.getTank().getPositionX(), p.getTank().getPositionY(), null);
				ArrayList<Bullet> nonActiveBullets = p.getTank().getListOfNonActive();
				for (int i = 0; i < nonActiveBullets.size(); i++) {
					Bullet b = nonActiveBullets.get(i);
					if (b.isShooted()) {
						g.drawImage(b.getImage(), b.getPositionx(), b.getPositiony(), null);
					}
				}
			}
		}
	}

	public synchronized void start() {
		running = true;
		game.start();
		new Thread(this).start();
		
		if (JOptionPane.showConfirmDialog(this, "You wanna run server?") == 0) {
			gameServerSocket = new GameServer(game);
			gameServerSocket.start();
		}

		gameClientSocket = new GameClient(game, "localhost");
		
		gameClientSocket.start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
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
		List<Player> playerList = game.getAllPlayers();
		
		if (playerList.size() >= 1) {
			
			Player p = playerList.get(0);
			Packet02Move packet = new Packet02Move(p.getName(),p.getColor(),p.getTank().getPositionX(),p.getTank().getPositionY(),p.getTank().getDirection());
			packet.writeData(gameClientSocket);
			
			if (inputHandler.getUp().isPressed()) {
				p.getTank().moveUp();
			} else if (inputHandler.getDown().isPressed()) {
				p.getTank().moveDown();
			} else if (inputHandler.getLeft().isPressed()) {
				p.getTank().moveLeft();
			} else if (inputHandler.getRight().isPressed()) {
				p.getTank().moveRight();
			}
			if (inputHandler.getSpace().isPressed()) {
				inputHandler.getSpace().toggle(false);
				p.getTank().shoot();
				Packet03Shoot packetShoot = new Packet03Shoot(p.getName(),p.getColor());
				packetShoot.writeData(gameClientSocket);
			}
			if (inputHandler.getReload().isPressed() && !p.getTank().isReloading()) {
				inputHandler.getReload().toggle(false);
				p.getTank().reloadBullet();
			}
			for(Player enemy : playerList){
				if(playerList.size() == 1){
					updateBullet(p,null);
					enemy.increasingScore();
				}
				if(!enemy.equals(p)){
					updateBullet(p, enemy);
					enemy.increasingScore();
				}
			}
			
		}
	}
	
	public void updateBullet(Player p, Player enemy){
		p.getTank().checkIfRelaodingFinished();
		if(enemy != null)
			if(p.getTank().checkHitEnemy(enemy.getTank())){
				p.increasingScore();
				game.getAllPlayers().remove(enemy);
			}
		ArrayList<Bullet> nonActiveBullets = p.getTank().getListOfNonActive();
		for (int i = 0; i < nonActiveBullets.size(); i++) {
			if(enemy != null)
				nonActiveBullets.get(i).changeIsHitEnemyStatus(enemy.getTank().getPositionX(),
					enemy.getTank().getPositionY());
			nonActiveBullets.get(i).move();
		}
	}

	/**
	 * output from the tick method
	 */
	public void render() {
		repaint();
	}

	public static void main(String[] args) {
		Window g = new Window();
		g.start();
	}

}
