package me.kate.lobby.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.items.portals.SendToServer;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;

public class InteractNPCEvent implements Listener {

	private final CooldownManager cooldownManager = new CooldownManager();

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {
		final Player p = event.getWhoClicked().getPlayer();
		String id = Main.NPCINFO.get(event.getNPC().getId());
		int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
		if (timeLeft == 0) {
			cooldownManager.startCooldown(p, NPCFile.getNPCConfig().getInt("cooldown"));
			for (String npc : NPCFile.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
				if (id.equalsIgnoreCase(npc)) {
					ConfigurationSection section = NPCFile.getNPCConfig().getConfigurationSection("npcs." + npc);
					String server = section.getString("server");
					String msg = ChatColor.translateAlternateColorCodes('&', section.getString("message"));
					if (!server.equalsIgnoreCase("none")) {
						SendToServer.send(p, server);
					}
					if (!msg.equalsIgnoreCase("none")) {
						p.sendMessage(msg);
					}
				}
			}
		}
	}
}
