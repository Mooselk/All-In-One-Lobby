package me.kate.lobby.listeners.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Permissions;
import me.kate.lobby.data.files.PluginConfig;

public class BlockRelatedListener implements Listener {
	
	private PluginConfig config;
	
	public BlockRelatedListener(JavaPlugin plugin) {
		this.config = new PluginConfig();
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		final Player player = (Player) event.getPlayer();
		
		if (!config.blockBreakIsDisabled()) {
			return;
		}
		
		if (!Permissions.LOBBY_BUILD_BREAK.has(player)) {
			player.sendMessage(config.getBreakDenyMessage());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = (Player) event.getPlayer();
		
		if (!config.blockPlaceIsDisabled()) {
			return;
		}
		
		if (!Permissions.LOBBY_BUILD_PLACE.has(player)) {
			player.sendMessage(config.getPlaceDenyMessage());
			event.setCancelled(true);
		}
	}
}
