package me.kate.lobby.tasks.bungee;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.tasks.Task;

public class BungeePingTask implements Task {

	private Main plugin;
	
	private static final UUID taskID = UUID.fromString("f190e378-e155-445d-ac0d-30d8f572d633");
	private static final int DELAY = 5;
	
	public BungeePingTask(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void start() {
		Task.getTasks().put(taskID, Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			BungeeMessenger.getPlayerCounts();
		}, DELAY * 20, DELAY * 20));
	}

	@Override
	public void stop() {
		BukkitTask bukkitTask = Task.getTasks().remove(taskID);
		if (bukkitTask != null) {
			bukkitTask.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return Task.getTasks().containsKey(taskID);
	}

	@Override
	public UUID getTaskID() {
		return taskID;
	}

}
