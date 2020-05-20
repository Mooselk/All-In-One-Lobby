package me.kate.lobby.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.kate.lobby.Main;

public class MenuObject {
	
	private static final Map<String, MenuObject> ITEM_BY_SLOT = new HashMap<>();
	
	private String slot;
	private ItemStack itemStack;
	private boolean isDecoration;
	private String message;
	private String server;
	private boolean isLive;
	private String type;
	
	private List<String> lore;
	
	public MenuObject(String slot, ItemStack itemStack, boolean isDecoration, String message, String server, boolean isLive, String type) {
		this.slot = slot;
		this.itemStack = itemStack;
		this.isDecoration = isDecoration;
		this.message = message;
		this.server = server;
		this.isLive = isLive;
		this.type = type;
		
		lore = itemStack.getItemMeta().getLore();
		ITEM_BY_SLOT.put(slot, this);
	}
	
	public static MenuObject getBySlot(String slot) {
		return ITEM_BY_SLOT.get(slot);
	}
	
	public String getSlot() {
		return this.slot;
	}
	
	public int getInvSlot() {
		return Integer.parseInt(slot.split(":")[0]);
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	public ItemMeta getItemMeta() {
		return this.getItemStack().getItemMeta();
	}
	
	public String getDisplayName() {
		return this.getItemMeta().getDisplayName();
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public String getServer() {
		if (server == null) {
			server = "none";
		}
		return this.server;
	}
	
	public String getMessage() {
		if (message == null) {
			message = "none";
		}
		return ChatColor
				.translateAlternateColorCodes(
						'&', this.message);
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean isDecoration() {
		return this.isDecoration;
	}
	
	public boolean isLive() {
		return this.isLive;
	}
	
	public boolean serverIsOnline() {
		Map<String, Object> placeholders = null;
		boolean isOnline = false;
		if (Main.getInstance().getPlaceholders().containsKey(server)) {
			placeholders = Main.getInstance().getPlaceholders().get(server);
			isOnline = (boolean) placeholders.get("isOnline");
		}
		return isOnline;
	}
	
	public int getPlayerCount() {
		Map<String, Object> placeholders = Main.getInstance().getPlaceholders().get(server);
		String online = (String) placeholders.get("online");
		return Integer.valueOf(online);
	}
	
	
	public void setDisplayName(String displayName) {
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(displayName);
		this.getItemStack().setItemMeta(meta);
	}
	
	public void setLore(List<String> lore) {
		ItemMeta meta = this.getItemMeta();
		meta.setLore(lore);
		this.getItemStack().setItemMeta(meta);
	}

}
