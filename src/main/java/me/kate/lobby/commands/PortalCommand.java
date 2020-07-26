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
		this.portalConfig = new PortalsConfig(plugin);
		this.portal = new Portal(plugin);
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
			return false;
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
			
			if (Permissions.PORTAL_HELP.hasPermission(sender)) {
				messages.portalHelp(sender);
				return true;
			}
			
			break;
		}
		
		case "wand" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (Permissions.PORTAL_HELP.hasPermission(sender)) {
				messages.send("&f[&6Portal&f] &5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2", sender);
				player.getInventory().addItem(PortalWand.WAND);
				return true;
			}
		}
		
		case "create" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (Permissions.PORTAL_CREATE.hasPermission(sender)) {
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
			
			break;
		}
		
		case "delete" : {
			
			if (Permissions.PORTAL_DELETE.hasPermission(sender)) {
				
				if (args.length < 2) {
					messages.send("&6Usage: &f/portal delete <portal_name>", sender);
					break;
				}
				
				if (args.length == 2) {
					String name = args[1];
					
					// test
					if (!portalConfig.delete(name)) {
						messages.send("&f[&6Portal&f] Error deleting portal '" + name + "'. Portal does not exist!", sender);
						break;
					}
					portal.getPortals().remove(name);
					messages.send("&f[&6Portal&f] Deleted portal '" + name + "'.", sender);
				}
				return true;
			}
			break;
		}
		
		case "show" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (Permissions.PORTAL_SHOW.hasPermission(sender)) {
				
				if (selection == null || selection.getPos1() == null || selection.getPos2() == null) {
					messages.send("&f[&6Portal&f] Select two points with the portalwand first.", player);
					return true;
				}
				
				visualizer.setVisualizing();
				
				if (visualizer.isVisualizing()) {
					
					messages.send("&f[&6Portal&f] Selection shown.", sender);
					if (args.length == 2) {
						// todo
						// show existing portal selection
					} else {
						visualizer.setCornerA(selection.getPos1());
						visualizer.setCornerB(selection.getPos2());
					}
					
					visualizer.update();
					
				} else {
					visualizer.stop();
					messages.send("&f[&6Portal&f] Selection hidden.", sender);
				}
				
				return true;
			}
			break;
		}
		
		case "clear" : {
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (Permissions.PORTAL_CLEAR.hasPermission(sender)) {
				
				if (selection == null) {
					messages.send("&f[&6Portal&f] No selections to clear.", sender);
					break;
				}
				
				if (visualizer.isVisualizing()) {
					visualizer.setVisualizing();
					visualizer.stop();
				}
				
				selection.clear(player.getUniqueId());
				messages.send("&f[&6Portal&f] Cleared last selection.", sender);
				return true;
			}
			break;
		}
		
		case "reload" : {
			
			if (Permissions.PORTAL_RELOAD.hasPermission(sender)) {
				portal.reloadAll();
				messages.send("&f[&6Portals&f] Reloading all portals.", sender);
				return true;
			}
			
			break;
		}
		
		case "set" : {
			
			if (Permissions.PORTAL_SET.hasPermission(sender)) {
				
				switch (args[1]) {
				
				case "server" : {
					
					if (args.length != 3) {
						messages.send("&6Usage: &f/portal set server <name> <server>", sender);
						return false;
					}
					
					portalConfig.setServer(args[2], args[3]);
					messages.send("&f[&6Portals&f] Successfully set server for portal " + args[2] + " to " + args[3], sender);
					return true;
				}
				
				}
				
				messages.send("&6Usage: ", sender);
				messages.send(" - &f/portal set server <name> <server>", sender);
				messages.send(" - &f/portal set something <name> <something>", sender);
			}
		}
		
		}
		
		return true;
	}
}
