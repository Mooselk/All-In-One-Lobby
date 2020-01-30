package me.kate.lobby.gui;

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
		inventory = Bukkit.createInventory(null, invSize, invName);
		actions = new HashMap<>();
		inventoriesByUUID.put(getUuid(), this);
	}
	
	public void open(Player player){
		player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUuid());
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
	
	public void setItem(int slot, ItemStack stack, GUIAction action){
        inventory.setItem(slot, stack);
        if (action != null){
            actions.put(slot, action);
        }
    }
	
	public void setItem(int slot, ItemStack stack){
        setItem(slot, stack, null);
    }
	
//	public void setItemMap(Map<?,?> map) {
//		int slot = 0;
//		this.clearInventory();
//		for (Map.Entry<?, ?> mapkey : map.entrySet()) {
//			setItem(slot, new ItemBuilder(Material.COMMAND)
//					.setName("&c" + mapkey.getKey())
//					.toItemStack(), player -> {
//				player.sendMessage("sa");
//	        });
//			slot++;
//		}
//	}
	
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
