package me.kate.lobby.modules.selector.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUI {

	public static Map<UUID, GUI> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
	
	private Inventory inventory;
	private Map<Integer, GUIAction> actions;
	private UUID uuid;
	
	public GUI(int invSize, String invName) {
		uuid = UUID.randomUUID();
		inventory = Bukkit.createInventory(null, invSize * 9, invName);
		actions = new HashMap<>();
		inventoriesByUUID.put(getUuid(), this);
	}
	
	public void open(Player player) {
		player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUuid());
        // to-do
        // start update task
    }
	
	public void close(Player player) {
		player.closeInventory();
		// to-do
		// stop update task
	}
	
	public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = openInventories.get(player.getUniqueId());
            if (uuid.equals(getUuid())) {
                player.closeInventory();
            }
        }
        inventoriesByUUID.remove(getUuid());
    }
	
	public void setItem(int slot, ItemStack stack, GUIAction action) {
        inventory.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }
	
	public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }
	
	public void removeItem(ItemStack stack) {
		inventory.remove(stack);
	}
	
	public void clearInventory() {
		inventory.clear();
	}
	
	public static Map<UUID, GUI> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Map<Integer, GUIAction> getActions() {
        return actions;
    }
	
	public Inventory getInventory() {
        return inventory;
    }
	
	public UUID getUuid() {
        return uuid;
    }
	
	public interface GUIAction {
		
        void click(Player player);
        
    }
}
