package me.kate.lobby.modules.portals.select;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.kate.lobby.utils.Logger;

public class Selections {

	/**
	 * String (Position-UUID)
	 * #translate(String)
	 * 
	 * String (PortalLocation)
	 * 
	 */
	private static final Map<String, String> SELECTIONS = new HashMap<>();
	
	public Map<String, String> getSelections() {
		return SELECTIONS;
	}

	public void clearSelection(Player player) {
		for (Map.Entry<String, String> m : getSelections().entrySet()) {
			if (player.getUniqueId() == translate(m.getKey())) {
				getSelections().remove(m.getKey());
			}
		}
	}
	
	public String getPlayerSelection(String position, Player player) {
		for (Map.Entry<String, String> m : getSelections().entrySet()) {
			if (player.getUniqueId().equals(translate(m.getKey())) 
					&& m.getKey().startsWith(position)) {
				return m.getValue();
			}
		}
		return null;
	}
	
	public void select(Player player, Position position, String portalLocation) {
		String playerSelection = position.toString() + "-" + player.getUniqueId().toString();
		getSelections().put(playerSelection, portalLocation);
		Logger.debug("Selections: " + getSelections());
	}
	
	private UUID translate(String uuidString) {
		UUID uuid = null;
		if (uuidString.startsWith("POS1-")) {
			uuid = UUID.fromString(uuidString.replace("POS1-", ""));
		}
		if (uuidString.startsWith("POS2-")) {
			uuid = UUID.fromString(uuidString.replace("POS2-", ""));
		}
		return uuid;
	}
}
