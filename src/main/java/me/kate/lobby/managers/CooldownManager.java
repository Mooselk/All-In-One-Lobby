package me.kate.lobby.managers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;

public class CooldownManager {
	
	public static final int DEFAULT_COOLDOWN = 1;
	
	private JavaPlugin plugin;
	
	public CooldownManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setCooldown(UUID player, int time) {
		if (time < 1) {
			Main.COOLDOWNS.remove(player);
		} else {
			Main.COOLDOWNS.put(player, time);
		}
	}

	public int getCooldown(UUID player) {
		return Main.COOLDOWNS.getOrDefault(player, 0);
	}
	
	public void startCooldown(Player player, int cooldown) {
		if (cooldown == 0) {
			setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
			Bukkit.getLogger().warning("[Lobby] Cooldown was 0, using default (1 seconds).");
		} else {
			setCooldown(player.getUniqueId(), cooldown);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				int timeLeft = getCooldown(player.getUniqueId());
				setCooldown(player.getUniqueId(), --timeLeft);
				if (timeLeft == 0) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 20, 20);
	}
}
