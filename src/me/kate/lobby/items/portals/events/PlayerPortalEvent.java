package me.kate.lobby.items.portals.events;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.kate.lobby.Main;
import me.kate.lobby.items.portals.Cuboid;
import me.kate.lobby.npcs.api.utilities.Logger;

public class PlayerPortalEvent implements Listener {
	
	private Logger log = new Logger("[DEBUG]");

	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		final Location to = e.getTo();
		final Location from = e.getFrom();
		final double dif = 0.25;
		if ((int) from.getX() + dif != (int) to.getX() + dif 
				|| (int) from.getY() + dif != (int) to.getY() + dif
				|| (int) from.getZ() + dif != (int) to.getZ() + dif) {
			if (this.isInCuboid(p)) {
				log.info("Called on block X: " 
					+ ((int) p.getLocation().getX() + dif) + " Y: "
					+ ((int) p.getLocation().getBlockY() + dif) + " Z: " 
					+ ((int) p.getLocation().getZ() + dif));
			}
		}
	}

	private boolean isInCuboid(Player p) {
		for (Map.Entry<String, Cuboid> entry : Main.PORTALS.entrySet()) {
			final Cuboid c = entry.getValue();
			if (c.isIn(p)) {
				log.info("is in cuboid: " + entry.getKey());
				return true;
			}
		}
		return false;
	}
}
