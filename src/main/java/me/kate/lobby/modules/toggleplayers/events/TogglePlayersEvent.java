package me.kate.lobby.modules.toggleplayers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.modules.Items;
import me.kate.lobby.modules.toggleplayers.Hideable;
import me.kate.lobby.modules.toggleplayers.TogglePlayers;

public class TogglePlayersEvent implements Listener {

	private Hideable playerToggle = new TogglePlayers();
	private CooldownManager cooldownManager = new CooldownManager(Main.getInstance());
	private Items items = new Items();

	private ToggleConfig playerToggleConfig = new ToggleConfig();
	private FileConfiguration hideConfig = playerToggleConfig.getConfig();
	
	private ConfigurationSection hideSection = hideConfig.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hideConfig.getConfigurationSection("item.unhide");

	private static final long MESSAGE_THRESHOLD = 1000 * 1;
	private static final long MESSAGE_ENABLE_THRESHOLD = 1000 * 1;
	private long lastMessage;
	private long lastEnableMessage;

	public TogglePlayersEvent(JavaPlugin plugin) {
	}
	
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
					player.getInventory().setItem(playerToggleConfig.getSlot(), items.unHide());
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						Messages.send(playerToggleConfig.getHideMessage(), player);
					}
				} else {
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						Messages.send(playerToggleConfig.getCooldownMessage(timeLeft), player);
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
					player.getInventory().setItem(playerToggleConfig.getSlot(), items.hide());
					long nowEnable = System.currentTimeMillis();
					if ((nowEnable - lastEnableMessage) > MESSAGE_ENABLE_THRESHOLD) {
						lastEnableMessage = nowEnable;
						Messages.send(playerToggleConfig.getUnhideMessage(), player);
					}
				} else {
					long now = System.currentTimeMillis();
					if ((now - lastMessage) > MESSAGE_THRESHOLD) {
						lastMessage = now;
						Messages.send(playerToggleConfig.getCooldownMessage(timeLeft), player);
					}
				}
				event.setCancelled(true);
			}
		}
	}
}
