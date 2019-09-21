package me.kate.lobby.items.portals.events;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsFile;
import me.kate.lobby.items.portals.Cuboid;
import me.kate.lobby.items.portals.SendToServer;
import me.kate.lobby.managers.CooldownManager;

public class PlayerPortalEvent implements Listener {

	private final CooldownManager cooldownManager = new CooldownManager();

	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		Location to = e.getTo();
		Location from = e.getFrom();

		if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
			if (this.isInCuboid(p)) {

			}
		}
	}

	private boolean isInCuboid(Player p) {
		for (Map.Entry<String, Cuboid> entry : Main.PORTALS.entrySet()) {
			Cuboid c = entry.getValue();
			if (c.isIn(p)) {
				String key = entry.getKey();
				String server = PortalsFile.portalconf.getString("portals." + key + ".server");
				int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
				if (timeLeft == 0) {
					cooldownManager.startCooldown(p, 3);
					SendToServer.send(p, server);
				}
				return true;
			}
		}
		return false;
	}
}
