package me.kate.lobby.items.selector.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.items.selector.Selector;

public class SelectorClickEvent implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType().equals(Material.COMPASS)) {
			final Selector compass = new Selector(p);
			compass.open();
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (p.getInventory().contains(Material.COMPASS)) {
			ItemStack is = e.getCurrentItem();
			if (is == null) {
				return;
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
}