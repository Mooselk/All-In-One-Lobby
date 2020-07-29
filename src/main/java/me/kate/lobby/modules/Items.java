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
	
	private ToggleConfig playerToggleConfig;
	private SelectorConfig selectorConfig;
	
	private ConfigurationSection togglePlayersHide;
	private ConfigurationSection togglePlayersUnhide;
	
	
	public Items(Main plugin) {
		this.playerToggleConfig = new ToggleConfig(plugin);
		this.selectorConfig = new SelectorConfig(plugin);
		
		this.togglePlayersHide = playerToggleConfig.getSection("item.hide");
		this.togglePlayersUnhide = playerToggleConfig.getSection("item.unhide");
	}
	
	
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
