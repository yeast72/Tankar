package packets;

import server.GameClient;
import server.GameServer;

public class Packet04Reload extends Packet{

	private String username;
	private String color;

	public Packet04Reload(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(" ");
		this.username = dataArray[1];
		this.color = dataArray[2];
	}

	public Packet04Reload(String username, String color) {
		super(04);
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
		return ("04" + " " + this.username + " " + this.color).getBytes();
	}

	public String getUsername() {
		return username;
	}

	public String getColor() {
		return color;
	}

}
