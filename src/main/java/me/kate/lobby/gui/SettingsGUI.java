package me.kate.lobby.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.kate.lobby.gui.npcsettings.NPCSettingsGUI;
import me.kate.lobby.utils.ItemBuilder;

public class SettingsGUI extends GUI {

	private NPCSettingsGUI npcEditor = new NPCSettingsGUI(27, "NPC Settings");
	
	public SettingsGUI(int invSize, String invName) {
		super(invSize, invName);
		
		setItem(4, new ItemBuilder(Material.DIAMOND)
				.setName("&bNPC Settings")
				.setLore(Arrays.asList(
						"&7- Set skins", 
						"&7- Move NPCs", 
						"&7- Delete NPCs", 
						"&7- Reload NPCs"))
				.toItemStack(), player -> {
					// npcEditor.preload(Main.getRegistry().getNPCObjects());
					npcEditor.open(player);
            player.sendMessage("Opened NPC Settings");
        });
		
		setItem(13, new ItemStack(Material.COMMAND), player -> {
			
        });
		
		setItem(22, new ItemStack(Material.LOG), player -> {
            
        });
	}
}
