package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.Permissions;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.modules.selector.Selector;

public class LobbyCommand implements CommandExecutor {

	private Main plugin;
	private Config selectorConfig;
	private Config mainConfig;
	private Config togglePlayersConfig;
	private Selector selector;
	private Messages messages;

	public LobbyCommand(Main plugin) {
		this.plugin = plugin;
		this.selectorConfig = new SelectorConfig();
		this.mainConfig = new PluginConfig();
		this.togglePlayersConfig = new ToggleConfig();
		this.selector = plugin.getSelector();
		this.messages = new Messages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("lobby")) {
			return false;
		}

		if (args.length == 0) {
			messages.lobbyHelp(sender);
			return false;
		}

		switch (args[0]) {

		case "help": {
			if (!Permissions.LOBBY_HELP.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			messages.lobbyHelp(sender);
			break;
		}

		case "setspawn": {
			if (!Permissions.LOBBY_SETSPAWN.has(sender)) {
				messages.noPermission(sender);
				break;
			}

			if (!(sender instanceof Player)) {
				messages.consoleSender(sender);
				break;
			}

			Spawn.setSpawn((Player) sender);
			break;
		}

		case "spawn": {
			if (!Permissions.LOBBY_SPAWN.has(sender)) {
				messages.noPermission(sender);
				break;
			}

			if (!(sender instanceof Player)) {
				messages.consoleSender(sender);
				break;
			}

			Spawn.toSpawn((Player) sender);
			break;
		}

		case "reload": {

			if (!Permissions.LOBBY_HELP.has(sender)) {
				messages.noPermission(sender);
				break;
			}

			if (args.length > 1) {
				switch (args[1]) {

				case "playerhider": {
					togglePlayersConfig.reloadConfig(togglePlayersConfig, sender);
					break;
				}

				case "selector": {
					selectorConfig.reloadConfig(selectorConfig, sender);
					selector.reload();
					break;
				}

				case "config": {
					mainConfig.reloadConfig(mainConfig, sender);
					if (mainConfig.getConfig().getBoolean("tablist.enabled"))
						plugin.getTabList().update();
					break;
				}

				default:
					messages.send("Unknown config: " + args[1].toLowerCase(), sender);
				}

			} else {
				messages.lobbyHelpReload(sender);
			}

			break;
		}

		default: {
			messages.lobbyHelp(sender);
		}

		}

		return true;
	}
}