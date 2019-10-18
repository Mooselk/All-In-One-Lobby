package me.kate.lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.items.Spawn;

public class LobbyCommand implements CommandExecutor {

	private FileConfiguration c = Main.getInstance().getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lobby")) {
			if (args.length == 0) {
				if (p.hasPermission("lobby.help")) {
					this.helpMessage(p);
				}
				return true;
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("lobby.npc.help")) {
						this.helpMessage(p);
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (p.hasPermission("lobby.setspawn")) {
						Location loc = p.getLocation();
						c.set("spawn.world", p.getWorld().getName());
						c.set("spawn.x", loc.getBlockX());
						c.set("spawn.y", loc.getBlockY());
						c.set("spawn.z", loc.getBlockZ());
						c.set("spawn.yaw", loc.getYaw());
						c.set("spawn.pitch", loc.getPitch());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&f[&6Lobby&f] Set spawn in world '&6" 
						+ p.getWorld().getName() 
						+ "&f' at X: &6"
						+ loc.getBlockX() 
						+ "&f Y: &6" 
						+ loc.getBlockY() 
						+ "&f Z: &6"
						+ loc.getBlockZ()));
						Main.getInstance().saveConfig();
						Main.getInstance().reloadConfig();
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (p.hasPermission("lobby.spawn")) {
						p.teleport(Spawn.toSpawn());
						p.sendMessage(
								ChatColor.translateAlternateColorCodes('&', "&f[&6Lobby&f] Teleporting to spawn..."));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("lobby.reload")) {
						if (args.length > 1) {
							switch (args[1]) {
							case "playerhider":
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "selector"));
								break;
							case "selector":
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "selector"));
								break;
							case "lobby":
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "lobby"));
								break;
							default:
							}
						} else {
							this.helpReload(p);
						}
					}
				}
			}
		}
		return false;
	}
	
	private void reload(String conf) {
		if (conf.equalsIgnoreCase("selector")) {
			Bukkit.getLogger().info("relod");
		}
		if (conf.equalsIgnoreCase("playerhider")) {
			
		}
		if (conf.equalsIgnoreCase("lobby")) {
			
		}
	}

	private void helpMessage(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m------------&r[ &l&6Lobby Help&r ]&m------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/spawn &f- Sends you to lobby spawn point."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lobby setspawn &f- Sets lobby spawn point."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc help &f- Lists all NPC commands."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal help &f - Lists all portal commands."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lobby reload <config> &f- Reload specifed config."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
	}

	private void helpReload(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Lobby&f] Reloadable configs: lobby, selector, playerhider"));
	}
}