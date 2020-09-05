package me.kate.lobby.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.kate.lobby.utils.Utils;

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
		
		this.lore = itemStack.getItemMeta().getLore();
		ITEM_BY_SLOT.put(slot, this);
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
		return itemStack.getItemMeta();
	}
	
	public String getDisplayName() {
		return this.getItemMeta().getDisplayName();
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public String getServer() {
		if (server == null)
			server = "none";
		
		return this.server;
	}
	
	public String getMessage() {
		if (message == null)
			message = "none";
		return Utils.color(message);
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getOnlineKey() {
		return ":online";
	}
	
	public String getOfflineKey() {
		return ":offline";
	}
	
	public boolean isDecoration() {
		return this.isDecoration;
	}
	
	public boolean isLive() {
		return this.isLive;
	}
	
	public boolean serverIsOnline() {
		PlaceHolder placeholder = PlaceHolder.getPlaceHolders().get(server);
		
		if (placeholder == null) {
			return false;
		}
		return placeholder.isOnline();
	}
	
	public String getPlayerCount() {
		
		PlaceHolder placeholder = PlaceHolder.getPlaceHolders().get(server);
		
		if (placeholder == null) {
			return "0";
		}
		return placeholder.getPlayerCount();
	}	
	
	public void setDisplayName(String displayName) {
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(displayName);
		itemStack.setItemMeta(meta);
	}
	
	public void setLore(List<String> lore) {
		ItemMeta meta = this.getItemMeta();
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
	}

}
