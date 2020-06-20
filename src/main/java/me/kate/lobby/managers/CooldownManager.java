package me.kate.lobby.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.utils.Logger;

public class CooldownManager {
	
	private static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
	
	private static final int DEFAULT_COOLDOWN = 1;
	
	private JavaPlugin plugin;
	private BukkitTask task;
	
	public CooldownManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setCooldown(UUID player, int time) {
		if (time < 1) {
			COOLDOWNS.remove(player);
		} else {
			COOLDOWNS.put(player, time);
		}
	}

	public int getCooldown(UUID player) {
		return COOLDOWNS.getOrDefault(player, 0);
	}
	
	public void startCooldown(Player player, int cooldown) {
		if (cooldown == 0) {
			setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
			Logger.warn("[Lobby] Cooldown was 0, using default (1 seconds).");
		} else {
			setCooldown(player.getUniqueId(), cooldown);
		}
		
		task = Bukkit.getScheduler().runTaskTimer(plugin,  () -> {
			
			int timeLeft = getCooldown(player.getUniqueId());
			setCooldown(player.getUniqueId(), --timeLeft);
			
			if (timeLeft == 0)
				task.cancel();
			
		}, 20, 20);
	}
}
