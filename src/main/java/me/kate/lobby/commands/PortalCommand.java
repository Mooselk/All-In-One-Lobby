package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.items.portals.Portal;
import me.kate.lobby.items.portals.Position;
import me.kate.lobby.items.portals.utils.PortalWand;
import me.kate.lobby.utils.Messages;

public class PortalCommand implements CommandExecutor {

	private final Portal portal = new Portal();
	private final Messages msgs = new Messages();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("portal")) {
			if (args.length == 0) {
				msgs.portalHelp(p);
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("lobby.portal.help")) {
						msgs.portalHelp(p);
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("wand")) {
					if (p.hasPermission("lobby.portal.wand")) {
						msgs.send("&f[&6Portal&f] &5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2", p);
						p.getInventory().addItem(PortalWand.WAND);
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("create")) {
					if (p.hasPermission("lobby.portal.create")) {
						if (args.length < 3) {
							msgs.send("&6Usage: &f/portal create <portal_name> <server>", p);
						} else {
							if (args.length == 3) {
								String name = args[1];
								String server = args[2];
								String world = p.getWorld().getName();
								if (!Main.SELECTIONS.isEmpty()) {
									Location pos1 = Main.SELECTIONS.get(Position.POS1);
									Location pos2 = Main.SELECTIONS.get(Position.POS2);
									msgs.send("&f[&6Portal&f] Created portal '" + name + "'.", p);
									portal.create(pos1, pos2, name, world, server);
								} else {
									msgs.send("&f[&6Portal&f] Create a selection before creating a portal.", p);
								}
							}
						}
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (p.hasPermission("lobby.portal.delete")) {
						if (args.length < 2) {
							msgs.send("&6Usage: &f/portal delete <portal_name>", p);
						} else {
							if (args.length == 2) {
								String name = args[1];
								if (PortalsConfig.portalconf.getString("portals." + name) != null) {
									portal.delete(name);
									msgs.send("&f[&6Portal&f] Deleted portal '" + name + "'.", p);
								} else {
									msgs.send("&f[&6Portal&f] Error deleting portal '" + name + "'. Portal does not exist!", p);
								}
							}
						}
					} else {
						msgs.noPermission(p);
					}
				}
				
				if (args[0].equalsIgnoreCase("clear")) {
					if (p.hasPermission("lobby.portal.clear")) {
						if (!Main.PORTALS.isEmpty()) {
							Main.PORTALS.clear();
							msgs.send("&f[&6Portal&f] Cleared last selection.", p);
						} else {
							msgs.send("&f[&6Portal&f] No selections to clear.", p);
						}
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("lobby.portal.reload")) {
						portal.reloadAll();
						msgs.send("&f[&6Portals&f] Reloading all portals.", p);
					} else {
						msgs.noPermission(p);
					}
				}
			}
		}
		return false;
	}
}
