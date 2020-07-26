package me.kate.lobby.modules;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class Items {
	
	private ToggleConfig playerToggleConfig = new ToggleConfig(Main.getInstance());
	private SelectorConfig selectorConfig = new SelectorConfig(Main.getInstance());
	
	private ConfigurationSection togglePlayersHide = playerToggleConfig.getSection("item.hide");
	private ConfigurationSection togglePlayersUnhide = playerToggleConfig.getSection("item.unhide");
	
	public ItemStack hide() {
		return new ItemBuilder(Material.getMaterial(
				togglePlayersHide.getString("material")), 1)
				.setName(Utils.color(togglePlayersHide.getString("name")))
				.toItemStack();
	}
	
	public ItemStack unHide() {
		return new ItemBuilder(Material.getMaterial(
				togglePlayersUnhide.getString("material")))
				.setName(Utils.color(togglePlayersUnhide.getString("name")))
				.toItemStack();
	}
	
	public ItemStack selector() {
		return new ItemBuilder(selectorConfig.getSelectorMaterial())
				.setName(Utils.color(selectorConfig.getItemName()))
				.setLore(Utils.colorParser(selectorConfig.getItemLore()))
				.toItemStack();
	}
	
	public void giveItems(Player player) {
		
		player.getInventory().setItem(playerToggleConfig.getSlot(), this.hide()); 
		
		if (selectorConfig.isEnabled()) {
			player.getInventory().setItem(selectorConfig.getInvSlot(), selector());
		}
	}
}
