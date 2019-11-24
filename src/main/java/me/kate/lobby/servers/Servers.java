package me.kate.lobby.servers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.npcs.HoloTextHandler;
import me.kate.lobby.ping.MineStat;

public class Servers extends ServerManager {

	private final String taskUUID = "820c06aa-0e8a-11ea-8d71-362b9e155667";
	private HoloTextHandler holotext = new HoloTextHandler();
	private Map<String, String> countMap = new HashMap<>();
	
	private static final int DELAY = 3;
	
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
						if (!Main.PLACEHOLDERS.containsKey(server)) {
							if (minestat.isServerUp()) {
								countMap.put(server, minestat.getCurrentPlayers());
								serverInfo.put("max", minestat.getMaximumPlayers());
								serverInfo.put("online", minestat.getCurrentPlayers());
								Main.PLACEHOLDERS.put(server, serverInfo);
								System.out.println("PLACEHOLDERS does not contain " + server + ", adding.");
								sleep();
							} else continue;
						} else {
							String playerCount = countMap.get(server);
							if (playerCount.equals(minestat.getCurrentPlayers())) {
								System.out.println("Player count remains the same, skipping. (" + server + ": "+ playerCount + ")");
								continue;
							} else {
								countMap.remove(server);
								Main.PLACEHOLDERS.remove(server);
								serverInfo.put("online", minestat.getCurrentPlayers());
								serverInfo.put("max", minestat.getMaximumPlayers());
								Main.PLACEHOLDERS.put(server, serverInfo);
								countMap.put(server, minestat.getCurrentPlayers());
								System.out.println("Updating player count.(" + server + ": " + playerCount +")");
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
	
	public void sleep() {
		try {
			System.out.println("Sleeping...");
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}
	
	public Map<String, String> getCountMap() {
		return countMap;
	}
	
	public void startNPCTask() {
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
			for (Map.Entry<String, MineStat> str : getServerInfo().entrySet()) {
				String server = str.getKey();
				String name = Main.getRegistry().getAssociation().get(server);
				holotext.updateText(server, name);
			}
		}, DELAY * 20, DELAY * 20);
		Main.getInstance().getTasks().put(UUID.fromString(taskUUID), refreshTimer);
	}

	public void stopNPCTask() {
		BukkitTask bukkitTask = Main.getInstance().getTasks().remove(UUID.fromString(taskUUID));
		if (bukkitTask != null) { bukkitTask.cancel(); }
	}
}