package me.kate.lobby.modules.selector.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.modules.selector.Selector;

public class SelectorClickEvent extends Selector implements Listener {

	private ISelectorSettings selector = new SelectorConfig();
	
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (player.getItemInHand().getType().equals(
				Material.getMaterial(selector.getSelectorFile().getConfigurationSection("selector.options").getString("material")))) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.setCancelled(true);
				open(player);
			}
		}
	}
	
	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}
