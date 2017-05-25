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
import packets.PacketDisconnect;
import packets.PacketLogin;
import packets.PacketMove;
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
		System.out.println(message);
		PacketTypes type = Packet.lookupPacket(input[0]);
		Packet packet = null;
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			packet = new PacketLogin(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((PacketLogin) packet).getUsername()
					+ " has joined the game..");
			
			PlayerMP player = new PlayerMP(((PacketLogin) packet).getUsername(), ((PacketLogin) packet).getColor(),
					address, port);
			game.addPlayer(player);
			break;
		case DISCONNECT:
			packet = new PacketDisconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((PacketDisconnect) packet).getUsername()
					+ " has left this battle field..");
			game.removePlayerMP(((PacketDisconnect) packet).getUsername());
			break;
		case MOVE:
			packet = new PacketMove(input[1],input[2],Integer.parseInt(input[3]),Integer.parseInt(input[4]));
			handlePacket(((PacketMove) packet));
			break;
		}
	}
	
	private void handlePacket(PacketMove packetMove) {
		this.game.movePlayer(packetMove.getUsername(), packetMove.getX(), packetMove.getY());
		
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
