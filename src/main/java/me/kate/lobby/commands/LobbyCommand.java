package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.HidePlayersConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.modules.Spawn;

public class LobbyCommand implements CommandExecutor {

	private final Messages msgs = new Messages();
	private ISelectorSettings selector = new SelectorConfig();
	private IHidePlayerSettings hider = new HidePlayersConfig();
	
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
								if (hider.reload())
									msgs.send("Successfully reloaded playerhider", player);
								else msgs.send("Failed to reload playerhider", player);
								break;
							case "selector":
								if (selector.reload())
									msgs.send("Successfully reloaded selector", player);
								else msgs.send("Failed to reload selector", player);
								break;
							case "config":
								if (Config.reload()) {
									Main.getInstance().reloadConfig();
									Main.getTabList().update();
									msgs.send("Successfully reloaded config", player);
								} else msgs.send("Failed to reload config", player);
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
}