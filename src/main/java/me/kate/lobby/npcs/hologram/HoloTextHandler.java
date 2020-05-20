package me.kate.lobby.npcs.hologram;

import java.util.Map;

import me.kate.lobby.Main;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.utils.Utils;

public class HoloTextHandler {

	private String preloadCount = "Loading...";
	private String offline = "OFFLINE";
	
	public void updateText(String serverName, String name) {
		LobbyNPC lobbyNpc = LobbyNPC.getByName(name);
		String playerCount = getPlayerCount(serverName);
		lobbyNpc.getNPC().setText(Utils.replaceText(lobbyNpc.getHolotext(), playerCount));
	}

	public String getPlayerCount(String serverName) {
		boolean isOnline = false;
		Map<String, Object> placeholders = null;
		
		if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
			placeholders = Main.getInstance().getPlaceholders().get(serverName);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		
		if (isOnline) 
			return (String) placeholders.get("online");
		
		if (!isOnline) 
			return offline;
		
		return preloadCount;
	}
}
