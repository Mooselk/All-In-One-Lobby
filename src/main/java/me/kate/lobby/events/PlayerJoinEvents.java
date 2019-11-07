package me.kate.lobby.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.HidePlayersConfig;
import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.Spawn;
import me.kate.lobby.items.toggleplayers.Hideable;
import me.kate.lobby.items.toggleplayers.TogglePlayers;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class PlayerJoinEvents implements Listener {

	private ISelectorSettings selectorFile = new SelectorConfig();
	private IHidePlayerSettings hideFile = new HidePlayersConfig();
	private Hideable playerToggle = new TogglePlayers();
	private FileConfiguration selectorConf = selectorFile.getSelectorFile();
	private FileConfiguration hideConf = hideFile.getHideSettings();
	private IPlayerSettings playerSettings = new PlayerSettingsConfig();
	private final IUtils utils = new Utils();

	private NPCBuilder builder = new NPCBuilder();

	private ConfigurationSection hideSection = hideConf.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hideConf.getConfigurationSection("item.unhide");
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!Main.getInstance().getConfig().getString("options.custom-joinmsg").equals("none")) {
			event.setJoinMessage(utils.replacePlayer(Main.getInstance().getConfig().getString("options.custom-joinmsg"), player));
		}
		
		builder.load(player);

		if (!playerSettings.sectionExists(player.getUniqueId().toString())) {
			playerSettings.createSection(player.getUniqueId().toString());
			playerSettings.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString()).set("hidden", false);
			playerSettings.save();
			playerSettings.reload();
		}
		player.teleport(Spawn.toSpawn());
	}

	@EventHandler
	public void hideOnJoin(final PlayerJoinEvent event) {
		final Player login = (Player) event.getPlayer();
		ConfigurationSection hSection;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			hSection = playerSettings.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
			if (hSection.getBoolean("hidden")) {
				player.hidePlayer(login);
			}
		}
	}

	private final ItemStack hide = new ItemBuilder(Material.getMaterial(hideSection.getString("material")), 1)
			.setName(ChatColor.translateAlternateColorCodes('&', hideSection.getString("name")))
			.toItemStack();
	
	private final ItemStack unhide = new ItemBuilder(Material.getMaterial(unhideSection.getString("material")))
			.setName(ChatColor.translateAlternateColorCodes('&', unhideSection.getString("name")))
			.toItemStack();
	
	private final ItemStack selector = new ItemBuilder(
			Material.getMaterial(selectorConf.getConfigurationSection("selector.options").getString("material")))
					.setName(ChatColor.translateAlternateColorCodes('&', selectorConf.getConfigurationSection("selector.options").getString("item-name")))
					.setLore(utils.colorParser(selectorConf.getConfigurationSection("selector.options").getStringList("lore")))
					.toItemStack();

	@EventHandler
	public void giveItemsOnJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		final ConfigurationSection hSection = playerSettings.getPlayerSettings().getConfigurationSection(player.getUniqueId().toString());
		if (!hSection.getBoolean("hidden")) {
			player.getInventory().setItem(2, hide);
		}
		if (hSection.getBoolean("hidden")) {
			player.getInventory().setItem(2, unhide);
			playerToggle.setHidden(true, player);
		}
		if (selectorConf.getConfigurationSection("selector.options").getBoolean("enabled")) {
			ConfigurationSection section = selectorConf.getConfigurationSection("selector.options");
			if (!player.getInventory().contains(Material.getMaterial(section.getString("material")), 1)) {
				player.getInventory().setItem(section.getInt("slot"), selector);
			}
		}
	}
}
