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
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class Selector {

	protected Config config = new SelectorConfig();

	private Inventory inv = Bukkit.createInventory(null, config.getConfig().getInt("selector.options.rows") * 9,
			ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("selector.options.name")));

	private Map<Integer, ItemStack> itemsOnline = new HashMap<>();
	private Map<Integer, ItemStack> itemsOffline = new HashMap<>();
	private final IUtils utils = new Utils();

	public Selector() {
		this.setup();
	}

	public void open(Player player) {
		this.update();
		player.openInventory(inv);
	}

	public void close(Player player) {
		player.closeInventory();
	}
	
	private void setup() {
		for (final String key : config.getConfig().getConfigurationSection("selector").getKeys(false)) {
			final ConfigurationSection section = config.getConfig().getConfigurationSection("selector." + key);
			if (key.equals("options")) { continue; }
			if (section.getBoolean("decoration")) {
				inv.setItem(Integer.valueOf(key), createItem("selector." + key));
			} else {
				itemsOnline.put(Integer.valueOf(key), createItem("selector." + key + ".online"));
				
				itemsOffline.put(Integer.valueOf(key), createItem("selector." + key + ".offline"));
			}
		}
	}
	
	private void update() {
		for (Map.Entry<Integer, ItemStack> map : itemsOnline.entrySet()) {
			
			int slot = map.getKey();
			ItemStack itemstack = map.getValue();
			ItemMeta meta = itemstack.getItemMeta();
			
			final ConfigurationSection section = config.getConfig().getConfigurationSection("selector." + slot);
			
			String displayName = meta.getDisplayName();
			List<String> lore = meta.getLore();
			
			
			if (section.getBoolean("server.ping-server")) {
				String serverName = section.getString("server.server-id");
				Map<String, Object> placeholders = null;
				boolean isOnline = false;
				
				if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
					placeholders = Main.getInstance().getPlaceholders().get(serverName);
					isOnline = (boolean) placeholders.get("isOnline");
				}
				
				if (isOnline) {
					String onlineplayers = (String) placeholders.get("online");
					String maxplayers = (String) placeholders.get("max");
					int online = Integer.valueOf(onlineplayers);
					int max = Integer.valueOf(maxplayers);
					
					meta.setLore(utils.replaceLore(lore, max, online));
					meta.setDisplayName(utils.replace(displayName, max, online));
					itemstack.setItemMeta(meta);
					
					inv.setItem(slot, itemstack);
				}
				if (!isOnline) {
					ItemStack offline = itemsOffline.get(slot);
					inv.setItem(slot, offline);
				}
			}
		}
	}

	private ItemStack createItem(String path) {
		final ConfigurationSection section = config.getConfig().getConfigurationSection(path);
		String name = section.getString("name");
		Material material = Material.getMaterial(section.getString("material"));
		List<String> lore = section.getStringList("lore");
		short data = (short) section.getInt("byte");
		boolean enchanted = section.getBoolean("enchanted");
		ItemStack item = new ItemBuilder(material)
				.setName(name)
				.setDurability(data)
				.setLore(lore)
				.setEnchanted(enchanted)
				.toItemStack();
		return item;
	}

	public boolean isServerOnline(int slot) {
		boolean isOnline = false;
		final ConfigurationSection sec = config.getConfig().getConfigurationSection("selector." + slot);
		if (sec.getBoolean("server.ping-server")) {
			String serverName = sec.getString("server.server-id");
			Map<String, Object> placeholders = null;
			if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
				placeholders = Main.getInstance().getPlaceholders().get(serverName);
				isOnline = (boolean) placeholders.get("isOnline");
			}
		}
		Logger.debug("Online?: " + isOnline + " Slot: " + slot);
		return isOnline;
	}
}