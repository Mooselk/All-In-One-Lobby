package me.kate.lobby.items.selector.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.portals.utils.SendToServer;
import me.kate.lobby.items.selector.Selector;
import net.md_5.bungee.api.ChatColor;

public class SelectorGuiEvents implements Listener {

	private ISelectorSettings sf = new SelectorFile();
	private FileConfiguration c = sf.getSelectorFile();
	private final Selector selector = new Selector();

	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		ConfigurationSection sec = c.getConfigurationSection("selector." + slot);
		if (sec == null) {
			return;
		}
		if (e.getSlot() == slot) {
			if (!sec.getBoolean("decoration")) {
				if (!sec.getString("message").equalsIgnoreCase("none")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', sec.getString("message")));
				}
				if (!sec.getString("connect").equalsIgnoreCase("none")
						&& (!sec.getString("connect").equalsIgnoreCase("menuclose"))) {
					SendToServer.send(p, sec.getString("connect"));
				} else {
					selector.close(p);
				}
				selector.close(p);
			}
			if (!e.getInventory().getType().equals(InventoryType.PLAYER)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void closeInventory(final InventoryCloseEvent e) {
		final Player p = (Player) e.getPlayer();
		selector.onClose(p);
	}
}
