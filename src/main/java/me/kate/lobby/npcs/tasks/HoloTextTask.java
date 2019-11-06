package me.kate.lobby.npcs.tasks;

import org.bukkit.configuration.ConfigurationSection;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.PlayerCount;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class HoloTextTask {

	private final IUtils utils = new Utils();
	private PlayerCount count = new PlayerCount();

	public void updateText(String serverName, String name) {
		String playerCount = count.getPlayerCount(serverName);
		getHoloText();
		try {
			NPC npc = Main.getRegistry().getNPCObjects().get(name);
			String idToName = Main.getRegistry().getNPCInfo().get(npc.getId());
			if (idToName.equals(name)) {
				npc.setText(utils.replaceHoloText(Main.getRegistry().getNPCHoloText().get(npc), playerCount));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void getHoloText() {
		for (final String name : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
			final ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + name);
			NPC npcs = Main.getRegistry().getNPCObjects().get(name);
			if (!Main.getRegistry().getNPCHoloText().containsKey(npcs)) {
				Main.getRegistry().getNPCHoloText().put(npcs, utils.colorParser(section.getStringList("holotext")));
			}
		}
	}
}
