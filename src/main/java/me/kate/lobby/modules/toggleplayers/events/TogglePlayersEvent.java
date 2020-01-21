package me.kate.lobby.modules.toggleplayers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Messages;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.modules.toggleplayers.Hideable;
import me.kate.lobby.modules.toggleplayers.TogglePlayers;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class TogglePlayersEvent implements Listener {

	private Hideable playerToggle = new TogglePlayers();
	private final CooldownManager cooldownManager = new CooldownManager();
	private final Messages msgs = new Messages();

	private final IUtils utils = new Utils();

	private ToggleConfig playerToggleConfig = new ToggleConfig();
	private FileConfiguration hideConfig = playerToggleConfig.getConfig();
	private SelectorConfig selectorConfig = new SelectorConfig();
	
	private FileConfiguration selectorConf = selectorConfig.getConfig();
	private ConfigurationSection hideSection = hideConfig.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hideConfig.getConfigurationSection("item.unhide");

	private static final long MESSAGE_THRESHOLD = 1000 * 1;
	private static final long MESSAGE_ENABLE_THRESHOLD = 1000 * 1;
	private long lastMessage;
	private long lastEnableMessage;

	private ItemStack hide = new ItemBuilder(Material.getMaterial(playerToggleConfig.getHideMaterial()), 1)
			.setName(utils.color(playerToggleConfig.getHideDisplayName()))
			.toItemStack();
	
	private ItemStack unhide = new ItemBuilder(Material.getMaterial(playerToggleConfig.getUnHideMaterial()))
			.setName(utils.color(playerToggleConfig.getUnhideDisplayName()))
			.toItemStack();
	
	private ItemStack selector = new ItemBuilder(
			Material.getMaterial(selectorConf.getConfigurationSection("selector.options").getString("material")))
					.setName(utils.color(selectorConf.getConfigurationSection("selector.options").getString("item-name")))
					.setLore(utils.colorParser(selectorConf.getConfigurationSection("selector.options").getStringList("lore")))
					.toItemStack();

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		ConfigurationSection hSection = null;
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.getItemInHand().getType().equals(Material.getMaterial(hideSection.getString("material")))) {
				int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
				long now = System.currentTimeMillis();
				if (timeLeft == 0) {
					cooldownManager.startCooldown(player, playerToggleConfig.getCooldownLength());
					playerToggle.hide(player, hSection);
					player.getInventory().setItem(playerToggleConfig.getSlot(), unhide);
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						msgs.send(playerToggleConfig.getHideMessage(), player);
					}
				} else {
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						msgs.send(playerToggleConfig.getCooldownMessage(timeLeft), player);
					}
				}
				event.setCancelled(true);
			}
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.getItemInHand().getType().equals(Material.getMaterial(unhideSection.getString("material")))) {
				int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
				if (timeLeft == 0) {
					cooldownManager.startCooldown(player, playerToggleConfig.getCooldownLength());
					playerToggle.unhide(player, hSection);
					player.getInventory().setItem(playerToggleConfig.getSlot(), hide);
					long nowEnable = System.currentTimeMillis();
					if ((nowEnable - lastEnableMessage) > MESSAGE_ENABLE_THRESHOLD) {
						lastEnableMessage = nowEnable;
						msgs.send(playerToggleConfig.getUnhideMessage(), player);
					}
				} else {
					long now = System.currentTimeMillis();
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						msgs.send(playerToggleConfig.getCooldownMessage(timeLeft), player);
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (item == null) { return; }
		if (item.equals(selector)) { event.setCancelled(true); }
		if (item.equals(hide)) { event.setCancelled(true); }
		if (item.equals(unhide)) { event.setCancelled(true); }
	}
}
