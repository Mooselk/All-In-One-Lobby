package me.kate.lobby.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.npcs.api.events.NPCHideEvent;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.utils.Utils;

public class InteractNPCListener implements Listener {

	private Main plugin;
	private NPCConfig npcConfig;
	private Messages messages;
	
	public InteractNPCListener(Main plugin) {
		this.plugin = plugin;
		this.npcConfig = new NPCConfig(plugin);
		this.messages = new Messages();
	}

	private CooldownManager cooldownManager = new CooldownManager(plugin);

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {

		Player player = event.getWhoClicked().getPlayer();
		LobbyNPC lobbyNPC = LobbyNPC.getLobbyNPC(event.getNPC().getId(), LobbyNPC.Get.ID);

		int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
		
		if (timeLeft == 0) {
			
			cooldownManager.startCooldown(player, npcConfig.getCooldown());
			final ConfigurationSection section = npcConfig.getSection("npcs." + lobbyNPC.getName());
			
			messages.sendList(player, section);
			
			if (!lobbyNPC.getServer().equalsIgnoreCase("none"))
				Utils.send(player, lobbyNPC.getServer());
			
		}
	}
	
	@EventHandler
	public void onAutoHide(final NPCHideEvent event) {
		
		if (!npcConfig.autoHideNPCs()) {
			return;
		}
		
		if (event.isAutomatic()) {
			event.setCancelled(true);
		}
		
	}
}
