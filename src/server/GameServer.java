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
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
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
		String[] input = message.split(",");
		System.out.println(message);
		PacketTypes type = Packet.lookupPacket(input[0]);
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			PacketLogin packet = new PacketLogin(input[1],input[2]);
			System.out.println(
					"[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has connected..");
			PlayerMP player = new PlayerMP(packet.getUsername(), packet.getColor(), address, port);
			if(player != null) {
				this.connectedPlayers.add(player);
				game.addPlayer(player);
			}
			break;
		case DISCONNECT:
			break;
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
