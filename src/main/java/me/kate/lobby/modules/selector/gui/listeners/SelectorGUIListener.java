package me.kate.lobby.modules.selector.gui.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kate.lobby.modules.selector.gui.GUI;

public class SelectorGUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {

		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		UUID playerUUID = player.getUniqueId();

		UUID inventoryUUID = GUI.openInventories.get(playerUUID);

		if (inventoryUUID != null) {
			event.setCancelled(true);
			GUI gui = GUI.getInventoriesByUUID().get(inventoryUUID);
			GUI.GUIAction action = gui.getActions().get(event.getSlot());

			if (action != null) {
				action.click(player);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {

		Player player = (Player) event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		GUI.openInventories.remove(playerUUID);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		GUI.openInventories.remove(playerUUID);
	}
}
