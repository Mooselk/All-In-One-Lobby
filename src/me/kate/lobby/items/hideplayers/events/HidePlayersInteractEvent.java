package me.kate.lobby.items.hideplayers.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.items.hideplayers.HidePlayers;
import me.kate.lobby.items.hideplayers.interfaces.Hideable;
import me.kate.lobby.managers.CooldownManager;
import net.md_5.bungee.api.ChatColor;

public class HidePlayersInteractEvent implements Listener {

	private Hideable h = new HidePlayers();
	private IHidePlayerSettings hideSettings = new HidePlayersFile();
	private final CooldownManager cooldownManager = new CooldownManager();
	private static final long MESSAGE_THRESHOLD = 1000 * 1;
	private static final long MESSAGE_ENABLE_THRESHOLD = 1000 * 1;
	private long lastMessage;
	private long lastEnableMessage;

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON)) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			long now = System.currentTimeMillis();
			if (timeLeft == 0) {
				this.startCooldown(p);
				h.hide(p);
				ItemStack item = p.getItemInHand();
				item.setType(Material.LEVER);
				p.getInventory().setItem(2, item);
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getHideMessage()));
				}
			} else {
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getCooldownMessage(timeLeft)));
				}
			}
			e.setCancelled(true);
		}
		if (p.getItemInHand().getType().equals(Material.LEVER)) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			if (timeLeft == 0) {
				this.startCooldown(p);
				h.unhide(p);
				ItemStack item = p.getItemInHand();
				item.setType(Material.REDSTONE_TORCH_ON);
				p.getInventory().setItem(2, item);
				p.getInventory().setItem(2, item);
				long nowEnable = System.currentTimeMillis();
				if ((nowEnable - lastEnableMessage) > MESSAGE_ENABLE_THRESHOLD) {
					lastEnableMessage = nowEnable;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getUnhideMessage()));
				}
			} else {
				long now = System.currentTimeMillis();
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getCooldownMessage(timeLeft)));
				}
			}
			e.setCancelled(true);
		}
	}

	public void startCooldown(Player p) {
		cooldownManager.setCooldown(p.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
		new BukkitRunnable() {
			@Override
			public void run() {
				int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
				cooldownManager.setCooldown(p.getUniqueId(), --timeLeft);
				if (timeLeft == 0) {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
	}
}
