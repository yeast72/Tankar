package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.tank.game.Game;
import com.tank.game.PlayerMP;

import packets.Packet;
import packets.Packet01Disconnect;
import packets.Packet00Login;
import packets.Packet02Move;
import packets.Packet03Shoot;
import packets.Packet.PacketTypes;



public class GameClient extends Thread{
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game,String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		String[] input = message.split(" ");
		PacketTypes type = Packet.lookupPacket(input[0]);
		Packet packet = null;
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login)packet,address,port);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername()
					+ " has left this battle field..");
			game.removePlayerMP(((Packet01Disconnect) packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handlePacket(((Packet02Move) packet));
			break;
		case SHOOT:
			packet = new Packet03Shoot(data);
			handleShoot((Packet03Shoot) packet);
			break;
		}
	}
	
	private void handleShoot(Packet03Shoot packet) {
		this.game.playerShoot(packet.getUsername());
		
	}

	private void handlePacket(Packet02Move packetMove) {
		this.game.movePlayer(packetMove.getUsername(), packetMove.getX(), packetMove.getY(), packetMove.getDirection());
	}
	
	private void handleLogin(Packet00Login packetLogin,InetAddress address,int port){
		
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packetLogin.getUsername()
				+ " has joined the game..");
		
		PlayerMP player = new PlayerMP (packetLogin.getUsername(),packetLogin.getColor(),packetLogin.getX(),packetLogin.getY(),address,port);
		game.addPlayer(player);
	}

	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public InetAddress getIPAddress(){
		return this.ipAddress;
	}
}
