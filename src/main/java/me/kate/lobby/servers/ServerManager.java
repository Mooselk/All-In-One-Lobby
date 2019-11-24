package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.ping.MineStat;

public class ServerManager {
	
	private Map<String, MineStat> serverInfo = new HashMap<>();
	
	/**
	 * Get entire map
	 */
	public Map<String, MineStat> getServerInfo() {
		return serverInfo;
	}
	
	/**
	 * Puts server name and
	 * MineStat object into a map
	 * from configuration
	 */
	
	public void loadServers() {
		if (!serverInfo.isEmpty()) { this.clearServers(); }
		for (String key : Main.getInstance()
				.getConfig()
				.getConfigurationSection("servers.")
				.getKeys(false)) {
			final ConfigurationSection section = Main.getInstance()
					.getConfig()
					.getConfigurationSection("servers." + key);
			String serverName = key;
			String address = section.getString("ip");
			int port = section.getInt("port");
			serverInfo.put(serverName, new MineStat(address, port));
		}
		this.loadNPCAssosiation();
	}
	
	public void loadNPCAssosiation() {
		if (!Main.getRegistry().getAssociation().isEmpty()) { 
			Main.getRegistry().getAssociation().clear(); }
		if (NPCConfig
				.getNPCConfig()
				.getConfigurationSection("npcs") != null) {
			for (final String name : NPCConfig
					.getNPCConfig()
					.getConfigurationSection("npcs")
					.getKeys(false)) {
				final ConfigurationSection section = NPCConfig
						.getNPCConfig()
						.getConfigurationSection("npcs." + name + ".server");
				if (section.getBoolean("live-player-count")) {
					String serverName = section.getString("server-name");
					String npcName = name;
					Main.getRegistry().getAssociation().put(serverName, npcName);
				}
			}
		}
	}
	
	/**
	 * Clear map
	 */
	public void clearServers() {
		serverInfo.clear();
	}
	
	/**
	 * Get specific server
	 * returns MineStat object
	 */
	
	public MineStat getServer(String server) {
		return serverInfo.get(server);
	}
}
