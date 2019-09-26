package me.kate.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PortalsFile;
import me.kate.lobby.items.portals.Portal;
import me.kate.lobby.items.portals.utils.PortalWand;

public class PortalCommand implements CommandExecutor {

	private final Portal portal = new Portal();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("portal")) {
			if (args.length == 0) {
				this.helpMessage(p);
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("lobby.portal.help")) {
						this.helpMessage(p);
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("wand")) {
					if (p.hasPermission("lobby.portal.wand")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] &5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2"));
						p.getInventory().addItem(PortalWand.WAND);
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("create")) {
					if (p.hasPermission("lobby.portal.create")) {
						if (args.length < 3) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Usage: &f/portal create <portal_name> <server>"));
						} else {
							if (args.length == 3) {
								String name = args[1];
								String server = args[2];
								String world = p.getWorld().getName();
								if (!Main.SELECTIONS.isEmpty()) {
									Location pos1 = Main.SELECTIONS.get("pos1");
									Location pos2 = Main.SELECTIONS.get("pos2");
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] Created portal '" + name + "'."));
									portal.create(pos1, pos2, name, world, server);
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] Create a selection before creating a portal."));
								}
							}
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (p.hasPermission("lobby.portal.delete")) {
						if (args.length < 2) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Usage: &f/portal delete <portal_name>"));
						} else {
							if (args.length == 2) {
								String name = args[1];
								if (PortalsFile.portalconf.getString("portals." + name) != null) {
									portal.delete(name);
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] Deleted portal '" + name + "'."));
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] Error deleting portal '" + name + "'. Portal does not exist!"));
								}
							}
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}
				
				if (args[0].equalsIgnoreCase("clear")) {
					if (p.hasPermission("lobby.portal.clear")) {
						if (!Main.PORTALS.isEmpty()) {
							Main.PORTALS.clear();
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] Cleared last selection."));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portal&f] No selections to clear."));
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("lobby.portal.reload")) {
						portal.reloadAll();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Portals&f] Reloading all portals."));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}
			}
		}
		return false;
	}

	private void helpMessage(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m-------------&r[ &l&6Portal Help&r ]&m-------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal wand &f- Gives portal selection tool."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal create <&fname&6> <&fserver&6> &f- Create portal."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal delete <&fname&6> &f- Deletes specified portal."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal clear &f- Clears current portal selection."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal reload &f- Reload portal storage config."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
	}
}
