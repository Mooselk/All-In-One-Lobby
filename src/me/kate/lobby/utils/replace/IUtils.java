package me.kate.lobby.utils.replace;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface IUtils {
	List<String> replaceLore(List<String> lore, int max, int online, int ping, boolean noPing);

	String replace(String in, ConfigurationSection section, int max, int online, int ping);

	ItemStack itemStackBuilder(String mat, ItemStack item, String displayName, List<String> lore, boolean enchanted, int type);
}