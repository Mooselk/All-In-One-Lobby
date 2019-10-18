package me.kate.lobby.items.selector.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.selector.Selector;

public class SelectorClickEvent implements Listener {

	private ISelectorSettings sf = new SelectorFile();
	private FileConfiguration config = sf.getSelectorFile();
	
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand().getType().equals(Material.getMaterial(config.getConfigurationSection("selector.options").getString("material")))) {
			final Selector compass = new Selector();
			compass.open(p);
		}
	}
	
	@EventHandler
	public void onDrop(final PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
}
