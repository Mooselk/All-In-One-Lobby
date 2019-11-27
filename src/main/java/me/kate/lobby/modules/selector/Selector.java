package me.kate.lobby.modules.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.kate.lobby.Main;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class Selector {


	private final ISelectorSettings selectorFile = new SelectorConfig();
	private Inventory inv = Bukkit.createInventory(null,
			selectorFile.getSelectorFile().getInt("selector.options.rows") * 9, ChatColor.translateAlternateColorCodes(
					'&', selectorFile.getSelectorFile().getString("selector.options.name")));
	
	private Map <Integer, ItemStack> itemsOnline = new HashMap<>();
	private Map <Integer, ItemStack> itemsOffline = new HashMap<>();
	private Map <Integer, ItemStack> itemsDeco = new HashMap<>();
	private final IUtils utils = new Utils();
	private int instances = 1;

	public Selector() {
		Logger.severe("Created new instance of Selector (" + instances + ")");
		this.loadOnline();
		this.loadOffline();
		this.loadDeco();
	}

	public void open(Player player) {
		this.update();
		player.openInventory(inv);
	}

	public void close(Player player) {
		player.closeInventory();
	}
	
	public void loadDeco() {
		if (itemsDeco.isEmpty()) {
			for (final String key : selectorFile.getSelectorFile().getConfigurationSection("selector").getKeys(false)) {
				if (!key.equals("options")) {
					final ConfigurationSection section = selectorFile
							.getSelectorFile()
							.getConfigurationSection("selector." + key);
					if (section.getBoolean("decoration")) {
						final int slot = Integer.valueOf(key);
						final String name = section.getString("name");
						final Material material = Material.getMaterial(section.getString("material"));
						final short data = (short) section.getInt("byte");
						final ItemStack item = new ItemBuilder(material).setName(name).setDurability(data).toItemStack();
						inv.setItem(slot, item);
					}
				}
			}
		}
	}
	
	public void loadOffline() {
		if (itemsOffline.isEmpty()) {
			for (final String key : selectorFile.getSelectorFile().getConfigurationSection("selector").getKeys(false)) {
				if (!key.equals("options")) {
					final ConfigurationSection section = selectorFile
							.getSelectorFile()
							.getConfigurationSection("selector." + key);
					if (section.getBoolean("decoration")) {
						continue;
					}
					final int slot = Integer.valueOf(key);
					final String name = section.getString("offline.name");
					final List<String> lore = section.getStringList("offline.lore");
					final Material material = Material.getMaterial(section.getString("offline.material"));
					final short data = (short) section.getInt("offline.byte");
					final ItemStack item = new ItemBuilder(material).setName(utils.replace(name, 0, 0, 0)).setLore(utils.replaceLore(lore, 0, 0)).setDurability(data).toItemStack();
					itemsOffline.put(slot, item);
				}
			}
		}
	}
	
	public void loadOnline() {
		if (itemsOnline.isEmpty()) {
		for (final String key : selectorFile.getSelectorFile().getConfigurationSection("selector").getKeys(false)) {
			if (!key.equals("options")) {
				final ConfigurationSection section = selectorFile
						.getSelectorFile()
						.getConfigurationSection("selector." + key);
				if (section.getBoolean("decoration")) {
					continue;
				}
					final int slot = Integer.valueOf(key);
					final String name = section.getString("online.name");
					final Material material = Material.getMaterial(section.getString("online.material"));
					final List<String> lore = section.getStringList("online.lore");
					final boolean enchanted = section.getBoolean("online.enchanted");
					final ItemStack item = new ItemBuilder(material)
							.setName(name)
							.setLore(lore)
							.setEnchanted(enchanted)
							.toItemStack();
					itemsOnline.put(slot, item);
				}
			}
		}
	}

	public void update() {
		for (Map.Entry<Integer, ItemStack> map : itemsOnline.entrySet()) {
			int slot = map.getKey();
			ItemStack itemstack = map.getValue();
			final ConfigurationSection section = selectorFile
					.getSelectorFile()
					.getConfigurationSection("selector." + slot);
			List<String> lore = section.getStringList("online.lore");
			ItemMeta meta = itemstack.getItemMeta();
			if (section.getBoolean("server.ping-server")) {
				String serverName = section.getString("server.server-id");
				Map<String, Object> placeholders = null;
				boolean isOnline = false;
				if (Main.PLACEHOLDERS.containsKey(serverName)) {
					placeholders = Main.PLACEHOLDERS.get(serverName);
					isOnline = (boolean) placeholders.get("isOnline");
				}
				if (isOnline) {
					String onlineplayers = (String) placeholders.get("online");
					String maxplayers = (String) placeholders.get("max");
					int online = Integer.valueOf(onlineplayers);
					int max = Integer.valueOf(maxplayers);
					meta.setLore(utils.replaceLore(lore, max, online));
					meta.setDisplayName(utils.replace(section.getString("online.name"), 0, 0, 0));
					itemstack.setItemMeta(meta);
					inv.setItem(slot, itemstack);
				}
				if (!isOnline) {
					final ItemStack offline = itemsOffline.get(slot);
					inv.setItem(slot, offline);
				}
			}
		}
	}
	
	public boolean isServerOnline(int slot) {
		boolean isOnline = false;
		final ConfigurationSection sec = selectorFile.getSelectorFile().getConfigurationSection("selector." + slot);
		if (sec.getBoolean("server.ping-server")) {
			String serverName = sec.getString("server.server-id");
			Map<String, Object> placeholders = null;
			if (Main.PLACEHOLDERS.containsKey(serverName)) {
				placeholders = Main.PLACEHOLDERS.get(serverName);
				isOnline = (boolean) placeholders.get("isOnline");
			} else {
				isOnline = false;
			}
		}
		Logger.debug("Online?: " + isOnline + " Slot: " + slot);
		return isOnline;
	}
}