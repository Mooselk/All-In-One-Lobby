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
	private final NPCBuilder builder = new NPCBuilder();
	private final PlayerCount count = new PlayerCount();

	public void updateText(String serverName, String name) {
		String playerCount = count.getPlayerCount(serverName);
		getHoloText();
		try {
			NPC npc = Main.getInstance().NPCS_OBJECT.get(name);
			String idToName = Main.getInstance().NPCINFO.get(npc.getId());
			if (idToName.equals(name)) {
				npc.setText(utils.replaceHoloText(Main.getInstance().HOLOTEXT.get(npc), playerCount));
			}
		} catch (NullPointerException e) {
			Logger.severe("One or more NPCs failed to load! " + e.getStackTrace());
			Logger.severe("Reloading NPCs...");
			e.printStackTrace();
			for (Player online : Bukkit.getOnlinePlayers()) {
				builder.build(online);
			}
		}
	}

	public void getHoloText() {
		for (final String name : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
			final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + name);
			NPC npcs = builder.getNPCById(builder.getValue(Main.getInstance().NPCINFO, name));
			if (!Main.getInstance().HOLOTEXT.containsKey(npcs)) {
				Main.getInstance().HOLOTEXT.put(npcs, utils.colorParser(section.getStringList("holotext")));
			}
		}
	}
}
