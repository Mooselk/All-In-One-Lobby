package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.PortalLocation;
import me.kate.lobby.modules.portals.utils.PortalWand;
import me.kate.lobby.utils.Logger;

public class PortalCommand extends PortalLocation implements CommandExecutor {

	private Config portalConfig = new PortalsConfig();
	private final Portal portal = new Portal();
	
	public PortalCommand() {
		super(null, null);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("portal")) {
			if (args.length == 0) {
				Messages.portalHelp(player);
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("lobby.portal.help"))
						Messages.portalHelp(player);
					else 
						Messages.noPermission(player);
				}

				if (args[0].equalsIgnoreCase("wand")) {
					if (player.hasPermission("lobby.portal.wand")) {
						Messages.send("&f[&6Portal&f] &5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2", player);
						player.getInventory().addItem(PortalWand.WAND);
					} else Messages.noPermission(player);
				}

				if (args[0].equalsIgnoreCase("create")) {
					if (player.hasPermission("lobby.portal.create")) {
						if (args.length < 3) {
							Messages.send("&6Usage: &f/portal create <portal_name> <server>", player);
						} else {
							if (args.length == 3) {
								String name = args[1];
								String server = args[2];
								String world = player.getWorld().getName();
								
								Location pos1 = fromPortal(getPlayerSelection("POS1", player), player, "POS1");
								Location pos2 = fromPortal(getPlayerSelection("POS2", player), player, "POS2");
								
								if (pos1 != null && pos2 != null) {
									
									Logger.debug("Selection 1: " + getPlayerSelection("POS1", player) + " Selection 2: " + getPlayerSelection("POS2", player));				
									Logger.debug("Location 1: " + pos1 + " Location 2: " + pos2);
									
									portal.create(pos1, pos2, name, world, server, player);
									Messages.send("&f[&6Portal&f] Created portal '" + name + "'.", player);
									
								} else {
									Messages.send("&f[&6Portal&f] Create a selection before creating a portal.", player);
								}
							}
						}
					} else Messages.noPermission(player);
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (player.hasPermission("lobby.portal.delete")) {
						if (args.length < 2) {
							Messages.send("&6Usage: &f/portal delete <portal_name>", player);
						} else {
							if (args.length == 2) {
								String name = args[1];
								if (portalConfig.getConfig().getString("portals." + name) != null) {
									portal.delete(name);
									Messages.send("&f[&6Portal&f] Deleted portal '" + name + "'.", player);
								} else {
									Messages.send("&f[&6Portal&f] Error deleting portal '" + name + "'. Portal does not exist!", player);
								}
							}
						}
					} else Messages.noPermission(player);
				}
				
				if (args[0].equalsIgnoreCase("clear")) {
					if (player.hasPermission("lobby.portal.clear")) {
						if (!Main.getInstance().getPortals().isEmpty()) {
							// Main.getInstance().getPortals().clear();
							Messages.send("unfinished", player);
							Messages.send("&f[&6Portal&f] Cleared last selection.", player);
						} else {
							Messages.send("&f[&6Portal&f] No selections to clear.", player);
						}
					} else Messages.noPermission(player);
				}

				if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("lobby.portal.reload")) {
						portal.reloadAll();
						Messages.send("&f[&6Portals&f] Reloading all portals.", player);
					} else Messages.noPermission(player);
				}
			}
		}
		return true;
	}
}
