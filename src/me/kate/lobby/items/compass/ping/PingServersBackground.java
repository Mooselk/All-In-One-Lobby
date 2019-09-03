package me.kate.lobby.items.compass.ping;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.utils.MineStat;

public class PingServersBackground extends Thread {

	private FileConfiguration config = Main.getInstance().getConfig();

	@Override
	public void run() {
		while (true) {
			try {
				for (final String key : config.getConfigurationSection("compass").getKeys(false)) {
					final ConfigurationSection section1 = config.getConfigurationSection("compass." + key);
					if (!section1.getBoolean("decoration") && !key.equals("options")) {
						try {
							Thread.sleep(1000);
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

						String serverName = section.getString("server.server-id");

						Map<String, Object> serverInfo = new HashMap<>();

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