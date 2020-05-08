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
import me.kate.lobby.objects.LobbyNPC;

public class InteractNPCEvent implements Listener {

	private Config npcConfig = new NPCConfig();

	public InteractNPCEvent(JavaPlugin plugin) {
		
	}

	private CooldownManager cooldownManager = new CooldownManager(Main.getInstance());

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {

		Player player = event.getWhoClicked().getPlayer();
		LobbyNPC lobbyNPC = LobbyNPC.getByID(event.getNPC().getId());

		String npcName = lobbyNPC.getName();
		String npcServer = lobbyNPC.getServer();

		int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
		
		if (timeLeft == 0) {
			
			cooldownManager.startCooldown(player, npcConfig.getConfig().getInt("cooldown"));
			final ConfigurationSection section = npcConfig.getSection("npcs." + npcName);
			
			player.sendMessage(npcName);
			
			sendMessages(player, section);
			
			if (!npcServer.equalsIgnoreCase("none")) {
				SendToServer.send(player, npcServer);
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
