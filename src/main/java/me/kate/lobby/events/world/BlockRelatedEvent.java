package me.kate.lobby.events.world;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.kate.lobby.Main;
import net.md_5.bungee.api.ChatColor;

public class BlockRelatedEvent implements Listener {

	private FileConfiguration config = Main.getInstance().getConfig();

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
		final Player p = (Player) e.getPlayer();
		if (config.getConfigurationSection("options.build").getBoolean("disable-block-place")) {
			if (!p.hasPermission(config.getConfigurationSection("options.build").getString("break-bypass-permission"))
					|| !p.isOp()) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						config.getConfigurationSection("options.build").getString("block-break-msg")));
			}
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e) {
		final Player p = (Player) e.getPlayer();
		if (config.getConfigurationSection("options.build").getBoolean("disable-block-break")) {
			if (!p.hasPermission(config.getConfigurationSection("options.build").getString("place-bypass-permission"))
					|| !p.isOp()) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						config.getConfigurationSection("options.build").getString("block-place-msg")));
			}
		}
	}
}
