package me.kate.lobby.items.compass.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.kate.lobby.Main;
import net.md_5.bungee.api.ChatColor;

public class GuiClickEvent implements Listener {

	private FileConfiguration c = Main.getInstance().getConfig();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
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
					p.closeInventory();
				}
			}
			e.setCancelled(true);
		}
	}
}
