package me.kate.lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import net.md_5.bungee.api.ChatColor;

public class SetSpawnCommand implements CommandExecutor {

	private FileConfiguration c = Main.getInstance().getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Bukkit.getServer().getLogger().warning("This command can only be used by a player!");
			return true;
		} else {
			final Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("setspawn")) {
				if (p.isOp()) {
					Location loc = p.getLocation();
					c.set("spawn.world", p.getWorld().getName());
					c.set("spawn.x", loc.getBlockX());
					c.set("spawn.y", loc.getBlockY());
					c.set("spawn.z", loc.getBlockZ());
					c.set("spawn.yaw", loc.getYaw());
					c.set("spawn.pitch", loc.getPitch());
					p.sendMessage(ChatColor.AQUA 
							+ "Set spawn in world '"
							+ p.getWorld().getName()
							+ "' at X: "
							+ loc.getBlockX()
							+ " Y: "
							+ loc.getBlockY()
							+ " Z: "
							+ loc.getBlockZ());
					Main.getInstance().saveConfig();
					Main.getInstance().reloadConfig();
				} else {
					p.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
				}
			}
		}
		return true;
	}
}
