package me.kate.lobby.items.selector.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.portals.utils.SendToServer;
import me.kate.lobby.items.selector.Selector;
import net.md_5.bungee.api.ChatColor;

public class SelectorGuiEvents implements Listener {

	private ISelectorSettings selectorFile = new SelectorConfig();
	private FileConfiguration config = selectorFile.getSelectorFile();
	private final Selector selector = new Selector();

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		ConfigurationSection sec = config.getConfigurationSection("selector." + slot);
		if (sec == null) {
			return;
		}
		if (event.getSlot() == slot) {
			if (!sec.getBoolean("decoration") && selector.isServerOnline(slot)) {
				if (!sec.getString("message").equalsIgnoreCase("none")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', sec.getString("message")));
				}
				if (!sec.getString("connect").equalsIgnoreCase("none")
						&& (!sec.getString("connect").equalsIgnoreCase("menuclose"))) {
					SendToServer.send(player, sec.getString("connect"));
				} else {
					selector.close(player);
				}
				selector.close(player);
			}
			if (!event.getInventory().getType().equals(InventoryType.PLAYER)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void closeInventory(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		selector.onClose(player);
	}
}
