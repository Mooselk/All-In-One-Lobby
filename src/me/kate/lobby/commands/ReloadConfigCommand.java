package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import net.md_5.bungee.api.ChatColor;

public class ReloadConfigCommand implements CommandExecutor {

	private ISelectorSettings sf = new SelectorFile();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("reloadselector")) {
			if (p.isOp()) {
				sf.reload();
				if (!sf.reloadFailed()) {
					p.sendMessage(ChatColor.RED + "Reloaded selector! ");
				} else {
					p.sendMessage(ChatColor.RED + "Reload failed, Check console for errors! ");
				}
			}
		}
		return true;
	}
}
