package me.kate.lobby.items.portals.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.items.portals.PortalWand;
import net.md_5.bungee.api.ChatColor;

public class WandInteractEvent implements Listener {
	
	@EventHandler
	public void onClick(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.hasPermission("portal.create") || p.isOp()) {
			final ItemStack wand = PortalWand.WAND;
			if (e.getItem().equals(wand)) {
				if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					Location pos1 = e.getClickedBlock().getLocation();
					p.sendMessage("Position " 
					+ ChatColor.GOLD 
					+ "#1" 
					+ ChatColor.WHITE
					+ " set to ("
					+ ChatColor.GOLD 
					+ pos1.getBlockX()
					+ ", "
					+ pos1.getBlockY()
					+ ", "
					+ pos1.getBlockZ()
					+ ChatColor.WHITE
					+ ").");
					e.setCancelled(true);
				}
				if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Location pos2 = e.getClickedBlock().getLocation();
					p.sendMessage("Position "
					+ ChatColor.GOLD 
					+ "#2" 
					+ ChatColor.WHITE 
					+ " set to ("
					+ ChatColor.GOLD 
					+ pos2.getBlockX()
					+ ", "
					+ pos2.getBlockY()
					+ ", "
					+ pos2.getBlockZ()
					+ ChatColor.WHITE
					+ ").");
					e.setCancelled(true);
				}
			}
		}
	}
	// Hash map to store positions,
	// Store whole location,
	// On first select, check if it exists,
	// On reselect, check if exists, delete and add new,
	// /portal create wall add coords to config.
}
