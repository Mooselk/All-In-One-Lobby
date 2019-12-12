package me.kate.lobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.modules.Spawn;
import me.kate.lobby.modules.toggleplayers.Hideable;
import me.kate.lobby.modules.toggleplayers.TogglePlayers;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class PlayerJoinEvents extends NPCBuilder implements Listener {

	private Config selectorConfig = new SelectorConfig();
	private ToggleConfig playerToggleConfig = new ToggleConfig();
	private Hideable playerToggle = new TogglePlayers();
	private PlayerSettingsConfig playerSettings = new PlayerSettingsConfig();
	
	private final IUtils utils = new Utils();
	
	private ConfigurationSection togglePlayersHide = playerToggleConfig.getConfig().getConfigurationSection("item.hide");
	private ConfigurationSection togglePlayersUnhide = playerToggleConfig.getConfig().getConfigurationSection("item.unhide");
	
	public PlayerJoinEvents(JavaPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		
		if (!Main.getInstance().getConfig().getString("options.custom-joinmsg").equals("none")) {
			event.setJoinMessage(utils.replacePlayer(Main.getInstance().getConfig().getString("options.custom-joinmsg"), player));
		}
		
		if (Main.getInstance().getConfig().getBoolean("tablist.enabled")) {
			Main.getInstance().getTabList().sendHeaderFooter(player);
		}

		if (!playerSettings.sectionExists(player.getUniqueId().toString())) {
			playerSettings.createSection(player.getUniqueId().toString());
			playerSettings.getConfig().getConfigurationSection(player.getUniqueId().toString()).set("hidden", false);
			playerSettings.save();
			playerSettings.reload();
		}
		
		player.teleport(Spawn.toSpawn());
		
		final Player login = (Player) event.getPlayer();
		ConfigurationSection hSection;
		for (final Player players : Bukkit.getOnlinePlayers()) {
			hSection = playerSettings.getConfig().getConfigurationSection(players.getUniqueId().toString());
			if (hSection != null) {
				if (hSection.getBoolean("hidden")) {
					players.hidePlayer(login);
				}
			}
		}
		giveItems(player);
		loadNPCsFor(player);
	}

//	@EventHandler
//	public void hideOnJoin(final PlayerJoinEvent event) {
//		
//	}

	private final ItemStack hide = new ItemBuilder(Material.getMaterial(togglePlayersHide.getString("material")), 1)
			.setName(ChatColor.translateAlternateColorCodes('&', togglePlayersHide.getString("name")))
			.toItemStack();
	
	private final ItemStack unhide = new ItemBuilder(Material.getMaterial(togglePlayersUnhide.getString("material")))
			.setName(ChatColor.translateAlternateColorCodes('&', togglePlayersUnhide.getString("name")))
			.toItemStack();
	
	private final ItemStack selector = new ItemBuilder(
			Material.getMaterial(selectorConfig.getConfig().getConfigurationSection("selector.options").getString("material")))
					.setName(utils.color(selectorConfig.getConfig().getConfigurationSection("selector.options").getString("item-name")))
					.setLore(utils.colorParser(selectorConfig.getConfig().getConfigurationSection("selector.options").getStringList("lore")))
					.toItemStack();

	
	private void giveItems(final Player player) {
		final ConfigurationSection hSection = playerToggleConfig.getConfig().getConfigurationSection(player.getUniqueId().toString());
		if (hSection != null) {
			if (!hSection.getBoolean("hidden")) {
				player.getInventory().setItem(2, hide);
			}
			if (hSection.getBoolean("hidden")) {
				player.getInventory().setItem(2, unhide);
				playerToggle.setHidden(true, player);
			}
		} else {
			player.getInventory().setItem(2, hide);
		}
		if (selectorConfig.getConfig().getConfigurationSection("selector.options").getBoolean("enabled")) {
			ConfigurationSection section = selectorConfig.getConfig().getConfigurationSection("selector.options");
			if (!player.getInventory().contains(Material.getMaterial(section.getString("material")), 1)) {
				player.getInventory().setItem(section.getInt("slot"), selector);
			}
		}
	}
}
