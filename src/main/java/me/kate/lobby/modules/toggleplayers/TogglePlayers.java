package me.kate.lobby.modules.toggleplayers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;

public class TogglePlayers implements Hideable {

	private IPlayerSettings playerSettings = new PlayerSettingsConfig();
	
	private boolean hidden;

	public TogglePlayers() {}

	@Override
	public boolean isHidden(Player p) {
		// TO-DO get players setting
		return hidden;
	}

	@Override
	public void hide(Player player, ConfigurationSection section) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(online);
		}
		section = playerSettings.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
		playerSettings.save();
		section.set("hidden", false);
	}

	@Override
	public void unhide(Player player, ConfigurationSection section) {
		section = null;
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.showPlayer(online);
		}
		section = playerSettings.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
		playerSettings.save();
		section.set("hidden", true);
	}

	@Override
	public void setHidden(boolean hide, Player player) {
		if (hide) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				player.hidePlayer(online);
			}
		}
		if (!hide) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				player.showPlayer(online);
			}
		}
	}
}
