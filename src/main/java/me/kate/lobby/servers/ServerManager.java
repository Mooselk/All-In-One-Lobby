package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.ping.MineStat;

public class ServerManager {

	private NPCConfig npcConfig = new NPCConfig();
	private PluginConfig config = new PluginConfig();

	private static Map<String, MineStat> serverInfo = new HashMap<>();
	
	private static Set<String> servers = new HashSet<>();
	
	private static Map<String, String> addresses = new HashMap<>();

	/**
	 * ServerName, MineStat
	 * 
	 * @returns serverInfo map
	 */
	public static Map<String, MineStat> getServerInfo() {
		return serverInfo;
	}
	
	/**
	 * ServerName, Address (ip:port)
	 * 
	 * @returns serverInfo map
	 */
	
	public static Map<String, String> getServerAddress() {
		return addresses;
	}
	
	/**
	 * ServerNames
	 * 
	 * @returns servers set
	 */
	
	public static Set<String> getServers() {
		return servers;
	}

	/**
	 * Puts server name and MineStat object into a map from configuration
	 */

	public void loadServers() {
		if (!serverInfo.isEmpty()) {
			this.clearServers();
		}
		for (String key : config.get("servers.")) {
			final ConfigurationSection section = config.getSection("servers." + key);
			String serverName = key;
			String ip = section.getString("ip");
			int port = section.getInt("port");
			String address = ip + ":" + port;
			servers.add(serverName);
			addresses.put(serverName, address);
		}
		this.loadNPCAssosiation();
	}

	/**
	 * For each NPC in the NPC config If live-player-count is true add the server
	 * associated with said NPC, used later to update text
	 */

	public void loadNPCAssosiation() {
		if (!Main.getRegistry().getAssociation().isEmpty()) {
			Main.getRegistry().getAssociation().clear();
		}
		if (npcConfig.getSection("npcs") == null) {
			return;
		}
		for (final String name : npcConfig.get("npcs")) {
			final ConfigurationSection section = npcConfig.getSection("npcs." + name + ".server");
			if (section.getBoolean("live-player-count")) {
				String serverName = section.getString("server-name");
				String npcName = name;
				Main.getRegistry().getAssociation().put(npcName, serverName);
			}
		}
	}

	public void loadNPCAssosiation(String npc) {
		if (npcConfig.getSection("npcs") == null) {
			return;
		}
		final ConfigurationSection section = npcConfig.getSection("npcs." + npc + ".server");
		if (section.getBoolean("live-player-count")) {
			String serverName = section.getString("server-name");
			String npcName = npc;
			if (Main.getRegistry().getAssociation().containsKey(npcName)) {
				Main.getRegistry().getAssociation().remove(npcName);
			}
			Main.getRegistry().getAssociation().put(npcName, serverName);
		}
	}

	/**
	 * Clear map
	 */
	public void clearServers() {
		serverInfo.clear();
	}

	/**
	 * Gets specific server @returns MineStat object
	 */
	public MineStat getServer(String server) {
		return serverInfo.get(server);
	}
}
