package me.kate.lobby.items.toggleplayers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.data.files.HidePlayersConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.items.toggleplayers.Hideable;
import me.kate.lobby.items.toggleplayers.TogglePlayers;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Messages;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;
import net.md_5.bungee.api.ChatColor;

public class TogglePlayersEvent implements Listener {

	private Hideable playerToggle = new TogglePlayers();
	private IHidePlayerSettings hideSettings = new HidePlayersConfig();
	private final CooldownManager cooldownManager = new CooldownManager();
	private final Messages msgs = new Messages();
	
	private final IUtils utils = new Utils();

	private IHidePlayerSettings hideFile = new HidePlayersConfig();
	private FileConfiguration hideConfig = hideFile.getHideSettings();

	private ISelectorSettings selectorFile = new SelectorConfig();
	private FileConfiguration selectorConf = selectorFile.getSelectorFile();
	private ConfigurationSection hideSection = hideConfig.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hideConfig.getConfigurationSection("item.unhide");

	private static final long MESSAGE_THRESHOLD = 1000 * 1;
	private static final long MESSAGE_ENABLE_THRESHOLD = 1000 * 1;
	private long lastMessage;
	private long lastEnableMessage;

	private ItemStack hide = new ItemBuilder(Material.getMaterial(
			hideSection.getString("material")), 1)
			.setName(ChatColor.translateAlternateColorCodes('&', 
			hideSection.getString("name")))
			.toItemStack();
	private ItemStack unhide = new ItemBuilder(Material.getMaterial(
			unhideSection.getString("material")))
			.setName(ChatColor.translateAlternateColorCodes('&', 
			unhideSection.getString("name")))
			.toItemStack();
	private ItemStack selector = new ItemBuilder(Material.getMaterial(
			selectorConf.getConfigurationSection("selector.options").getString("material")))
			.setName(ChatColor.translateAlternateColorCodes('&', selectorConf.getConfigurationSection("selector.options").getString("item-name")))
			.setLore(utils.colorParser(selectorConf.getConfigurationSection("selector.options").getStringList("lore")))
			.toItemStack();

	@EventHandler
	public void onInteract(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ConfigurationSection hSection = null;

		if (p.getItemInHand().getType().equals(Material.getMaterial(hideSection.getString("material")))) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			long now = System.currentTimeMillis();
			if (timeLeft == 0) {
				cooldownManager.startCooldown(p, hideFile.getCooldownLength());
				playerToggle.hide(p, hSection);
				p.getInventory().setItem(2, unhide);
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					msgs.send(hideSettings.getHideMessage(), p);
				}
			} else {
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					msgs.send(hideSettings.getCooldownMessage(timeLeft), p);
				}
			}
			e.setCancelled(true);
		}

		if (p.getItemInHand().getType().equals(Material.getMaterial(unhideSection.getString("material")))) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			if (timeLeft == 0) {
				cooldownManager.startCooldown(p, hideFile.getCooldownLength());
				playerToggle.unhide(p, hSection);
				p.getInventory().setItem(2, hide);
				long nowEnable = System.currentTimeMillis();
				if ((nowEnable - lastEnableMessage) > MESSAGE_ENABLE_THRESHOLD) {
					lastEnableMessage = nowEnable;
					msgs.send(hideSettings.getUnhideMessage(), p);
				}
			} else {
				long now = System.currentTimeMillis();
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					msgs.send(hideSettings.getCooldownMessage(timeLeft), p);
				}
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		ItemStack i = e.getCurrentItem();
		if (i == null) {
			return;
		}
		if (i.equals(selector)) {
			e.setCancelled(true);
		}
		if (i.equals(hide)) {
			e.setCancelled(true);
		}
		if (i.equals(unhide)) {
			e.setCancelled(true);
		}
	}
}
