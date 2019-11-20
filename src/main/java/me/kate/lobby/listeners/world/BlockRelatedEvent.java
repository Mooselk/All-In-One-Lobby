package me.kate.lobby.listeners.world;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;

public class BlockRelatedEvent implements Listener {

	private FileConfiguration config = Main.getInstance().getConfig();
	private final Messages msgs = new Messages();

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		final Player player = (Player) event.getPlayer();
		if (config.getConfigurationSection("options.build").getBoolean("disable-block-place")) {
			if (!player.hasPermission(config.getConfigurationSection("options.build").getString("break-bypass-permission")) || !player.isOp()) {
				msgs.send(config.getConfigurationSection("options.build").getString("block-break-msg"), player);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = (Player) event.getPlayer();
		if (config.getConfigurationSection("options.build").getBoolean("disable-block-break")) {
			if (!player.hasPermission(config.getConfigurationSection("options.build").getString("place-bypass-permission")) || !player.isOp()) {
				msgs.send(config.getConfigurationSection("options.build").getString("block-place-msg"), player);
				event.setCancelled(true);
			}
		}
	}
}
