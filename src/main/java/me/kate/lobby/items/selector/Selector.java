package me.kate.lobby.items.selector;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.utils.replace.IUtils;
import me.kate.lobby.utils.replace.Utils;

public class Selector {

	private BukkitTask refreshTimer;
	
	private ISelectorSettings sf = new SelectorFile();
	private FileConfiguration c = sf.getSelectorFile();
	private Inventory inv = Bukkit.createInventory(null, c.getInt("selector.options.rows") * 9,
			ChatColor.translateAlternateColorCodes('&', c.getString("selector.options.name")));

	private IUtils u = new Utils();

	public Selector() {
		this.update();
	}

	public void open(Player player) {
		this.inv.clear();
		this.update();
		player.openInventory(inv);
		refreshTimer = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
			this.update();
		}, 1 * 20, 1 * 20);
		Main.TASKS.put(player.getUniqueId(), refreshTimer);
	}

	public void close(Player player) {
		BukkitTask bukkitTask = Main.TASKS.remove(player.getUniqueId());
		if (bukkitTask != null) {
			bukkitTask.cancel();
		}
		player.closeInventory();
	}

	public void onClose(Player player) {
		BukkitTask bukkitTask = Main.TASKS.remove(player.getUniqueId());
		if (bukkitTask != null) {
			bukkitTask.cancel();
		}
	}

	public void update() {
		for (final String key : c.getConfigurationSection("selector").getKeys(false)) {
			final ItemStack i = new ItemStack(Material.AIR);
			if (!key.equals("options")) {
				final ConfigurationSection section = c.getConfigurationSection("selector." + key);
				if (!section.getBoolean("decoration")) {
					List<String> lore = null;
					if (section.getBoolean("server.ping-server")) {
						String serverName = section.getString("server.server-id");
						Map<String, Object> placeholders = null;
						boolean isOnline;
						if (Main.SERVER_PLACEHOLDERS.containsKey(serverName)) {
							placeholders = Main.SERVER_PLACEHOLDERS.get(serverName);
							isOnline = (boolean) placeholders.get("isOnline");
						} else {
							isOnline = false;
						}
						if (isOnline) {
							String olineplayers = (String) placeholders.get("online");
							String maxplayers = (String) placeholders.get("max");
							long serverping = (long) placeholders.get("ping");
							int online = Integer.valueOf(olineplayers);
							int max = Integer.valueOf(maxplayers);
							int ping = (int) serverping;
							int slot = Integer.valueOf(key);
							lore = section.getStringList("online.lore");
							inv.setItem(slot,
									u.itemStackBuilder(section.getString("online.material"), i,
											u.replace(section.getString("online.name"), section, max, online, ping),
											u.replaceLore(lore, max, online, ping, false),
											section.getBoolean("online.enchanted"), section.getInt("online.byte")));
						}
						if (!isOnline) {
							int slot = Integer.valueOf(key);
							lore = section.getStringList("offline.lore");
							inv.setItem(slot,
									u.itemStackBuilder(section.getString("offline.material"), i,
											u.replace(section.getString("offline.name"), section, 0, 0, 0),
											u.replaceLore(lore, 0, 0, 0, false),
											section.getBoolean("offline.enchanted"), section.getInt("offline.byte")));
						}
					} else {
						int slot = Integer.valueOf(key);
						lore = section.getStringList("offline.lore");
						inv.setItem(slot,
								u.itemStackBuilder(section.getString("offline.material"), i,
										section.getString("offline.name"), u.replaceLore(lore, 0, 0, 0, true),
										section.getBoolean("offline.enchanted"), section.getInt("offline.byte")));
					}
				}
				if (section.getBoolean("decoration")) {
					int slot = Integer.valueOf(key);
					int type = section.getInt("byte");
					ItemStack item = new ItemStack(Material.getMaterial(section.getString("material")), 1,
							(short) type);
					ItemMeta im = item.getItemMeta();
					im.setDisplayName(section.getString("name"));
					item.setItemMeta(im);
					inv.setItem(slot, item);
				}
			}
		}
	}
}