package me.kate.lobby.items.compass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.kate.lobby.Main;

public class Compass {

	private static List<String> empty = Arrays.asList("");
	
	public static Player p;
	private static ItemStack i;
	private static FileConfiguration c = Main.getInstance().getConfig();
	private static Inventory inv = Bukkit.createInventory(null, c.getInt("compass.options.rows") * 9,
			ChatColor.translateAlternateColorCodes('&', c.getString("compass.options.name")));

	public Compass(Player player) {
		p = player;
		Compass.update();
	}

	public static Player getPlayer() {
		return p;
	}

	public void openMenu() {
		p.openInventory(inv);
	}
	
	public static void update() {
		for (final String key : c.getConfigurationSection("compass").getKeys(false)) {
			final ConfigurationSection section = c.getConfigurationSection("compass." + key);
			if (!section.getBoolean("decoration")) {
				List<String> lore = null;
				if (section.getBoolean("server.ping-server")) {
					if (!key.equals("options")) {
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
							int online = (int) placeholders.get("online");
							int max = (int) placeholders.get("max");
							int ping = (int) placeholders.get("ping");
							int slot = Integer.valueOf(key);
							lore = section.getStringList("online.lore");
							inv.setItem(slot,
									itemStackBuilder(section.getString("online.material"), i,
											replace(section.getString("online.name"), section, max, online, ping),
											replaceLore(lore, max, online, ping, false),
											section.getBoolean("online.enchanted"), section.getInt("online.byte")));
						}
						if (!isOnline) {
							if (!key.equals("options")) {
								int slot = Integer.valueOf(key);
								lore = section.getStringList("offline.lore");
								inv.setItem(slot,
										itemStackBuilder(section.getString("offline.material"), i,
												replace(section.getString("offline.name"), section, 0, 0, 0),
												replaceLore(lore, 0, 0, 0, false), section.getBoolean("offline.enchanted"),
												section.getInt("offline.byte")));
							}
						}
					}
				} else {
					if (!key.equals("options")) {
						int slot = Integer.valueOf(key);
						lore = section.getStringList("offline.lore");
						inv.setItem(slot,
								itemStackBuilder(section.getString("offline.material"), i, section.getString("offline.name"),
										replaceLore(lore, 0, 0, 0, true), section.getBoolean("offline.enchanted"),
										section.getInt("offline.byte")));
					}
				}
			} 
			if (section.getBoolean("decoration")) {
				if (!key.equals("options")) {
					int slot = Integer.valueOf(key);
					int type = section.getInt("byte");
					ItemStack item = new ItemStack(Material.getMaterial(section.getString("material")), 1, (short) type);
					ItemMeta im = item.getItemMeta();
					im.setDisplayName(section.getString("name"));
					item.setItemMeta(im);
					inv.setItem(slot, item);
				}
			}
		}
	}

	public static ItemStack itemStackBuilder(String mat, ItemStack item, String displayName, List<String> lore,
			boolean enchanted, int type) {
		item = new ItemStack(Material.getMaterial(mat), 1, (short) type);
		ItemMeta im = (ItemMeta) item.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		if (enchanted) {
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}

	private static List<String> replaceLore(List<String> lore, int max, int online, int ping, boolean noPing) {
		ArrayList<String> mlore = new ArrayList<String>();
		if (!mlore.isEmpty()) {
			mlore.clear();
		}
		for (String l : lore) {
			String out = l;
			if (!noPing) {
				if (l.contains("%online%")) {
					out = out.replaceAll("%online%", String.valueOf(online));
				}
				if (l.contains("%max%")) {
					out = out.replaceAll("%max%", String.valueOf(max));
				}
				if (l.contains("%ping%")) {
					out = out.replaceAll("%ping%", String.valueOf(ping) + "ms");
				}
			}
			mlore.add(ChatColor.translateAlternateColorCodes('&', out));
		}
		return mlore;
	}

	private static String replace(String in, ConfigurationSection section, int max, int online, int ping) {
		String out = in;
		if (max != 0 || online != 0 || ping != 0) {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", String.valueOf(online));
			}
			if (in.contains("%max%")) {
				out = out.replaceAll("%max%", String.valueOf(max));
			}
			if (in.contains("%ping%")) {
				out = out.replace("%ping%", String.valueOf(ping) + "ms");
			}
		} else {
			if (in.contains("%online%")) {
				out = out.replaceAll("%online%", "0");
			}
			if (in.contains("%max%")) {
				out = out.replaceAll("%max%", "0");
			}
			if (in.contains("%ping%")) {
				out = out.replaceAll("%ping%", "Offline");
			}
		}
		return out;
	}
}
