package me.kate.lobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.modules.Items;
import me.kate.lobby.modules.PlayerPotionEffects;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;

public class PlayerJoinEvents implements Listener {

	private JavaPlugin plugin;
	
	private NPCBuilder builder = new NPCBuilder(plugin);
	private PlayerSettingsConfig playerSettings = new PlayerSettingsConfig();
	private PlayerPotionEffects effects = new PlayerPotionEffects();
	
	private IUtils utils = new Utils();
	private Items items = new Items();
	
	public PlayerJoinEvents(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		
		effects.addEffect(player);
		
		if (!Main.getInstance().getConfig().getString("options.custom-joinmsg").equals("none")) {
			event.setJoinMessage(utils.replacePlayer(Main.getInstance().getConfig().getString("options.custom-joinmsg"), player));
		}
		
		if (Main.getInstance().getConfig().getBoolean("tablist.enabled")) {
			Main.getInstance().getTabList().sendHeaderFooter(player);
		}

		if (!playerSettings.sectionExists(player.getUniqueId().toString())) {
			playerSettings.createSection(player.getUniqueId().toString());
			playerSettings.getSection(player.getUniqueId().toString()).set("hidden", false);
			playerSettings.save();
			playerSettings.reload();
		}
		
		player.teleport(Spawn.toSpawn());
		
		for (final Player players : Bukkit.getOnlinePlayers()) {
			ConfigurationSection hideSection = playerSettings.getSection(players.getUniqueId().toString());
			if (hideSection != null) {
				if (hideSection.getBoolean("hidden")) {
					players.hidePlayer(player);
				}
			}
		}
		items.giveItems(player);
		builder.loadNPCsFor(player);
	}
}
