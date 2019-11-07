package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Messages;
import me.kate.lobby.modules.Spawn;

public class LobbyCommand implements CommandExecutor {

	private final Messages msgs = new Messages();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lobby")) {
			if (args.length == 0) {
				if (player.hasPermission("lobby.help")) {
					msgs.lobbyHelp(player);
				}
				return true;
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("lobby.npc.help")) {
						msgs.lobbyHelp(player);
					} else {
						msgs.noPermission(player);
								
					}
				}
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (player.hasPermission("lobby.setspawn")) {
						Location loc = player.getLocation();
						Spawn.setSpawn(player, loc);
					} else {
						msgs.noPermission(player);
					}
				}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (player.hasPermission("lobby.spawn")) {
						player.teleport(Spawn.toSpawn());
						msgs.send("&f[&6Lobby&f] Teleporting to spawn...", player);
					} else {
						msgs.noPermission(player);
								
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("lobby.reload")) {
						if (args.length > 1) {
							switch (args[1]) {
							case "playerhider":
								msgs.send("playerhider", player);
								break;
							case "selector":
								msgs.send("selector", player);
								break;
							case "lobby":
								msgs.send("lobby", player);
								break;
							default:
							}
						} else {
							msgs.lobbyHelpReload(player);
						}
					}
				}
			}
		}
		return false;
	}
	
//	private void reload(String conf) {
//		if (conf.equalsIgnoreCase("selector")) {
//			
//		}
//		if (conf.equalsIgnoreCase("playerhider")) {
//			
//		}
//		if (conf.equalsIgnoreCase("lobby")) {
//			
//		}
//	}
}