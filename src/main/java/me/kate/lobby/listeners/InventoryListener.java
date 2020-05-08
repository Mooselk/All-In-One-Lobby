package me.kate.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.modules.Items;

public class InventoryListener implements Listener {

	private Items items = new Items();
	
	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		final ItemStack item = event.getCurrentItem();
		if (item == null) { return; }
		if (item.equals(items.selector())) { event.setCancelled(true); }
		if (item.equals(items.hide())) { event.setCancelled(true); }
		if (item.equals(items.unHide())) { event.setCancelled(true); }
	}
}
