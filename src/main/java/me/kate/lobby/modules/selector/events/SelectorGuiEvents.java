package me.kate.lobby.modules.selector.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.kate.lobby.modules.portals.utils.SendToServer;
import me.kate.lobby.modules.selector.Selector;
import net.md_5.bungee.api.ChatColor;

public class SelectorGuiEvents extends Selector implements Listener {

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		final ConfigurationSection sec = getSelectorFile().getConfigurationSection("selector." + slot);
		if (sec == null) { return; } 
		if (event.getSlot() == slot) {
			if (!sec.getBoolean("decoration") && isServerOnline(slot)) {
				if (!sec.getString("message").equalsIgnoreCase("none")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', sec.getString("message")));
				}
				if (!sec.getString("connect").equalsIgnoreCase("none")
						&& (!sec.getString("connect").equalsIgnoreCase("menuclose"))) {
					SendToServer.send(player, sec.getString("connect"));
				} else {
					close(player);
				}
				close(player);
			}
			if (!event.getInventory().getType().equals(InventoryType.PLAYER)) {
				event.setCancelled(true);
			}
		}
	}
}
