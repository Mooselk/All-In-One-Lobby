package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.utils.LocalServerPinger;
import net.md_5.bungee.api.ChatColor;

public class ReloadConfigCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("reloadconfig")) {
			if (p.isOp()) {
				Main.getInstance().reloadConfig();
				p.sendMessage(ChatColor.RED + "Reloading config! " + LocalServerPinger.getPing(p));
			}
		}
		return true;
	}
}
