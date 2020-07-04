package me.kate.lobby;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.utils.Utils;

public class Messages {
	
	private static Messages instance;
	
	public Messages() {
	}
	
	public static Messages get() {
		if (instance == null) {
            instance = new Messages();
        }
		return instance;
	}
	
	private final String[] colorCodes = {
			"&0", "&1", "&2", 
			"&3", "&4", "&5", 
			"&6", "&7", "&8", 
			"&9", "&a", "&b",
			"&c", "&d", "&e", 
			"&f", "&k", "&l", 
			"&m", "&n","&o", 
				  "&r",
	};
	
	private String removeColor(String str) {
		String newStr = null;
		for (String code : colorCodes) {
			str = str.replace(code, "");
			newStr = str;	
		}	
		return newStr;
	}
	
	public void send(String msg, CommandSender sender) {
		if ((sender instanceof Player)) {
			sender.sendMessage(Utils.color(msg));
			return;
		}
		sender.sendMessage(removeColor(msg));
	}
	
	public void noPermission(CommandSender sender) {
		sender.sendMessage(Utils.color("&cYou do not have permission to use this command."));
	}
	
	public void consoleSender(CommandSender sender) {
		sender.sendMessage("This command can only be run by a player!");
	}
	
	public void sendList(CommandSender player, ConfigurationSection section) {
		for (String message : section.getStringList("messages")) {
			if (!message.equals("none"))
				send(message, player);
			else break;
		}
	}
	
	private final String[] lobbyHelp = {
			"&m------------&r[ &l&6Lobby Help&r ]&m------------",
			"",
			"&6/lobby spawn &f- Sends you to lobby spawn point.",
			"&6/lobby setspawn &f- Sets lobby spawn point.",
			"&6/lobby reload <config> &f- Reload specifed config.",
			" - ",
			"&6/npc help &f- Lists all NPC commands.",
			"&6/portal help &f - Lists all portal commands.",
			"",
	};
	
	public void lobbyHelp(CommandSender sender) {
		for (String line : lobbyHelp) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(removeColor(line));
				continue;
			} 
			sender.sendMessage(Utils.color(line));
		}
	}

	private final String reloadConfig = "&f[&6Lobby&f] Reloadable configs: config, selector, playerhider";
	
	public void lobbyHelpReload(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Utils.color(reloadConfig));
			return;
		}
		sender.sendMessage(removeColor(reloadConfig));
	}
	
	private final String[] npcHelp = {
			"&m-------------&r[ &l&6NPC Help&r ]&m-------------", "", 
			"&6/npc create <&fnpc_name&6> <&fskin_id&6> &f- Creates NPC.", 
			"&6/npc move <&fnpc_name&6> &f- Move NPC to your location.",
			"&6/npc delete <&fnpc_name&6> &f- Deletes sepcified NPC.",
			"&6/npc setskin <&fskin_id&6> &f- Change a specified NPCs skin.", 
			"&6/npc reload &f- Reloads NPC config and respawns NPCs.", "", 
			"&6Note: &f&oSkin ids can be otained from https://mineskin.org/",
	};
	
	
	public void npcHelp(CommandSender sender) {
		for (String line : npcHelp) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(removeColor(line));
				continue;
			} 
			sender.sendMessage(Utils.color(line));
		}
	}
	
	private final String[] portalHelp = {
			"&m-------------&r[ &l&6Portal Help&r ]&m-------------", "", 
			"&6/portal wand &f- Gives portal selection tool.", 
			"&6/portal create <&fname&6> <&fserver&6> &f- Create portal.",
			"&6/portal delete <&fname&6> &f- Deletes specified portal.", 
			"&6/portal clear &f- Clears current portal selection.", 
			"&6/portal reload &f- Reload portal storage config.",
	};
	
	public void portalHelp(CommandSender sender) {
		for (String line : portalHelp) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(removeColor(line));
				continue;
			} 
			sender.sendMessage(Utils.color(line));
		}
	}
}
