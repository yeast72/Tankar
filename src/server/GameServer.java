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
import com.tank.game.Tank;

import packets.Packet;
import packets.Packet.PacketTypes;
import packets.Packet01Disconnect;
import packets.Packet02Move;
import packets.Packet00Login;
import packets.Packet03Shoot;

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
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login) packet).getUsername()
					+ " has connected..");

			PlayerMP player = new PlayerMP(((Packet00Login) packet).getUsername(), ((Packet00Login) packet).getColor(),
					address, port);
			this.addConnection(player, (Packet00Login) packet);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has disconnect..");
			this.removeConnection((Packet01Disconnect) packet);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			this.handleMove(((Packet02Move) packet));
			break;
		case SHOOT:
			packet = new Packet03Shoot(data);
			System.out.println(((Packet03Shoot) packet).getUsername() +" shoot");
			this.handleShoot((Packet03Shoot) packet);
			break;
		}
	}

	private void handleShoot(Packet03Shoot packet) {
		if(getPlayerMP(packet.getUsername()) != null){
			int index = getPlayerIndex(packet.getUsername());
			this.connectedPlayers.get(index).getTank().shoot();
			packet.writeData(this);
		}
		
	}

	private void handleMove(Packet02Move packetMove) {
		if(getPlayerMP(packetMove.getUsername()) != null){
			int index = getPlayerIndex(packetMove.getUsername());
			int tankPosX = packetMove.getX();
			int tankPosY = packetMove.getY();
			int direction = packetMove.getDirection();
			this.connectedPlayers.get(index).getTank().setPositionX(tankPosX);
			this.connectedPlayers.get(index).getTank().setPositionY(tankPosY);
			this.connectedPlayers.get(index).getTank().rotate(this.connectedPlayers.get(index).getTank().getDirection(), direction);
			packetMove.writeData(this);
		}
		
	}

	private void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerIndex(packet.getUsername()));
		packet.writeData(this);

	}

	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getName().equals(username)) {
				return player;
			}
		}
		return null;
	}

	public int getPlayerIndex(String username) {
		int index = 0;
		for (PlayerMP p : connectedPlayers) {
			if (p.getName().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
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
				Packet00Login packetC = new Packet00Login(p.getName(), p.getColor(),p.getTank().getPositionX(),p.getTank().getPositionY());
				sendData(packetC.getData(), player.ipAddress, player.port);
				
				sendData(packet.getData(), p.ipAddress, p.port);

				
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
