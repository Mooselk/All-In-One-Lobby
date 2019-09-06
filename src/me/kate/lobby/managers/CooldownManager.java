package me.kate.lobby.managers;

import java.util.UUID;

import me.kate.lobby.Main;

public class CooldownManager {

	public static final int DEFAULT_COOLDOWN = 3;

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
}
