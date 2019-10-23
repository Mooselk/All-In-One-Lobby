package me.kate.lobby.utils.replace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
}
