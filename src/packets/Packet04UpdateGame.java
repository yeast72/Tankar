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
		client.sendData(getData());
	}
	@Override
	public void writeData(GameServer server) {
		System.out.println("test2");
		server.sendDataToAllClients(getData());
	}
	@Override
	public byte[] getData() {
		return ("04" + " " + this.game).getBytes();

	}
	public Game getGame() {
		return this.game;
	}
	

}
