package me.kate.lobby.items.selector.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.selector.Selector;
import net.md_5.bungee.api.ChatColor;

public class SelectorGuiEvents implements Listener {

	private ISelectorSettings sf = new SelectorFile();
	private FileConfiguration c = sf.getSelectorFile();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		ConfigurationSection sec = c.getConfigurationSection("compass." + slot);
		if (sec == null) {
			return;
		}
		if (e.getSlot() == slot) {
			if (!sec.getBoolean("decoration")) {
				if (!sec.getString("message").equalsIgnoreCase("none")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', sec.getString("message")));
				}
				if (!sec.getString("command").equalsIgnoreCase("none")
						&& (!sec.getString("command").equalsIgnoreCase("menuclose"))) {
					Bukkit.dispatchCommand(p, sec.getString("command"));
				} else {
					Selector.close(p);
				}
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		final Player p = (Player) e.getPlayer();
		Selector.onClose(p);
		p.sendMessage("Closed inventory");
	}
}
