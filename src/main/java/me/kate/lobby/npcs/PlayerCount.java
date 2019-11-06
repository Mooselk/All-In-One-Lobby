package me.kate.lobby.npcs;

import java.util.Map;

import me.kate.lobby.Main;
import me.kate.lobby.utils.Logger;

public class PlayerCount {

	public String getPlayerCount(String serverName) {
		String count = "Loading...";
		String offlineMSG = "0";
		boolean isOnline = false;
		Map<String, Object> placeholders = null;
		if (Main.NPC_PLACEHOLDERS.containsKey(serverName)) {
			placeholders = Main.NPC_PLACEHOLDERS.get(serverName);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		if (isOnline) {
			Logger.debug("Online: " + (String) placeholders.get("online") + " server: " + serverName);
			Logger.debug("  " + Main.NPC_PLACEHOLDERS);
			return (String) placeholders.get("online");
		} else {
			count = offlineMSG;
		}
		return count;
	}
}
