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
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Logger;
import me.kate.lobby.utils.Utils;

public class Selector {

	protected SelectorConfig config = new SelectorConfig();

	private Inventory inventory = Bukkit.createInventory(null, 
			config.getConfig().getInt("selector.options.rows") * 9, 
			ChatColor.translateAlternateColorCodes('&', 
			config.getConfig().getString("selector.options.name")));
	
	private Map<Integer, ItemStack> itemsOnline = new HashMap<>();
	private Map<Integer, ItemStack> itemsOffline = new HashMap<>();
	private IUtils utils = new Utils();

	public Selector() {
		this.setup();
	}

	public void open(Player player) {
		this.update(player);
		player.openInventory(inventory);
		player.updateInventory();
	}

	public void close(Player player) {
		player.closeInventory();
	}
	
	public boolean isServerOnline(int slot) {
		boolean isOnline = false;
		final ConfigurationSection section = config.getSection("selector." + slot);
		if (section.getBoolean("server.ping-server")) {
			String serverName = section.getString("server.server-id");
			Map<String, Object> placeholders = null;
			if (Main.getInstance().getPlaceholders().containsKey(serverName)) {
				placeholders = Main.getInstance().getPlaceholders().get(serverName);
				isOnline = (boolean) placeholders.get("isOnline");
			}
		}
		Logger.debug("Online?: " + isOnline + " Slot: " + slot);
		return isOnline;
	}
	
	public void setup() {
		for (final String key : config.getConfig().getConfigurationSection("selector").getKeys(false)) {
			final ConfigurationSection section = config.getConfig().getConfigurationSection("selector." + key);
			if (key.equals("options")) { continue; }
			if (section.getBoolean("decoration")) {
				inventory.setItem(Integer.valueOf(key), createItem("selector." + key));
			} else {
				itemsOnline.put(Integer.valueOf(key), createItem("selector." + key + ".online"));
				
				itemsOffline.put(Integer.valueOf(key), createItem("selector." + key + ".offline"));
			}
		}
	}
	
	private void update(Player player) {
		for (Map.Entry<Integer, ItemStack> map : itemsOnline.entrySet()) {
			int slot = map.getKey();
			ItemStack itemstack = map.getValue();
			ItemMeta meta = itemstack.getItemMeta();
			
			String displayName = meta.getDisplayName();
			
			final ConfigurationSection section = config.getSection("selector." + slot);
			
			List<String> lore = section.getStringList("online.lore");
			
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
					int online = Integer.valueOf(onlineplayers);
					meta.setLore(utils.replaceLore(lore, online));
					meta.setDisplayName(utils.replace(displayName, online));
					player.updateInventory();
					itemstack.setItemMeta(meta);
					inventory.setItem(slot, itemstack);
				}
				if (!isOnline) {
					ItemStack offline = itemsOffline.get(slot);
					inventory.setItem(slot, offline);
				}
			}
		}
	}
	
	private ItemStack createItem(String path) {
		final ConfigurationSection section = config.getSection(path);
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
}