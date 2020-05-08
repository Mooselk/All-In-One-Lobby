package me.kate.lobby.tasks.bungee;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.tasks.Task;

public class BungeePingTask extends Task {

	private Main plugin;
	
	private static final UUID taskID = UUID.fromString("f190e378-e155-445d-ac0d-30d8f572d633");
	private static final int DELAY = 5;
	
	public BungeePingTask(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void start() {
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			BungeeMessenger.getPlayerCounts();
		}, DELAY * 20, DELAY * 20);
		Main.getInstance().getTasks().put(taskID, refreshTimer);
	}

	@Override
	public void stop() {
		BukkitTask bukkitTask = Main.getInstance().getTasks().remove(taskID);
		if (bukkitTask != null) {
			bukkitTask.cancel();
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
