package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Main;
import me.kate.lobby.modules.Items;

public class InventoryListener implements Listener {

	private Items items;
	
	public InventoryListener(Main plugin) {
		this.items = new Items(plugin);
	}
	// placeholder for config value
	boolean canMoveItems = true;
	
	@EventHandler
	public void onInventoryInteract(final InventoryClickEvent event) {
		final ItemStack item = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if (item == null) { 
			return; 
		}
		
		if (player.isOp()) { 
			return;
		}
		
		if (canMoveItems) {
			return;
		}
		
		if (item.equals(items.selector())) { 
			event.setCancelled(true); 
		}
		
		if (item.equals(items.hide())) {
			event.setCancelled(true);
		}
		
		if (item.equals(items.unHide())) {
			event.setCancelled(true); 	
		}
	}
}
