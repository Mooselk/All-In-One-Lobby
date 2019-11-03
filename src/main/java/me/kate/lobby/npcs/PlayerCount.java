package me.kate.lobby.npcs;

import java.util.Map;

import me.kate.lobby.Main;
import me.kate.lobby.npcs.tasks.PingType;
import me.kate.lobby.utils.Logger;

public class PlayerCount {

	public PlayerCount() {

	}
	
	public String getPlayerCount(String serverName, PingType type) {
		String count = "Loading... ";
		if (type.equals(PingType.LOCAL)) {
			Main.getInstance().api.getPlayerCount(serverName).whenComplete((result, error) -> {
				
			});
		}
		if (type.equals(PingType.EXTERNAL)) {
			boolean isOnline;
			Map<String, Object> placeholders = null;
			if (Main.NPC_PLACEHOLDERS.containsKey(serverName)) {
				placeholders = Main.NPC_PLACEHOLDERS.get(serverName);
				isOnline = (boolean) placeholders.get("isOnline");
			} else {
				isOnline = false;
			}
			if (isOnline) {
				Logger.debug("Online: " + (String) placeholders.get("online")  + " server: " + serverName);
				count = (String) placeholders.get("online");
			}
		}
		return count;
	}
}
