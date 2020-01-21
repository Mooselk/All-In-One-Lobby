package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.servers.Servers;

public class LobbyCommand extends Servers implements CommandExecutor {

	private final Messages msgs = new Messages();
	private Config selectorConfig = new SelectorConfig();
	private Config mainConfig = new PluginConfig();
	private ToggleConfig togglePlayersConfig = new ToggleConfig();
	
	public LobbyCommand(JavaPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("lobby")) {
			if (args.length == 0) {
				if (player.hasPermission("lobby.help")) {
					msgs.lobbyHelp(player);
				}
			} else {
				if (args[0].equalsIgnoreCase("test")) {
					
				}
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
								if (togglePlayersConfig.reload())
									msgs.send("Successfully reloaded playerhider", player);
								else
									msgs.send("Failed to reload playerhider", player);
								break;
							case "selector":
								if (selectorConfig.reload())
									msgs.send("Successfully reloaded selector", player);
								else
									msgs.send("Failed to reload selector", player);
								break;
							case "config":
								if (mainConfig.reload()) {
									if (Main.getInstance().getConfig().getBoolean("tablist.enabled")) {
										Main.getInstance().getTabList().update();
									}
									msgs.send("Successfully reloaded config", player);
								} else
									msgs.send("Failed to reload config", player);
								break;
							default:
								msgs.send("Unknown config: " + args[1].toLowerCase(), player);
							}
						} else {
							msgs.lobbyHelpReload(player);
						}
					}
				}
			}
		}
		return true;
	}
}