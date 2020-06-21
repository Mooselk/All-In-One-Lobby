package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.Permissions;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.select.Selection;
import me.kate.lobby.modules.portals.select.SelectionVisualizer;
import me.kate.lobby.modules.portals.utils.PortalWand;
import me.kate.lobby.utils.Logger;

public class PortalCommand implements CommandExecutor {

	private Main plugin;
	private PortalsConfig portalConfig;
	private Portal portal;
	private Messages messages;
	private Selection selections;

	public PortalCommand(Main plugin) {
		this.plugin = plugin;
		this.portalConfig = new PortalsConfig();
		this.portal = new Portal();
		this.messages = new Messages();
		this.selections = new Selection();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		
		Selection selection = null;
		SelectionVisualizer visualizer = null;
		
		if (!cmd.getName().equalsIgnoreCase("portal")) {
			return true;
		}
		
		if (args.length == 0) {
			messages.portalHelp(sender);
			return true;
		}
		
		if (player != null) {
			selection = selections.getSelection(player.getUniqueId());
			visualizer = new SelectionVisualizer(plugin, player);
		}
		
		switch(args[0]) {
		
		case "help" : {
			
			if (!Permissions.PORTAL_HELP.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			messages.portalHelp(sender);
			return true;
		}
		
		case "wand" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (!Permissions.PORTAL_HELP.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			messages.send("&f[&6Portal&f] &5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2", sender);
			player.getInventory().addItem(PortalWand.WAND);
			
			return true;
		}
		
		case "create" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (!Permissions.PORTAL_CREATE.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (args.length < 3) {
				messages.send("&6Usage: &f/portal create <portal_name> [server]", player);
				break;
			}
			
			if (args.length == 3) {
				
				String name = args[1];
				String server = args[2];
				String world = player.getWorld().getName();

				if (selection != null 
						&& selection.getPos1() != null 
						&& selection.getPos2() != null) {

					Logger.debug("Selection 1: "
					+ selection.getPos1()
					+ " Selection 2: "
					+ selection.getPos2());
					
					Logger.debug("Location 1: " 
					+ selection.getPos1() 
					+ " Location 2: " 
					+ selection.getPos2());

					portal.create(selection.getPos1(), selection.getPos2(), name, world, server, player);
					messages.send("&f[&6Portal&f] Created portal '" + name + "'.", player);
					break;
				}
				
				messages.send("&f[&6Portal&f] Create a selection before creating a portal.", player);
			}
			return true;
		}
		
		case "delete" : {
			
			if (!Permissions.PORTAL_DELETE.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (args.length < 2) {
				messages.send("&6Usage: &f/portal delete <portal_name>", sender);
				break;
			}
			
			if (args.length == 2) {
				String name = args[1];
				
				if (portalConfig.getPortal(name) == null) {
					messages.send("&f[&6Portal&f] Error deleting portal '" + name + "'. Portal does not exist!", sender);
					break;
				}
				
				portal.delete(name);
				messages.send("&f[&6Portal&f] Deleted portal '" + name + "'.", sender);
			}
			return true;
		}
		
		case "show" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (!Permissions.PORTAL_SHOW.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (selection.getPos1() == null || selection.getPos2() == null) {
				messages.send("&f[&6Portal&f] Select two points with the portalwand first.", player);
			}
			
			visualizer.setVisualizing();
			
			if (visualizer.isVisualizing()) {
				
				if (args.length == 2) {
					
				} else {
					visualizer.setCornerA(selection.getPos1());
					visualizer.setCornerB(selection.getPos2());
				}
				
				visualizer.update();
				
			} else visualizer.stop();
			return true;
		}
		
		case "clear" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (!Permissions.PORTAL_CLEAR.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (selection == null) {
				messages.send("&f[&6Portal&f] No selections to clear.", sender);
				break;
			}
			
			selection.clear(player.getUniqueId());
			messages.send("&f[&6Portal&f] Cleared last selection.", sender);
			return true;
		}
		
		case "reload" : {
			
			if (!Permissions.PORTAL_RELOAD.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			portal.reloadAll();
			messages.send("&f[&6Portals&f] Reloading all portals.", sender);
			return true;
		}
		
		}
		
		return true;
	}
}
