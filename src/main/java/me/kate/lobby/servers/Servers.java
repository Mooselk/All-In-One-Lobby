package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.HoloTextHandler;
import me.kate.lobby.ping.MineStat;
import me.kate.lobby.utils.Logger;

public class Servers extends ServerManager {

	private static final String taskUUID = "820c06aa-0e8a-11ea-8d71-362b9e155667";
	private HoloTextHandler holotext = new HoloTextHandler();
	private Map<String, String> countMap = new HashMap<>();

	private Config npcConfig = new NPCConfig();
	
	private static final int DELAY = 3;

	private JavaPlugin plugin;
	
	public Servers(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public Map<String, String> getCountMap() {
		return countMap;
	}
	
	public void getCountAsync() {
		new Thread(() -> {
			while (true) {
				try {
					for (Map.Entry<String, MineStat> str : getServerInfo().entrySet()) {
						String server = str.getKey();
						MineStat minestat = str.getValue();
						Map<String, Object> serverInfo = null;
						serverInfo = new HashMap<>();
						minestat.refresh();
						serverInfo.put("isOnline", minestat.isServerUp());
						// if (Main.DEBUG) { System.out.println("Placeholders " + Main.getInstance().getPlaceholders()); }
						if (!Main.getInstance().getPlaceholders().containsKey(server)) {
							if (minestat.isServerUp()) {
								countMap.put(server, minestat.getCurrentPlayers());
								serverInfo.put("max", minestat.getMaximumPlayers());
								serverInfo.put("online", minestat.getCurrentPlayers());
								Main.getInstance().getPlaceholders().put(server, serverInfo);
								// if (Main.DEBUG) { System.out.println("PLACEHOLDERS does not contain " + server + ", adding."); }
								sleep();
							} else continue;
						} else {
							String playerCount = countMap.get(server);
							if (playerCount.equals(minestat.getCurrentPlayers())) {
								// if (Main.DEBUG) { System.out.println("Player count remains the same, skipping. (" + server + ": " + playerCount + ")"); }
								continue;
							} else {
								countMap.remove(server);
								Main.getInstance().getPlaceholders().remove(server);
								serverInfo.put("online", minestat.getCurrentPlayers());
								serverInfo.put("max", minestat.getMaximumPlayers());
								Main.getInstance().getPlaceholders().put(server, serverInfo);
								countMap.put(server, minestat.getCurrentPlayers());
								// if (Main.DEBUG) { System.out.println("Updating player count.(" + server + ": " + playerCount + ")"); }
								sleep();
							}
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					// Print stack trace, sleep for 5 seconds
					// And try again
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void sleep() {
		try {
			if (Main.DEBUG) { System.out.println("Sleeping..."); }
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}

	public void startNPCTask() {
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			Logger.debug("NPC task running");
			if (npcConfig.getConfig().getConfigurationSection("npcs") != null) {
				for (Map.Entry<String, MineStat> str : getServerInfo().entrySet()) {
					String server = str.getKey();
					String name = Main.getInstance().getRegistry().getAssociation().get(server);
					// Check if name is not equal to null
					// in case there are more servers than NPCS
					if (name != null) { holotext.updateText(server, name); }
				}
			}
		}, DELAY * 20, DELAY * 20);
		Main.getInstance().getTasks().put(UUID.fromString(taskUUID), refreshTimer);
	}

	public void stopNPCTask() {
		BukkitTask bukkitTask = Main.getInstance().getTasks().remove(UUID.fromString(taskUUID));
		if (bukkitTask != null) {
			bukkitTask.cancel();
		}
	}
}