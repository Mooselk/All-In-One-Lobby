package me.kate.lobby.modules.portals.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.utils.ItemBuilder;

public class PortalWand {
	
	private static final String NAME = "&5Left click: &dSelect pos #1; &5Right click: &dSelect pos #2";
	
	private static final String[] LORE = { "&8A special portal-tool." };
	
	public static final ItemStack WAND = new ItemBuilder(Material.STICK).setName(NAME).setLore(LORE).setUnstackable().toItemStack();
	
}
