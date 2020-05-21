package me.kate.lobby.tasks;

import java.util.Map;
import java.util.UUID;

import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

public interface Task {

	static final Map<UUID, BukkitTask> tasks = Maps.newHashMap();
	
	public static Map<UUID, BukkitTask> getTasks() {
		return tasks;
	}
	
	public void start();
	
	public void stop();
	
	public boolean isRunning();
	
	public UUID getTaskID();
	
}
