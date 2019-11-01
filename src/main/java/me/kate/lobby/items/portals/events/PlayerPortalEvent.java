package me.kate.lobby.items.portals.events;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.items.portals.utils.Cuboid;
import me.kate.lobby.items.portals.utils.SendToServer;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.utils.Logger;

public class PlayerPortalEvent implements Listener {

	private final CooldownManager cooldownManager = new CooldownManager();

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
			if (this.isInCuboid(player)) {
			}
		}
	}

	private boolean isInCuboid(final Player player) {
		for (Map.Entry<String, Cuboid> entry : Main.PORTALS.entrySet()) {
			Cuboid cube = entry.getValue();
			if (cube.isIn(player)) {
				String key = entry.getKey();
				String server = PortalsConfig.portalconf.getString("portals." + key + ".server");
				int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
				if (timeLeft == 0) {
					cooldownManager.startCooldown(player, 3);
					SendToServer.send(player, server);
					Logger.debug("In portal " + key);
				}
				return true;
			}
		}
		return false;
	}
}
