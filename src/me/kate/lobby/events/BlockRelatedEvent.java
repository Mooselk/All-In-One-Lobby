package me.kate.lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.md_5.bungee.api.ChatColor;

public class BlockRelatedEvent implements Listener {
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		final Player p = (Player) e.getPlayer();
		if (!e.getPlayer().isOp()) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.AQUA+ "You're not allowed to break blocks here!");
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		final Player p = (Player) e.getPlayer();
		if (!e.getPlayer().isOp()) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.AQUA+ "You're not allowed to place blocks here!");
		}
	}
}
