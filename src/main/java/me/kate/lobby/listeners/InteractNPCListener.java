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
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.utils.Utils;

public class InteractNPCListener implements Listener {

	private Config npcConfig;

	public InteractNPCListener(JavaPlugin plugin) {
		this.npcConfig = new NPCConfig();
	}

	private CooldownManager cooldownManager = new CooldownManager(Main.getInstance());

	@EventHandler
	public void onNPCInteract(NPCInteractEvent event) {

		Player player = event.getWhoClicked().getPlayer();
		LobbyNPC lobbyNPC = LobbyNPC.getById(event.getNPC().getId());

		int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
		
		if (timeLeft == 0) {
			
			cooldownManager.startCooldown(player, npcConfig.getConfig().getInt("cooldown"));
			final ConfigurationSection section = npcConfig.getSection("npcs." + lobbyNPC.getName());
			
			Messages.sendList(player, section);
			
			if (!lobbyNPC.getServer().equalsIgnoreCase("none"))
				Utils.send(player, lobbyNPC.getServer());
			
		}
	}
}
