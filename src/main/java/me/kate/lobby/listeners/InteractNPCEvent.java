package me.kate.lobby.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.modules.portals.utils.SendToServer;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;

public class InteractNPCEvent implements Listener {

	private JavaPlugin plugin;
	private CooldownManager cooldownManager = new CooldownManager(plugin);
	private Config npcConfig = new NPCConfig();
	
	public InteractNPCEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {
		final Player player = event.getWhoClicked().getPlayer();
		String id = Main.getRegistry().getNPCInfo().get(event.getNPC().getId());
		int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
		if (timeLeft == 0) {
			cooldownManager.startCooldown(player, npcConfig.getConfig().getInt("cooldown"));
			for (final String npc : npcConfig.get("npcs")) {
				if (id.equalsIgnoreCase(npc)) {
					final ConfigurationSection section = npcConfig.getSection("npcs." + npc);
					String server = section.getString("server.server-name");
					sendMessages(player, section);
					if (!server.equalsIgnoreCase("none")) {
						SendToServer.send(player, server);
					}
				}
			}
		}
	}
	
	private void sendMessages(Player player, ConfigurationSection section) {
		for (String message : section.getStringList("messages")) {
			if (!message.equals("none"))
				Messages.send(message, player);
			else break;
		}
	}
}
