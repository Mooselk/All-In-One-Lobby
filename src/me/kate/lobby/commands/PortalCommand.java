package me.kate.lobby.commands;

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
			if (p.isOp() || p.hasPermission("permission.all")) {
				p.getInventory().addItem(PortalWand.WAND);
			}
		}
		return false;
	}
}
