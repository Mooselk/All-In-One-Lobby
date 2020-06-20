package me.kate.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.modules.Items;
import me.kate.lobby.modules.PlayerPotionEffects;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.Utils;

public class PlayerJoinListener implements Listener {

	private Main plugin;
	private Items items;
	private PluginConfig config;
	private NPCBuilder builder;
	private PlayerPotionEffects effects;
	private Messages messages;
	
	public PlayerJoinListener(Main plugin) {
		this.plugin = plugin;
		this.items = new Items();
		this.config = new PluginConfig();
		this.builder = new NPCBuilder(plugin);
		this.effects = new PlayerPotionEffects();
		this.messages = new Messages();
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		
		String joinMSG = plugin.getConfig().getString("options.custom-joinmsg");
		
		if (joinMSG.equals("none") || joinMSG.equals("")) {
			event.setJoinMessage(null);
		} else {
			event.setJoinMessage(Utils.replacePlayer(joinMSG, player));
		}
		
		if (plugin.getConfig().getBoolean("tablist.enabled")) {
			plugin.getTabList().sendHeaderFooter(player);
		}
		
		// this.sendJoinMessage(player);
		
		player.teleport(Spawn.getSpawn());
		
		items.giveItems(player);
		effects.addEffect(player);
		builder.loadNPCsFor(player);
		
	}
	
	public void sendJoinMessage(Player player) {
		if (!config.motdIsEnabled())
			return;
		
		for (String motd : config.getJoinMOTD()) {
			messages.send(motd.replaceAll("%username%", player.getName()), player);
		}
	}
}
