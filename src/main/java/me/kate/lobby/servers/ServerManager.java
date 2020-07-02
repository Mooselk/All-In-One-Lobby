package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.google.common.collect.Maps;

import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.objects.Server;

public class ServerManager {
	
	private PluginConfig config = new PluginConfig();
	
	private static Set<String> servers = new HashSet<>();
	
	private static Map<String, String> addresses = new HashMap<>();
	
	public static final Map<String, Server> SERVER = Maps.newHashMap();
	
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
	
	public void loadServers() {
		config.get("servers.").forEach(key -> {
			final ConfigurationSection section = config.getSection("servers." + key);
			String serverName = key;
			String ip = section.getString("ip");
			int port = section.getInt("port");
			String address = ip + ":" + port;
			servers.add(serverName);
			addresses.put(serverName, address);
			
			SERVER.put(serverName, new Server(serverName, ip, port));
		});
	}
}
