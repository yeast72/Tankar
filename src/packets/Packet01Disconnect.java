package packets;

import server.GameClient;
import server.GameServer;

public class Packet01Disconnect extends Packet {

	private String username;
	private String color;

	public Packet01Disconnect(byte[] data) {
		super(01);
		String[] dataArray = readData(data).split(" ");
		this.username = dataArray[1];
		this.color = dataArray[2];
	}

	public Packet01Disconnect(String username, String color) {
		super(01);
		this.username = username;
		this.color = color;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());

	}

	@Override
	public byte[] getData() {
		return ("01" + " " + this.username + " " + this.color).getBytes();
	}

	public String getUsername() {
		return username;
	}

	public String getColor() {
		return color;
	}
}
