package me.kate.lobby.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.HoloTextHandler;
import me.kate.lobby.servers.ServerManager;
import me.kate.lobby.utils.Utils;

public class NPCTask extends Task {

	private static final UUID taskID = UUID.fromString("820c06aa-0e8a-11ea-8d71-362b9e155667");
	private static final int DELAY = 3;

	private Config npcConfig = new NPCConfig();
	private HoloTextHandler holotext = new HoloTextHandler();

	private JavaPlugin plugin;

	public NPCTask(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void start() {
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if (npcConfig.getSection("npcs") == null) { return; }
			for (String server : ServerManager.getServers()) {
				String npcname = Utils.getValue(Main.getRegistry().getAssociation(), server);
				if (npcname == null) { continue; }
				holotext.updateText(server, npcname);
			}
		}, DELAY * 20, DELAY * 20);
		Main.getInstance().getTasks().put(taskID, refreshTimer);
	}

	@Override
	public void stop() {
		BukkitTask refreshTimer = Main.getInstance().getTasks().remove(taskID);
		if (refreshTimer != null) {
			refreshTimer.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return Main.getInstance().getTasks().containsKey(taskID);
	}

	@Override
	public UUID getTaskID() {
		return taskID;
	}
}
