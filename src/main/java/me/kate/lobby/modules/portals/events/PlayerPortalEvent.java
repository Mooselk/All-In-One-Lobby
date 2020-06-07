package me.kate.lobby.modules.portals.events;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class PlayerPortalEvent implements Listener {
	
	private JavaPlugin plugin;
	private final CooldownManager cooldownManager = new CooldownManager(plugin);
	private Config portalConfig = new PortalsConfig();
	
	public PlayerPortalEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
			if (this.isInCuboid(player)) {
				// send message
			}
		}
	}

	private boolean isInCuboid(final Player player) {
		for (Map.Entry<String, Cuboid> entry : Main.getInstance().getPortals().entrySet()) {
			Cuboid cube = entry.getValue();
			if (cube.isIn(player)) {
				String key = entry.getKey();
				String server = portalConfig.getConfig().getString("portals." + key + ".server");
				int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
				if (timeLeft == 0) {
					cooldownManager.startCooldown(player, 3);
					Utils.send(player, server);
					Logger.debug("In portal " + key);
				}
				return true;
			}
		}
		return false;
	}
}
