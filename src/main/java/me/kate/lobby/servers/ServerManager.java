package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.ping.MineStat;

public class ServerManager {

	private NPCConfig npcConfig = new NPCConfig();
	private PluginConfig config = new PluginConfig();
	
	private Map<String, MineStat> serverInfo = new HashMap<>();

	/**
	 * @returns serverInfo map
	 */
	public Map<String, MineStat> getServerInfo() {
		return serverInfo;
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
			String address = section.getString("ip");
			int port = section.getInt("port");
			serverInfo.put(serverName, new MineStat(address, port));
		}
		this.loadNPCAssosiation();
	}

	/**
	 * For each NPC in the NPC config If live-player-count is true add the server
	 * associated with said NPC, used later to update text
	 */
	
	public void loadNPCAssosiation() {
		if (!Main.getInstance().getRegistry().getAssociation().isEmpty()) {
			Main.getInstance().getRegistry().getAssociation().clear();
		}
		if (npcConfig.getSection("npcs") != null) {
			for (final String name : npcConfig.get("npcs")) {
				final ConfigurationSection section = npcConfig.getSection("npcs." + name + ".server");
				if (section.getBoolean("live-player-count")) {
					String serverName = section.getString("server-name");
					String npcName = name;
					Main.getInstance().getRegistry().getAssociation().put(serverName, npcName);
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
	 * Gets specific server @returns MineStat object
	 */
	public MineStat getServer(String server) {
		return serverInfo.get(server);
	}
}
