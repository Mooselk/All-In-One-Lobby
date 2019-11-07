package me.kate.lobby;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {
	
	public Messages() {}
	
	public void send(String msg, Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public void noPermission(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to use this command."));
	}
	
	public void lobbyHelp(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m------------&r[ &l&6Lobby Help&r ]&m------------"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/spawn &f- Sends you to lobby spawn point."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lobby setspawn &f- Sets lobby spawn point."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc help &f- Lists all NPC commands."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal help &f - Lists all portal commands."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lobby reload <config> &f- Reload specifed config."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
	}

	public void lobbyHelpReload(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Lobby&f] Reloadable configs: lobby, selector, playerhider"));
	}
	
	public void npcHelp(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m-------------&r[ &l&6NPC Help&r ]&m-------------"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc create <&fnpc_name&6> <&fskin_id&6> &f- Creates NPC."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc move <&fnpc_name&6> &f- Move NPC to your location."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc delete <&fnpc_name&6> &f- Lists all portal commands."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc setskin <&fskin_id&6> &f- Change a specfic NPCs skin."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/npc reload &f- Reloads NPC config and respawns NPCs."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Note: &f&oSkin ids can be otained from https://mineskin.org/"));
	}
	
	public void portalHelp(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m-------------&r[ &l&6Portal Help&r ]&m-------------"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal wand &f- Gives portal selection tool."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal create <&fname&6> <&fserver&6> &f- Create portal."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal delete <&fname&6> &f- Deletes specified portal."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal clear &f- Clears current portal selection."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/portal reload &f- Reload portal storage config."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
	}
}
