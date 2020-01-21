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
		final ConfigurationSection section = config.getSection("selector." + slot);
		if (section == null) { return; } 
		if (event.getSlot() == slot) {
			if (!section.getBoolean("decoration") && isServerOnline(slot)) {
				if (!section.getString("message").equalsIgnoreCase("none")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', section.getString("message")));
				}
				if (!section.getString("connect").equalsIgnoreCase("none") && (!section.getString("connect").equalsIgnoreCase("menuclose"))) {
					SendToServer.send(player, section.getString("connect"));
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
