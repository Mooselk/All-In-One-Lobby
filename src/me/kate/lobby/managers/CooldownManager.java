package me.kate.lobby.managers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;

public class CooldownManager {
	
	public static final int DEFAULT_COOLDOWN = 1;
	
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
	
	public void startCooldown(Player p, int cooldown) {
		if (cooldown == 0) {
			setCooldown(p.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
			Bukkit.getLogger().warning("[Lobby] Cooldown was 0, using default (1 seconds).");
		} else {
			setCooldown(p.getUniqueId(), cooldown);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				int timeLeft = getCooldown(p.getUniqueId());
				setCooldown(p.getUniqueId(), --timeLeft);
				if (timeLeft == 0) {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
	}
}
