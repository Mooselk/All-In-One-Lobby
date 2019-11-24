package me.kate.lobby.threads;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.HoloTextHandler;
import me.kate.lobby.ping.MineStat;

public class PingNPCBackground extends Thread {

	private HoloTextHandler holo = new HoloTextHandler();
	private Map<String, MineStat> mineMap = new HashMap<>();
	
	@Override
	public void run() {
		while (true) {
			try {
				if (NPCConfig.getNPCConfig().getConfigurationSection("npcs") != null) {
					for (final String key : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
						final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + key + ".server");
						
						if (section.getBoolean("live-player-count", true)) {
							if (!mineMap.containsKey(key)) {
								String ip = section.getString("ip");
								int port = section.getInt("port");
								MineStat ms = new MineStat(ip, port, 1);
								mineMap.put(key, ms);
								System.out.print("[DEBUG] Map 'minestat' contains: " + mineMap);
							} else {
								
								
								Map<String, Object> serverInfo = null;
								serverInfo = new HashMap<>();
								MineStat ms = mineMap.get(key);
								String serverName = section.getString("server-name");
								ms.refresh();
								
								
								serverInfo.put("isOnline", ms.isServerUp());
								if (ms.isServerUp()) {
									if (Main.NPC_PLACEHOLDERS.containsKey(serverName)) {
										Main.NPC_PLACEHOLDERS.remove(serverName);
									}
									
									
									serverInfo.put("online", ms.getCurrentPlayers());
									Main.NPC_PLACEHOLDERS.put(serverName, serverInfo);
									holo.updateText(section.getString("server-name"), key);
								} else {
									if (Main.NPC_PLACEHOLDERS.containsKey(serverName)) {
										Main.NPC_PLACEHOLDERS.remove(serverName);
									}
									continue;
								}
								
								
								
							}
						}
					}
				}
				try {
					Thread.sleep(2000); // default: 2000
				} catch (InterruptedException e) {
					e.printStackTrace();
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
