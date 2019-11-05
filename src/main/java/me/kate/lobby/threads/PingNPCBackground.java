package me.kate.lobby.threads;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.tasks.HoloTextTask;
import me.kate.lobby.utils.MineStat;

public class PingNPCBackground extends Thread {

	private HoloTextTask holo = new HoloTextTask();
	
	@Override
	public void run() {
		while (true) {
			try {
				if (!Bukkit.getOnlinePlayers().isEmpty()) {
					if (NPCConfig.getNPCConfig().getConfigurationSection("npcs") != null) {
						for (final String key : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
							final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + key + ".server");
							try {
								Thread.sleep(1000); // default: 2000
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (!section.getBoolean("live-player-count", false)) {
								continue;
							}
							String ip = section.getString("ip");
							int port = section.getInt("port");

							MineStat ms = new MineStat(ip, port, 1);
							Map<String, Object> serverInfo = new HashMap<>();

							String serverName = section.getString("server-name");
							serverInfo.put("isOnline", ms.isServerUp());
							if (ms.isServerUp()) {
								serverInfo.put("online", ms.getCurrentPlayers());
								Main.NPC_PLACEHOLDERS.put(serverName, serverInfo);
								holo.updateText(section.getString("server-name"), key);
							}
							System.out.println(Main.getRegistry().getNPCInfo());
						}
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
