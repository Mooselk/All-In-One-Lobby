package me.kate.lobby.modules.selector.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.SelectorConfig;

public class SelectorInteractListener implements Listener {
	
	private SelectorConfig selectorConfig = new SelectorConfig();
	
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		
		if (player.getItemInHand().getType().equals(selectorConfig.getSelectorMaterial())) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.setCancelled(true);
				Main.getInstance().getSelector().update();
				Main.getInstance().getSelector().open(player);
			}
		}
	}
	
	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}
