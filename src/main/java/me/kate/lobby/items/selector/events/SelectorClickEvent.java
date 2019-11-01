package me.kate.lobby.items.selector.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.selector.Selector;

public class SelectorClickEvent implements Listener {

	private ISelectorSettings selectorFile = new SelectorConfig();
	private FileConfiguration config = selectorFile.getSelectorFile();
	
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (player.getItemInHand().getType().equals(Material.getMaterial(config.getConfigurationSection("selector.options").getString("material")))) {
			final Selector compass = new Selector();
			compass.open(player);
		}
	}
	
	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}
