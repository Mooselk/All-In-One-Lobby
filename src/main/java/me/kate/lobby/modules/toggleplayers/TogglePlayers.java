package me.kate.lobby.modules.toggleplayers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TogglePlayers implements Hideable {

	@Override
	public void hide(Player player, ConfigurationSection section) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(online);
		}
	}

	@Override
	public void unhide(Player player, ConfigurationSection section) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.showPlayer(online);
		}
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
