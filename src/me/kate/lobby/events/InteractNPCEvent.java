package me.kate.lobby.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;

public class InteractNPCEvent implements Listener {

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {
		Player p = event.getWhoClicked().getPlayer();
		String id = Main.NPCINFO.get(event.getNPC().getId());
		for (String npc : NPCFile.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
			if (id.equalsIgnoreCase(npc)) {
				ConfigurationSection section = NPCFile.getNPCConfig().getConfigurationSection("npcs." + npc);
				String command = section.getString("command");
				String msg = ChatColor.translateAlternateColorCodes('&', section.getString("message"));
				if (!command.equalsIgnoreCase("none")) {
					p.performCommand(command);
				}
				if (!msg.equalsIgnoreCase("none")) {
					p.sendMessage(msg);
				}
			}
		}
	}
}
