package me.kate.lobby;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.utils.Utils;

public class Messages {
	
	public static void send(String msg, Player player) {
		if (player == null) 
			return;
		player.sendMessage(Utils.color(msg));
	}
	
	public static void noPermission(Player player) {
		player.sendMessage(Utils.color("&cYou do not have permission to use this command."));
	}
	
	public static void sendList(Player player, ConfigurationSection section) {
		for (String message : section.getStringList("messages")) {
			if (!message.equals("none"))
				send(message, player);
			else break;
		}
	}
	
	public static void lobbyHelp(Player player) {
		player.sendMessage(Utils.color("&m------------&r[ &l&6Lobby Help&r ]&m------------"));
		player.sendMessage(Utils.color(""));
		player.sendMessage(Utils.color("&6/spawn &f- Sends you to lobby spawn point."));
		player.sendMessage(Utils.color("&6/lobby setspawn &f- Sets lobby spawn point."));
		player.sendMessage(Utils.color("&6/npc help &f- Lists all NPC commands."));
		player.sendMessage(Utils.color("&6/portal help &f - Lists all portal commands."));
		player.sendMessage(Utils.color("&6/lobby reload <config> &f- Reload specifed config."));
		player.sendMessage(Utils.color(""));
	}

	public static void lobbyHelpReload(Player player) {
		player.sendMessage(Utils.color("&f[&6Lobby&f] Reloadable configs: config, selector, playerhider"));
	}
	
	public static void npcHelp(Player player) {
		player.sendMessage(Utils.color("&m-------------&r[ &l&6NPC Help&r ]&m-------------"));
		player.sendMessage(Utils.color(""));
		player.sendMessage(Utils.color("&6/npc create <&fnpc_name&6> <&fskin_id&6> &f- Creates NPC."));
		player.sendMessage(Utils.color("&6/npc move <&fnpc_name&6> &f- Move NPC to your location."));
		player.sendMessage(Utils.color("&6/npc delete <&fnpc_name&6> &f- Lists all portal commands."));
		player.sendMessage(Utils.color("&6/npc setskin <&fskin_id&6> &f- Change a specfic NPCs skin."));
		player.sendMessage(Utils.color("&6/npc reload &f- Reloads NPC config and respawns NPCs."));
		player.sendMessage(Utils.color(""));
		player.sendMessage(Utils.color("&6Note: &f&oSkin ids can be otained from https://mineskin.org/"));
	}
	
	public static void portalHelp(Player player) {
		player.sendMessage(Utils.color("&m-------------&r[ &l&6Portal Help&r ]&m-------------"));
		player.sendMessage(Utils.color(""));
		player.sendMessage(Utils.color("&6/portal wand &f- Gives portal selection tool."));
		player.sendMessage(Utils.color("&6/portal create <&fname&6> <&fserver&6> &f- Create portal."));
		player.sendMessage(Utils.color("&6/portal delete <&fname&6> &f- Deletes specified portal."));
		player.sendMessage(Utils.color("&6/portal clear &f- Clears current portal selection."));
		player.sendMessage(Utils.color("&6/portal reload &f- Reload portal storage config."));
		player.sendMessage(Utils.color(""));
	}
}
