package me.kate.lobby.npcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;

public class HoloTextHandler {

	private final IUtils utils = new Utils();
	private Config npcConfig = new NPCConfig();

	private List<String> text = null;

	private String preloadCount = "Loading...";
	private String offline = "0";

	public void updateText(String serverName, String name) {
		String playerCount = getPlayerCount(serverName);
		getNPCHoloText();
		NPC npc = Main.getRegistry().getNPCObjects().get(name);
		String idToName = Main.getRegistry().getNPCInfo().get(npc.getId());
		if (idToName.equals(name)) {
			npc.setText(this.replaceHoloText(Main.getRegistry().getHoloTextFor(npc), playerCount));
		}
	}

	private void getNPCHoloText() {
		for (final String name : npcConfig.get("npcs")) {
			final ConfigurationSection section = npcConfig.getSection("npcs." + name);
			NPC npcs = Main.getRegistry().getNPCObjects().get(name);
			if (!Main.getRegistry().getNPCHoloText().containsKey(npcs)) {
				Main.getRegistry().getNPCHoloText().put(npcs, utils.colorParser(section.getStringList("holotext")));
			}
		}
	}

	private List<String> replaceHoloText(List<String> list, String players) {
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}

	public String getPlayerCount(String serverName) {
		boolean isOnline = false;
		Map<String, Object> placeholders = null;
		if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
			placeholders = Main.getInstance().getPlaceholders().get(serverName);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		if (isOnline) {
			return (String) placeholders.get("online");
		}
		if (!isOnline) {
			return offline;
		}
		return preloadCount;
	}
}
