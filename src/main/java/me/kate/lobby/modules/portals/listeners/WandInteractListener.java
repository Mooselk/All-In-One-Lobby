package me.kate.lobby.modules.portals.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.modules.portals.select.Selection;
import me.kate.lobby.modules.portals.select.SelectionVisualizer;
import me.kate.lobby.modules.portals.utils.PortalWand;
import me.kate.lobby.utils.Logger;

public class WandInteractListener implements Listener {

	private Main plugin;
	private Messages messages;
	private Selection selection;
	private SelectionVisualizer visualizer;

	public WandInteractListener(Main plugin) {
		this.plugin = plugin;
		this.messages = new Messages();
		this.selection = new Selection();
		this.visualizer = new SelectionVisualizer();
	}
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		
		if (!player.hasPermission("lobby.portal.create") || !player.isOp()) {
			return;
		}
		
		if (event.getItem() == null) {
			return;
		}
		
		if (!event.getItem().equals(PortalWand.WAND)) {
			return;
		}
		
		final UUID uuid = player.getUniqueId();
		
		selection = selection.getSelection(uuid);
		
		if (selection == null) {
			selection = new Selection(uuid);
		}
		
		visualizer = visualizer.getIntance(uuid);
		
		if (visualizer == null) {
			visualizer = new SelectionVisualizer(plugin, player);
		}
		
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			
			Location pos1 = event.getClickedBlock().getLocation();
			selection.setPos1(pos1);
			visualizer.setCornerA(pos1);
			
			messages.send("Position &6#1 &fset to (&6" 
			+ pos1.getBlockX() 
			+ ", " 
			+ pos1.getBlockY()
			+ ", " 
			+ pos1.getBlockZ() 
			+ "&f).", player);
			
			event.setCancelled(true);
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			Location pos2 = event.getClickedBlock().getLocation();
			selection.setPos2(pos2);
			visualizer.setCornerB(pos2);

			messages.send("Position &6#2 &fset to (&6" 
			+ pos2.getBlockX() 
			+ ", " 
			+ pos2.getBlockY() 
			+ ", " 
			+ pos2.getBlockZ() 
			+ "&f).", player);
			
			event.setCancelled(true);
		}
		
		if (visualizer.isVisualizing()) {
			visualizer.update();
		}
	}
	
	@EventHandler
	public void test(PlayerToggleSneakEvent event) {
		final Player player = event.getPlayer();
		
		Selection selections = null;
		
		if (selection.getSelection(player.getUniqueId()) != null) {
			selections = selection.getSelections().get(player.getUniqueId());
		}
		
		if (selections == null) {
			return;
		}
		
		if (!player.isSneaking()) {
			return;
		}
		
		if (selections.getPos1() != null) {
			Logger.info("Pos1 != null for player " + player.getName());
		}
		
		if (selections.getPos2() != null) {
			Logger.info("Pos2 != null for player " + player.getName());
		}
		
	}
}
