package me.kate.lobby.npcs.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.npcs.PlayerCount;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class HoloTextTask {

	private final IUtils utils = new Utils();
	private NPCBuilder builder = new NPCBuilder();
	private PlayerCount count = new PlayerCount();

	public void updateText(String serverName, String name) {
		this.getHoloText();
		try {
			NPC npc = Main.NPCS_OBJECT.get(name);
			String idToName = Main.NPCINFO.get(npc.getId());
			String playerCount = count.getPlayerCount(serverName, PingType.EXTERNAL);
			if (idToName.equals(name)) {
				Logger.debug("NPC: " + name + " PlayerCount: " + playerCount);
				npc.setText(utils.replaceHoloText(Main.HOLOTEXT.get(npc), playerCount));
			}
		} catch (NullPointerException e) {
			Logger.severe("One or more NPCs failed to load! " + e.getStackTrace());
			Logger.severe("Reloading NPCs...");
			for (Player online : Bukkit.getOnlinePlayers()) {
				builder.build(online, true);
			}
		}
	}

	public void getHoloText() {
		for (String name : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
			final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + name);
			NPC npcs = builder.getNPCById(builder.getValue(Main.NPCINFO, name));
			if (!Main.HOLOTEXT.containsKey(npcs)) {
				Main.HOLOTEXT.put(npcs, utils.colorParser(section.getStringList("holotext")));
			}
		}
	}
}
