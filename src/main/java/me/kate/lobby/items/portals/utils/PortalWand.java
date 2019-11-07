package me.kate.lobby.items.portals.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class PortalWand {
	
	private static final IUtils UTILS = new Utils();
	private static final String NAME = "&5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2";
	private static final  List<String> LORE = Arrays.asList("&8A special portal-tool.");
	
	public static final ItemStack WAND = new ItemBuilder(Material.STICK).setName(ChatColor.translateAlternateColorCodes('&', NAME)).setLore(UTILS.colorParser(LORE)).toItemStack();
}
