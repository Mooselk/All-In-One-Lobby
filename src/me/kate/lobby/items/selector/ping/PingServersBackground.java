package me.kate.lobby.items.selector.ping;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.utils.MineStat;

public class PingServersBackground extends Thread {

	private final FileConfiguration config = Main.getInstance().getConfig();

	@Override
	public void run() {
		while (true) {
			try {
				for (final String key : config.getConfigurationSection("compass").getKeys(false)) {
					
					final ConfigurationSection sec = config.getConfigurationSection("compass." + key);
					
					if (!sec.getBoolean("decoration") && !key.equals("options")) {
						try {
							Thread.sleep(750); // default: 2000
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						final ConfigurationSection section = config.getConfigurationSection("compass." + key);
						
						if (!section.getBoolean("server.ping-server", false)) {
							continue;
						}
						
						String ip = section.getString("server.ip");
						int port = section.getInt("server.port");
						
						MineStat ms = new MineStat(ip, port, 1);
						Map<String, Object> serverInfo = new HashMap<>();
						String serverName = section.getString("server.server-id");
						
						serverInfo.put("isOnline", ms.isServerUp());
						if (ms.isServerUp()) {
							serverInfo.put("online", ms.getCurrentPlayers());
							serverInfo.put("max", ms.getMaximumPlayers());
							serverInfo.put("version", ms.getVersion());
							serverInfo.put("ping", ms.getLatency());
							Bukkit.getLogger().info("updating: " + key);
						}
						Main.SERVER_PLACEHOLDERS.put(serverName, serverInfo);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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