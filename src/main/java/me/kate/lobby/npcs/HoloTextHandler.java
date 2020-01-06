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
	
	public void updateText(String serverName, String name) {
		String playerCount = getPlayerCount(serverName);
		getHoloText();
		try {
			NPC npc = Main.getInstance().getRegistry().getNPCObjects().get(name);
			String idToName = Main.getInstance().getRegistry().getNPCInfo().get(npc.getId());
			if (idToName.equals(name)) {
				npc.setText(replaceHoloText(Main.getInstance().getRegistry().getNPCHoloText().get(npc), playerCount));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void getHoloText() {
		for (final String name : npcConfig.getConfig().getConfigurationSection("npcs").getKeys(false)) {
			final ConfigurationSection section = npcConfig.getConfig().getConfigurationSection("npcs." + name);
			NPC npcs = Main.getInstance().getRegistry().getNPCObjects().get(name);
			if (!Main.getInstance().getRegistry().getNPCHoloText().containsKey(npcs)) {
				Main.getInstance().getRegistry().getNPCHoloText().put(npcs, utils.colorParser(section.getStringList("holotext")));
			}
		}
	}
	
	private List<String> replaceHoloText(List<String> list, String players) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}
	
	
	
	public String getPlayerCount(String serverName) {
		final String count = "Loading...";
		final String offline = "0";
		boolean isOnline = false;
		Map<String, Object> placeholders = null;
		if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
			placeholders = Main.getInstance().getPlaceholders().get(serverName);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		if (isOnline) {
//			Logger.debug("Online: " + (String) placeholders.get("online") + " server: " + serverName);
//			Logger.debug("  " + Main.getInstance().getPlaceholders());
			return (String) placeholders.get("online");
		} 
		if (!isOnline) {
			return offline;
		}
		return count;
	}
}
