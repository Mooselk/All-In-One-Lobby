package me.kate.lobby.managers;

import java.util.UUID;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;

public class CooldownManager {

	private static IHidePlayerSettings hf = new HidePlayersFile();
	
	public static final int DEFAULT_COOLDOWN = hf.getCooldownLength();
	
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
