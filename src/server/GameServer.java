package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.tank.game.Game;
import com.tank.game.PlayerMP;

import packets.Packet;
import packets.Packet.PacketTypes;
import packets.PacketDisconnect;
import packets.PacketLogin;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			// String message = new String(packet.getData());
			//
			// if (message.trim().equalsIgnoreCase("ping")) {
			// System.out.println(
			// "CLIENT [" + packet.getAddress().getHostAddress() + ":" +
			// packet.getPort() + " ] > " + message);
			// sendData("pong".getBytes(), packet.getAddress(),
			// packet.getPort());
			// }
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
			packet = new PacketLogin(input[1], input[2]);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((PacketLogin) packet).getUsername()
					+ " has connected..");
			
			PlayerMP player = new PlayerMP(((PacketLogin) packet).getUsername(), ((PacketLogin) packet).getColor(),
					address, port);
			this.addConnection(player, (PacketLogin) packet);
			break;
		case DISCONNECT:
			packet = new PacketDisconnect(input[1],input[2]);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((PacketDisconnect) packet).getUsername()
					+ " has disconnect..");
			this.removeConnection((PacketDisconnect) packet);
			break;
		}
	}

	private void removeConnection(PacketDisconnect packet) {
		this.connectedPlayers.remove(getPlayerIndex(packet.getUsername()));
		packet.writeData(this);
		
	}
	
	public PlayerMP getPlayerMP(String username){
		for(PlayerMP player:this.connectedPlayers){
			if(player.getName().equals(username)){
				return player;
			}
		}
		return null;
	}
	public int getPlayerIndex(String username){
		int index = 0;
		for(PlayerMP p : connectedPlayers){
			if(p.getName().equals(username)){
				break;
			}
				index++;
			}
		return index;
	}

	public void addConnection(PlayerMP player, PacketLogin packet) {
		boolean alredyConnected = false;
		for (PlayerMP p : this.connectedPlayers) {
			if (player.getName().equalsIgnoreCase(p.getName())) {
				if (p.ipAddress == null) {
					System.out.println("checked");
					p.ipAddress = player.ipAddress;
				}
				if (p.port == 1) {
					p.port = player.port;
				}
				alredyConnected = true;
			} else {
				sendData(packet.getData(), p.ipAddress, p.port);

				packet = new PacketLogin(p.getName(), p.getColor());
				sendData(packet.getData(), player.ipAddress, player.port);
			}
		}
		if (!alredyConnected) {
			this.connectedPlayers.add(player);
		}

	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}

	}
}
