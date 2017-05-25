package packets;

import java.net.URL;

import server.GameClient;
import server.GameServer;

public class Packet00Login extends Packet{

	private String username;
	private String color;
	private int x;
	private int y;
	public Packet00Login(byte[] data){
		super(00);
		String[] dataArray = readData(data).split(" ");
		this.username = dataArray[1];
		this.color = dataArray[2];
		this.x = Integer.parseInt(dataArray[3]);
		this.y = Integer.parseInt(dataArray[4]);
	}
	public Packet00Login(String username,String color,int x , int y){
		super(00);
		this.username = username;
		this.color = color;
		this.x = x;
		this.y = y; 
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
		return ("00" +" " + this.username +" " + this.color + " " + x + " " + y).getBytes();
	}
	
	public String getUsername(){
		return username;
	}
	public String getColor(){
		return color;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
