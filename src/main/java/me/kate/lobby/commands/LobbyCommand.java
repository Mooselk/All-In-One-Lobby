package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.modules.selector.Selector;
import me.kate.lobby.tasks.HeartBeat;
import me.kate.lobby.utils.Utils;

public class LobbyCommand implements CommandExecutor {

	private Config selectorConfig = new SelectorConfig();
	private Config mainConfig = new PluginConfig();
	private ToggleConfig togglePlayersConfig = new ToggleConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lobby")) {
			if (args.length == 0) {
				if (player.hasPermission("lobby.help")) {
					Messages.lobbyHelp(player);
				}
			} else {
				if (args[0].equalsIgnoreCase("test")) {
					HeartBeat.fetchServerStatusFromAsync("localhost", 25566, status -> {
					System.out.println("ServerStatus: " + status.toString());
				});
				}
				if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("lobby.npc.help")) {
						Messages.lobbyHelp(player);
					} else {
						Messages.noPermission(player);
					}
				}
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (player.hasPermission("lobby.setspawn")) {
						Location loc = player.getLocation();
						Spawn.setSpawn(player, loc);
					} else {
						Messages.noPermission(player);
					}
				}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (player.hasPermission("lobby.spawn")) {
						player.teleport(Spawn.toSpawn());
						Messages.send("&f[&6Lobby&f] Teleporting to spawn...", player);
					} else {
						Messages.noPermission(player);

					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("lobby.reload")) {
						if (args.length > 1) {
							switch (args[1]) {
							case "playerhider":
								Utils.reloadConfig(togglePlayersConfig, player);
								break;
							case "selector":
								Utils.reloadConfig(selectorConfig, player);
								new Selector().setup();
								break;
							case "config":
								Utils.reloadConfig(mainConfig, player);
								if (mainConfig.getConfig().getBoolean("tablist.enabled")) {
									Main.getInstance().getTabList().update();
								}
								break;
							default:
								Messages.send("Unknown config: " + args[1].toLowerCase(), player);
							}
						} else {
							Messages.lobbyHelpReload(player);
						}
					}
				}
			}
		}
		return true;
	}
}