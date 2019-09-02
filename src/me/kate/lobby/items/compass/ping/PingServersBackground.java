package me.kate.lobby.items.compass.ping;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.items.compass.Compass;
import me.kate.lobby.utils.ServerPinger;
import me.kate.lobby.utils.ServerPinger.Server;

public class PingServersBackground extends Thread {

	private FileConfiguration config = Main.getInstance().getConfig();

	@Override
	public void run() {
		while (true) {
			try {
				for (final String key : config.getConfigurationSection("compass").getKeys(false)) {
					// Don't overload the CPU or the API
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					final ConfigurationSection section = config.getConfigurationSection("compass." + key);

					if (!section.getBoolean("server.ping-server", false)) {
						continue;
					}

					String ip = section.getString("server.ip");
					int port = section.getInt("server.port");

					Server server;

					if (Main.getInstance().getConfig().getBoolean("server.external-query", true)) {
						server = new ServerPinger.ExternalServer(ip, port);
					} else {
						int timeout = section.getInt("server.ping-timeout", 100);
						server = new ServerPinger.InternalServer(ip, port, timeout);
					}

					String serverName = section.getString("server.server-id");

					Map<String, Object> serverInfo = new HashMap<>();
					serverInfo.put("isOnline", server.isOnline());

					if (server.isOnline()) {
						serverInfo.put("online", server.getOnlinePlayers());
						serverInfo.put("max", server.getMaximumPlayers());
						
						serverInfo.put("ping", server.getResponseTimeMillis());
						Bukkit.getLogger().info("updating");
					}
					Main.SERVER_PLACEHOLDERS.put(serverName, serverInfo);
					Compass.update();
					//Compass.getPlayer().updateInventory();
					//Bukkit.getLogger().info("ServerInfo: " + Main.SERVER_PLACEHOLDERS);
				}
			} catch (Exception e) {
				// So the loop doesn't break if an error occurs
				// Print the error, sleep, try again.
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}