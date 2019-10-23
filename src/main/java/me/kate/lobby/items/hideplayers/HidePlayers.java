package me.kate.lobby.items.hideplayers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.items.hideplayers.interfaces.Hideable;

public class HidePlayers implements Hideable {

	private IPlayerSettings ps = new PlayerSettingsFile();
	
	private boolean hidden;

	public HidePlayers() {

	}

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
		section = ps.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
		ps.save();
		section.set("hidden", true);
	}

	@Override
	public void unhide(Player player, ConfigurationSection section) {
		section = null;
		for (Player online : Bukkit.getOnlinePlayers()) {
			player.showPlayer(online);
		}
		section = ps.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
		ps.save();
		section.set("hidden", false);
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
