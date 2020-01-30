package me.kate.lobby.modules.portals.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Messages;
import me.kate.lobby.modules.portals.PortalLocation;
import me.kate.lobby.modules.portals.select.Position;
import me.kate.lobby.modules.portals.select.Selections;
import me.kate.lobby.modules.portals.utils.PortalWand;

public class WandInteractEvent extends Selections implements Listener {

	@EventHandler
	public void onClick(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (player.hasPermission("lobby.portal.create") || player.isOp()) {
			final ItemStack wand = PortalWand.WAND;
			if (event.getItem() == null) { return; }
			if (event.getItem().equals(wand)) {
				
				if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					Location pos1 = event.getClickedBlock().getLocation();

					select(player, Position.POS1, new PortalLocation(pos1, Position.POS1).toPortal());
					
					Messages.send("Position &6#1 &fset to (&6" + pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ() + "&f).", player);
					event.setCancelled(true);
				}
				
				if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Location pos2 = event.getClickedBlock().getLocation();
					
					select(player, Position.POS2, new PortalLocation(pos2, Position.POS2).toPortal());
					
					Messages.send("Position &6#2 &fset to (&6" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", " + pos2.getBlockZ() + "&f).", player);
					event.setCancelled(true);
				}
			}
		}
	}
}
