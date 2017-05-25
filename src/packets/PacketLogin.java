package packets;

import java.net.URL;

import server.GameClient;
import server.GameServer;

public class PacketLogin extends Packet{

	private String username;
	private String color;
	public PacketLogin(byte[] data){
		super(00);
		this.username = readData(data);
		this.color = readData(data);
	}
	public PacketLogin(String username,String color){
		super(00);
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
		return ("00" +"," + this.username +"," + this.color).getBytes();
	}
	
	public String getUsername(){
		return username;
	}
	public String getColor(){
		return color;
	}
}
