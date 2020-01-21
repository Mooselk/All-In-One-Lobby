package me.kate.lobby.modules.selector.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.modules.selector.Selector;

public class SelectorClickEvent extends Selector implements Listener {
	
	private SelectorConfig config = new SelectorConfig();
	
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (player.getItemInHand().getType().equals(config.getSelectorMaterial())) {
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
