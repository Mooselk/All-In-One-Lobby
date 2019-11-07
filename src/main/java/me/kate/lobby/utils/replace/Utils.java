package me.kate.lobby.utils.replace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.kate.lobby.data.files.NPCConfig;

public class Utils implements IUtils {

	@Override
	public ItemStack itemStackBuilder(String mat, ItemStack item, String displayName, List<String> lore,
			boolean enchanted, int data) {
		item = new ItemStack(Material.getMaterial(mat), 1, (short) data);
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
	
	@Override
	public String replacePlayer(String in, Player player) {
		String out = in.replaceAll("%player%", player.getName());
		out = ChatColor.translateAlternateColorCodes('&', out);
		return out;
	}

	@Override
	public List<String> replaceLore(List<String> lore, int max, int online, int ping, boolean noPing) {
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

	@Override
	public String replace(String in, ConfigurationSection section, int max, int online, int ping) {
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
	
	@Override
	public ArrayList<String> colorParser(List<String> list) {
		ArrayList<String> newList = null;
		newList = new ArrayList<String>();
		for (String line : list) {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return newList;
	}
	
	// Test first
	@Override
	public void toConfig(Location location, FileConfiguration config, String path) {
		config.set(path + ".x", location.getBlockX());
		config.set(path + ".y", location.getBlockY());
		config.set(path + ".z", location.getBlockZ());
		config.set(path + ".pitch", location.getPitch());
		config.set(path + ".yaw", location.getYaw());
	}
	
	private final boolean live = false;
	private final int port = 25565;
	private final String ip = "localhost";
	private final List<String> defaultHoloText = Arrays.asList("Edit this text", "Players: %players%");
	private final List<String> defaultMessages = Arrays.asList("&3Default message!", "&9Second line!");
	private final List<String> equipmentExample = Arrays.asList("helmet:IRON_HELMET:true", "hand:STONE_SWORD");
	
	@Override
	public void npcToConfig(Location location, FileConfiguration config, String path, String npcName, int skinId) {
		config.set(path + ".id", npcName);
		config.set(path + ".skin", skinId);
		config.set(path + ".holotext", defaultHoloText);
		config.set(path + ".messages", defaultMessages);
		config.set(path + ".equipment", equipmentExample);
		config.set(path + ".server.server-name", "example");
		config.set(path + ".server.live-player-count", live);
		config.set(path + ".server.ip", ip);
		config.set(path + ".server.port", port);
		config.set(path + ".location.x", location.getBlockX());
		config.set(path + ".location.y", location.getBlockY());
		config.set(path + ".location.z", location.getBlockZ());
		config.set(path + ".location.pitch", location.getPitch());
		config.set(path + ".location.yaw", location.getYaw());
		NPCConfig.save();
		NPCConfig.reload();
	}
	
	@Override
	public List<String> replaceHoloText(List<String> list, String players) {
		List<String> text = null;
		text = new ArrayList<String>();
		for (String in : list) {
			text.add(ChatColor.translateAlternateColorCodes('&', in.replace("%players%", players)));
		}
		return text;
	}
	
	public String getValue(Map<String, String> map, String value) {
		for (Map.Entry<String, String> s : map.entrySet()) {
			if (s.getValue().equalsIgnoreCase(value)) {
				String key = s.getKey();
				return key;
			}
		}
		return null;
	}
}
