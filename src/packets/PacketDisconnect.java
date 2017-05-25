package packets;

import server.GameClient;
import server.GameServer;

public class PacketDisconnect extends Packet{
	
	private String username;
	private String color;
	
	public PacketDisconnect(byte[] data){
		super(01);
		this.username = readData(data);
		this.color = readData(data);
	}
	public PacketDisconnect(String username,String color){
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
		return ("01" +" " + this.username +" " + this.color).getBytes();
	}
	
	public String getUsername(){
		return username;
	}
	public String getColor(){
		return color;
	}
}
