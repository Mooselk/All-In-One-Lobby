package me.kate.lobby.items.portals.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.utils.ItemBuilder;

public class PortalWand {
	
	private static final String NAME = "&5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2";
	private static final  List<String> LORE = Arrays.asList("&8A special portal-tool.");
	
	public static final ItemStack WAND = new ItemBuilder(Material.STICK).setName(ChatColor.translateAlternateColorCodes('&', NAME)).setLore(colorLore(LORE)).toItemStack();
	
	private static List<String> colorLore(List<String> lore) {
		List<String> nlore = new ArrayList<String>();
		for (String l : lore) {
			nlore.add(ChatColor.translateAlternateColorCodes('&', l));
		}
		return nlore;
	}
}
