package me.kate.lobby.modules.toggleplayers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PlayerSettingsConfig;

public class TogglePlayers implements Hideable {

	private Config playerSettings = new PlayerSettingsConfig();

	@Override
	public boolean isHidden(Player player) {
		return playerSettings.getConfig().getBoolean(player.getUniqueId().toString());
	}

	@Override
	public void hide(Player player, ConfigurationSection section) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(online);
		}
		section = playerSettings.getSection(player.getUniqueId().toString());
		section.set("hidden", false);
		playerSettings.save();
	}

	@Override
	public void unhide(Player player, ConfigurationSection section) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.showPlayer(online);
		}
		section = playerSettings.getSection(player.getUniqueId().toString());
		section.set("hidden", true);
		playerSettings.save();
	}

	@Override
	public void setHidden(boolean hide, Player player) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (hide)
				player.hidePlayer(online);
			if (!hide)
				player.showPlayer(online);
		}
	}
}
