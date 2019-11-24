package me.kate.lobby.threads;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.ping.MineStat;

public class PingSelectorBackground extends Thread {

	private ISelectorSettings selectorFile = new SelectorConfig();
	private FileConfiguration SelectorConfig = selectorFile.getSelectorFile();

	@Override
	public void run() {
		while (true) {
			try {
				for (final String key : SelectorConfig.getConfigurationSection("selector").getKeys(false)) {
					final ConfigurationSection section = SelectorConfig.getConfigurationSection("selector." + key);
					if (!section.getBoolean("decoration") && !key.equals("options")) {
						try {
							Thread.sleep(2000); // default: 2000
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (!section.getBoolean("server.ping-server", false)) { continue; }

						String ip = section.getString("server.ip");
						int port = section.getInt("server.port");

						MineStat ms = new MineStat(ip, port, 1);
						Map<String, Object> serverInfo = new HashMap<>();
						String serverName = section.getString("server.server-id");
						serverInfo.put("isOnline", ms.isServerUp());
						if (ms.isServerUp()) {
							serverInfo.put("online", ms.getCurrentPlayers());
							serverInfo.put("max", ms.getMaximumPlayers());
							// serverInfo.put("ping", ms.getLatency());
						}
						Main.SELECTOR_PLACEHOLDERS.put(serverName, serverInfo);
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