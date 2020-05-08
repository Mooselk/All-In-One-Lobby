package me.kate.lobby.modules;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.modules.toggleplayers.Hideable;
import me.kate.lobby.modules.toggleplayers.TogglePlayers;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class Items {
	
	private ToggleConfig playerToggleConfig = new ToggleConfig();
	private SelectorConfig selectorConfig = new SelectorConfig();

	private Hideable playerToggle = new TogglePlayers();
	
	private ConfigurationSection togglePlayersHide = playerToggleConfig.getSection("item.hide");
	private ConfigurationSection togglePlayersUnhide = playerToggleConfig.getSection("item.unhide");
	
	public ItemStack hide() {
		return new ItemBuilder(Material.getMaterial(togglePlayersHide.getString("material")), 1)
				.setName(ChatColor.translateAlternateColorCodes('&', togglePlayersHide.getString("name")))
				.toItemStack();
	}
	
	public ItemStack unHide() {
		return new ItemBuilder(Material.getMaterial(togglePlayersUnhide.getString("material")))
				.setName(ChatColor.translateAlternateColorCodes('&', togglePlayersUnhide.getString("name")))
				.toItemStack();
	}
	
	public ItemStack selector() {
		return new ItemBuilder(Material.getMaterial(selectorConfig.getSection("selector.options").getString("material")))
				.setName(Utils.color(selectorConfig.getSection("selector.options").getString("item-name")))
				.setLore(Utils.colorParser(selectorConfig.getSection("selector.options").getStringList("lore")))
				.toItemStack();
	}
	
	public void giveItems(Player player) {
		final ConfigurationSection hideSection = playerToggleConfig.getSection(player.getUniqueId().toString());
		if (hideSection != null) {
			if (!hideSection.getBoolean("hidden")) { 
				player.getInventory().setItem(playerToggleConfig.getSlot(), this.hide()); 
			}
			if (hideSection.getBoolean("hidden")) {
				player.getInventory().setItem(playerToggleConfig.getSlot(), this.hide());
				playerToggle.setHidden(true, player);
			}
		} else { player.getInventory().setItem(playerToggleConfig.getSlot(), this.hide()); }
		if (selectorConfig.getSection("selector.options").getBoolean("enabled")) {
			if (!player.getInventory().contains(selectorConfig.getSelectorMaterial(), 1)) {
				player.getInventory().setItem(selectorConfig.getSlot(), this.selector());
			}
		}
	}
}
