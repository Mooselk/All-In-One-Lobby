package me.kate.lobby.modules.portals.listeners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class PlayerPortalListener implements Listener {
	
	private CooldownManager cooldownManager;
	private PortalsConfig portalConfig;
	private Portal portal;
	
	public PlayerPortalListener(Main plugin) {
		this.cooldownManager = new CooldownManager(plugin);
		this.portalConfig = new PortalsConfig();
		this.portal = new Portal();
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
			if (isInCuboid(player)) {
				// call custom event
			}
		}
	}

	private boolean isInCuboid(final Player player) {
		for (Map.Entry<String, Cuboid> entry : portal.getPortals().entrySet()) {
			Cuboid cube = entry.getValue();
			if (cube.isIn(player)) {
				String server = portalConfig.getServer(entry.getKey());
				int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
				if (timeLeft == 0) {
					cooldownManager.startCooldown(player, 3);
					Utils.send(player, server);
					Logger.debug("In portal " + entry.getKey());
				}
				return true;
			}
		}
		return false;
	}
}
