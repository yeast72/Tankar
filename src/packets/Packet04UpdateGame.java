package packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.tank.game.Game;

import server.GameClient;
import server.GameServer;

public class Packet04UpdateGame extends Packet{

	private Game game;
	
	public Packet04UpdateGame(byte[] data){
		super(04);
	}
	public Packet04UpdateGame(Game game){
		super(04);
		this.game = game;

	}
	@Override
	public void writeData(GameClient client) {
		System.out.println("test1");
		client.sendData(getData());
	}
	@Override
	public void writeData(GameServer server) {
		System.out.println("test2");
		server.sendDataToAllClients(getData());
	}
	@Override
	public byte[] getData() {
		ByteArrayOutputStream byteData = new ByteArrayOutputStream(1024);
		ObjectOutputStream obj = null;
		try {
			obj = new ObjectOutputStream(byteData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			obj.writeObject(this.game);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteData.toByteArray();
	}
	public Game getGame() {
		return this.game;
	}
	

}
