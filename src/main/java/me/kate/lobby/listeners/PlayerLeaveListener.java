package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.utils.Utils;

public class PlayerLeaveListener implements Listener {
	
	private JavaPlugin plugin;
	
	public PlayerLeaveListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (!plugin.getConfig().getString("options.custom-leavemsg").equals("none")) {
			event.setQuitMessage(Utils.replacePlayer(plugin.getConfig().getString("options.custom-leavemsg"), player));
		} else {
			event.setQuitMessage(null);
		}
	}
}
