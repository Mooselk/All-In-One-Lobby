package me.kate.lobby.modules;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PluginConfig;

public class PlayerPotionEffects {
	
	private Config mainConfig;
	private List<String> effects;
	
	public PlayerPotionEffects(Main plugin) {
		this.mainConfig = new PluginConfig(plugin);
		this.effects = mainConfig.getConfig().getStringList("options.join-effects");
	}
	
	public void addEffect(Player player) {
		for (String effect : effects) {
			String[] splitEffects = effect.split(":");
			removeEffects(player, splitEffects[0]);
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.getByName(splitEffects[0]), 
					isMaxLength(splitEffects[1]),
					getAmplifier(splitEffects[2]), true, false));
		}
	}
	
	private void removeEffects(Player player, String effect) {
		if (hasEffect(player, effect)) {
			player.removePotionEffect(PotionEffectType.getByName(effect));
		}
	}
	
	private boolean hasEffect(Player player, String effect) {
		return player.hasPotionEffect(PotionEffectType.getByName(effect));
	}
	
	private int isMaxLength(String length) {
		if (length.equalsIgnoreCase("max")) {
			return Integer.MAX_VALUE;
		} else {
			return Integer.valueOf(length);
		}
	}
	
	private int getAmplifier(String amplifer) {
		return Integer.valueOf(amplifer) - 1;
	}
}
