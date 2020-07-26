package me.kate.lobby.npcs.hologram;

import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.objects.PlaceHolder;
import me.kate.lobby.utils.Utils;

public class HoloTextHandler {

	private final String preloadCount = "Loading...";
	private final String offline = "[OFFLINE]";
	
	public void updateText(LobbyNPC lobbyNpc) {
		String playerCount = getPlayerCount(lobbyNpc.getServer());
		lobbyNpc.getNPC().setText(Utils.replace(lobbyNpc.getHolotext(), playerCount));
	}
	
	private String getPlayerCount(String serverName) {
		PlaceHolder placeholder = null;
		if (PlaceHolder.getPlaceHolders().containsKey(serverName)) {
			placeholder = PlaceHolder.getPlaceHolders().get(serverName);
		}
		
		if (placeholder == null) {
			return offline;
		}
		
		if (placeholder.isOnline()) {
			return placeholder.getPlayerCount();
		}
		
		return preloadCount;
	}
}
