package me.kate.lobby.tasks.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.kate.lobby.Main;
import me.kate.lobby.servers.ServerManager;
import me.kate.lobby.utils.Logger;

public class BungeeMessenger extends ServerManager implements PluginMessageListener {
	
	public static void getPlayerCounts() {
		if (Bukkit.getOnlinePlayers().isEmpty()) {
			return;
		}
		
		for (String server : getServers()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PlayerCount");
			out.writeUTF(server);
			Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
		}
	}

	public static void getPlayerCountFor(String server) {
		if (Bukkit.getOnlinePlayers().isEmpty()) {
			return;
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		int playerCount = 0;
		String serverName = "null";
		String subChannel = "null";
		try {
			subChannel = in.readUTF();
			
			if (subChannel.equals("PlayerCount")) {
				serverName = in.readUTF();
				playerCount = in.readInt();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> serverInfo = new HashMap<>();
		// Must be final or effectively final my ass
		String count = String.valueOf(playerCount);
		String server = serverName;
		if (getServerAddress().get(serverName) == null) {
			Logger.debug("ServerName " + serverName + " is null in server address hashmap");
			Logger.debug("WHERE 'ALL' COMMING FROM");
			return;
		}
		String[] address = getServerAddress().get(serverName).split(":");
		HeartBeat.fetchServerStatusFromAsync(address[0], Integer.valueOf(address[1]), status -> {
			//System.out.println("Server " + server + " is " + status.toString());
				serverInfo.put("isOnline", status.equals(ServerStatus.ONLINE));
				serverInfo.put("online", String.valueOf(count));
				//System.out.println("ServerInfo" + serverInfo);
				Main.getInstance().getPlaceholders().put(server, serverInfo);
				Logger.debug("PlaceHolders- " + server + ": " + Main.getInstance().getPlaceholders());
				
			}
		);
		//System.out.println("Placeholders outside heartbeat: " + Main.getInstance().getPlaceholders());
	}
	
}
