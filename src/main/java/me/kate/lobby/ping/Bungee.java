package me.kate.lobby.ping;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Bungee implements PluginMessageListener {
	
	private static String server;
	private static int playerCount;
	
	@Override
	  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	      return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("SomeSubChannel")) {
	    	server = in.readUTF(); // Name of server, as given in the arguments
	    	playerCount = in.readInt();
	    }
	}
	
	public static int getPlayers(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		return playerCount;
	}
	
	// useless
	public static String getServer() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF("pvp");
		return server;
	}
}
