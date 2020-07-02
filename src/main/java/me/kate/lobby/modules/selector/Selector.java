package me.kate.lobby.modules.selector;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.modules.selector.gui.GUI;
import me.kate.lobby.objects.MenuObject;
import me.kate.lobby.utils.ItemBuilder;
import me.kate.lobby.utils.Utils;

public class Selector extends GUI {

	private static final Map<String, MenuObject> CONTENTS = Maps.newHashMap();

	private static SelectorConfig selectorConfig = new SelectorConfig();
	private String path;

	public Selector() {
		super(selectorConfig.getRows(), selectorConfig.getInvTitle());
		this.path = selectorConfig.getItemsPath();
	}

	public void update() {
		CONTENTS.values().forEach(mObj -> {
			if (mObj.isDecoration())
				return;
			
			if (mObj.isLive() && mObj.serverIsOnline()) {
				MenuObject online = CONTENTS.get(mObj.getInvSlot() + mObj.getOnlineKey());
				
				online.setDisplayName(Utils.replace(
						online.getDisplayName(), 
						online.getPlayerCount()));
				
				online.setLore(Utils.replaceLore(
						online.getLore(), 
						online.getPlayerCount()));	
				
				setItem(online.getInvSlot(), online.getItemStack(), player -> {
					if (!online.getServer().equals("none"))
						Utils.send(player, online.getServer());
					if (!online.getMessage().equals("none"))
						player.sendMessage(online.getMessage());
				});
			} else {
				MenuObject offline = CONTENTS.get(mObj.getInvSlot() + mObj.getOfflineKey());
				setItem(offline.getInvSlot(), offline.getItemStack(), player -> {
					if (!offline.getServer().equals("none"))
						Utils.send(player, offline.getServer());
					if (!offline.getMessage().equals("none"))
						player.sendMessage(offline.getMessage());
				});
			}
		});
		
	}

	public void setupContents() {
		CONTENTS.values().forEach(menuObject -> {
			
			if (menuObject == null) 
				return;
			if (menuObject.getType().equals("offline"))
				return;
			
			setItem(menuObject.getInvSlot(), menuObject.getItemStack(), player -> {
				if (menuObject.isDecoration())
					return;
				if (!menuObject.getServer().equals("none"))
					Utils.send(player, menuObject.getServer());
				if (!menuObject.getMessage().equals("none"))
					player.sendMessage(menuObject.getMessage());
			});
		});
	}

	public Map<String, MenuObject> getContents() {
		return CONTENTS;
	}

	public void addContents() {
		for (String keys : selectorConfig.get(path)) {

			ConfigurationSection section = selectorConfig.getSection(path + keys);
			
			if (section.getBoolean("decoration")) {
				createItem(path + keys, keys + ":decoration", section);
				continue;
			}
			
			String slotKeyOnline = keys + ":online";
			createItem(path + keys + ".online", slotKeyOnline, section);
			
			String slotKeyOffline = keys + ":offline";
			createItem(path + keys + ".offline", slotKeyOffline, section);
		}
		
	}

	private void createItem(String path, String slot, ConfigurationSection extras) {
		ConfigurationSection section = selectorConfig.getSection(path);
		
		ItemStack item = new ItemBuilder(
				Material.getMaterial(section.getString("material")))
				.setName(section.getString("name"))
				.setDurability((short) section.getInt("byte"))
				.setLore(section.getStringList("lore"))
				.setEnchanted(section.getBoolean("enchanted"))
				.toItemStack();
		
		CONTENTS.put(slot, new MenuObject(slot, item, 
				extras.getBoolean("decoration"),
				extras.getString("message"),
				extras.getString("server.server-id"), 
				extras.getBoolean("server.ping-server"),
				slot.split(":")[1]));
	}
	
	public void reload() {
		this.clearInventory();
		this.addContents();
		this.setupContents();
	}

}
