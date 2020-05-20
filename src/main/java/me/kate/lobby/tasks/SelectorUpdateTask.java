package me.kate.lobby.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.utils.Logger;

public class SelectorUpdateTask implements Task {

	private Main plugin;
	
	private static final UUID taskID = UUID.fromString("eae0b0a8-d528-4b89-927f-e3dd05f244b5");
	private static final int DELAY = 2;
	
	public SelectorUpdateTask(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void start() {
		Logger.debug("Running");
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			plugin.getSelector().update();
		}, DELAY * 20, DELAY * 20);
		
		plugin.getTasks().put(taskID, refreshTimer);
	}

	@Override
	public void stop() {
		Logger.debug("Stopping");
		BukkitTask refreshTimer = Main.getInstance().getTasks().remove(taskID);
		if (refreshTimer != null) {
			refreshTimer.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return plugin.getTasks().containsKey(taskID);
	}

	@Override
	public UUID getTaskID() {
		return taskID;
	}

}
