package me.kate.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lobby")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m------------&r[ &l&6Lobby Help&r ]&m------------"));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/spawn &f- Sends you to lobby spawn point."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/setspawn &f- Sets lobby spawn point."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc help &f- Lists all NPC commands."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal help &f - Lists all portal commands."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lobby reload <config> &f- Reload specifed config."));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		}
		return false;
	}
}