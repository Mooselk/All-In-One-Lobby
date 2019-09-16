package me.kate.lobby.items.hideplayers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.items.hideplayers.HidePlayers;
import me.kate.lobby.items.hideplayers.interfaces.Hideable;
import me.kate.lobby.managers.CooldownManager;
import me.kate.lobby.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class HidePlayersInteractEvent implements Listener {

	private Hideable h = new HidePlayers();
	private IHidePlayerSettings hideSettings = new HidePlayersFile();
	private final CooldownManager cooldownManager = new CooldownManager();

	private IHidePlayerSettings hf = new HidePlayersFile();
	private FileConfiguration hc = hf.getHideSettings();

	private IPlayerSettings ps = new PlayerSettingsFile();

	private ConfigurationSection hideSection = hc.getConfigurationSection("item.hide");
	private ConfigurationSection unhideSection = hc.getConfigurationSection("item.unhide");

	private static final long MESSAGE_THRESHOLD = 1000 * 1;
	private static final long MESSAGE_ENABLE_THRESHOLD = 1000 * 1;
	private long lastMessage;
	private long lastEnableMessage;

	private ItemStack hide;
	private ItemStack unhide;

	@EventHandler
	public void onInteract(final PlayerInteractEvent e) {

		final Player p = e.getPlayer();

		this.hide = new ItemBuilder(Material.getMaterial(hideSection.getString("material")), 1)
				.setName(ChatColor.translateAlternateColorCodes('&', hideSection.getString("name"))).toItemStack();
		this.unhide = new ItemBuilder(Material.getMaterial(unhideSection.getString("material")))
				.setName(ChatColor.translateAlternateColorCodes('&', unhideSection.getString("name"))).toItemStack();

		ConfigurationSection hSection = null;

		if (p.getItemInHand().getType().equals(Material.getMaterial(hideSection.getString("material")))) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			long now = System.currentTimeMillis();
			if (timeLeft == 0) {
				this.startCooldown(p);
				hSection = ps.getPlayerSettings().getConfigurationSection(p.getUniqueId().toString());
				hSection.set("hidden", true);
				ps.save();
				h.hide(p);
				p.getInventory().setItem(2, unhide);
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getHideMessage()));
				}
			} else {
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', hideSettings.getCooldownMessage(timeLeft)));
				}
			}
			e.setCancelled(true);
		}

		if (p.getItemInHand().getType().equals(Material.getMaterial(unhideSection.getString("material")))) {
			int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
			if (timeLeft == 0) {
				this.startCooldown(p);
				hSection = ps.getPlayerSettings().getConfigurationSection(p.getUniqueId().toString());
				hSection.set("hidden", false);
				ps.save();
				h.unhide(p);
				p.getInventory().setItem(2, hide);
				long nowEnable = System.currentTimeMillis();
				if ((nowEnable - lastEnableMessage) > MESSAGE_ENABLE_THRESHOLD) {
					lastEnableMessage = nowEnable;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', hideSettings.getUnhideMessage()));
				}
			} else {
				long now = System.currentTimeMillis();
				if ((now - lastMessage) > MESSAGE_THRESHOLD) {
					lastMessage = now;
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', hideSettings.getCooldownMessage(timeLeft)));
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
