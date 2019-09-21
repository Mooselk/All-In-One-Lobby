package me.kate.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.items.portals.PortalWand;

public class PortalCommand implements CommandExecutor {

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
						
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (p.hasPermission("lobby.portal.delete")) {
						
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}
				
				if (args[0].equalsIgnoreCase("clear")) {
					if (p.hasPermission("lobby.portal.clear")) {
						
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("lobby.portal.reload")) {
						
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You do not have permission to use this command."));
					}
				}
			}
		}
		return false;
	}

	private void helpMessage(Player p) {
		p.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&m-------------&r[ &l&6Portal Help&r ]&m-------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal wand &f- Gives portal selection tool."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal create <&fname&6> <&fserver&6> &f- Create portal."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal delete <&fname&6> &f- Deletes specified portal."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal clear &f- Clears current portal selection."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal reload &f- Reload portal storage config."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
	}
}
