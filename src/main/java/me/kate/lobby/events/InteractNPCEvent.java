package me.kate.lobby.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.items.portals.utils.SendToServer;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.utils.Messages;

public class InteractNPCEvent implements Listener {

	private final CooldownManager cooldownManager = new CooldownManager();
	private final Messages msgs = new Messages();

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {
		final Player player = event.getWhoClicked().getPlayer();
		String id = Main.NPCINFO.get(event.getNPC().getId());
		int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
		if (timeLeft == 0) {
			cooldownManager.startCooldown(player, NPCConfig.getNPCConfig().getInt("cooldown"));
			for (String npc : NPCConfig.getNPCConfig().getConfigurationSection("npcs").getKeys(false)) {
				if (id.equalsIgnoreCase(npc)) {
					ConfigurationSection section = NPCConfig.getNPCConfig().getConfigurationSection("npcs." + npc);
					String server = section.getString("server");
					String msg = ChatColor.translateAlternateColorCodes('&', section.getString("message"));
					if (!server.equalsIgnoreCase("none")) {
						SendToServer.send(player, server);
					}
					if (!msg.equalsIgnoreCase("none")) {
						msgs.send(msg, player);
					}
				}
			}
		}
	}
}
