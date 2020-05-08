package me.kate.lobby.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;

public class SelectorUpdateTask extends Task {

	private Player player;
	private JavaPlugin plugin;
	
	private static final int DELAY = 5;
	
	public SelectorUpdateTask(Player player, JavaPlugin plugin) {
		this.player = player;
		this.plugin = plugin;
	}
	
	@Override
	public void start() {
		if (player == null) {
			return;
		}
		BukkitTask refreshTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			
		}, DELAY * 20, DELAY * 20);
		Main.getInstance().getTasks().put(player.getUniqueId(), refreshTimer);
	}

	@Override
	public void stop() {
	}
	
	public void stop(Player player) {	
		
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public UUID getTaskID() {
		return null;
	}

}
