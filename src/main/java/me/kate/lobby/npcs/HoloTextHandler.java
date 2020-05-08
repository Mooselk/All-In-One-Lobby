package me.kate.lobby.npcs;

import java.util.Map;

import me.kate.lobby.Main;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.utils.Utils;

public class HoloTextHandler {
	
	// private Config npcConfig = new NPCConfig();

	private String preloadCount = "Loading...";
	private String offline = "0";
	
	// private final String path = "npcs.";

//	public void _updateText(String serverName, String name) {
//		String playerCount = getPlayerCount(serverName);
//		getNPCHoloText();
//		NPC npc = Main.getRegistry().getNPCObjects().get(name);
//		String idToName = Main.getRegistry().getNPCInfo().get(npc.getId());
//		
//		if (idToName.equals(name)) {
//			npc.setText(Utils.replaceHoloText(Main.getRegistry().getHoloTextFor(npc), playerCount));
//		}
//	}
	
	public void updateText(String serverName, String name) {
		LobbyNPC lobbyNpc = LobbyNPC.getByName(name);
		String playerCount = getPlayerCount(serverName);
		lobbyNpc.getNPC().setText(Utils.replaceHoloText(lobbyNpc.getHolotext(), playerCount));
	}

//	private void getNPCHoloText() {
//		for (final String name : npcConfig.get("npcs")) {
//			final ConfigurationSection section = npcConfig.getSection("npcs." + name);
//			NPC npcs = Main.getRegistry().getNPCObjects().get(name);
//			
//			if (!Main.getRegistry().getNPCHoloText().containsKey(npcs)) {
//				Main.getRegistry().getNPCHoloText().put(npcs, Utils.colorParser(section.getStringList("holotext")));
//			}
//		}
//	}

	public String getPlayerCount(String serverName) {
		boolean isOnline = false;
		Map<String, Object> placeholders = null;
		
		if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
			placeholders = Main.getInstance().getPlaceholders().get(serverName);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		if (isOnline) { return (String) placeholders.get("online"); }
		if (!isOnline) { return offline; }
		
		return preloadCount;
	}
}
