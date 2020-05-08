package me.kate.lobby.npcs.nms.v1_13_R1;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.modules.tablist.TabList;
import me.kate.lobby.utils.Utils;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R1.PacketPlayOutPlayerListHeaderFooter;

public class TabList_v1_13_R1 implements TabList {
	
	private Player player;
	
	@Override
	public void update() {
		this.sendHeaderFooter(player);
	}
	
	@Override
	public void sendHeaderFooter(Player player) {    
		this.player = player;
		IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\": \"" + Utils
				.replaceTab(Main
				.getInstance()
				.getConfig()
				.getStringList("tablist.header"), player) + "\"}");
        IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\": \"" + Utils
        		.replaceTab(Main
        		.getInstance()
        		.getConfig()
        		.getStringList("tablist.footer"), player) + "\"}");
        
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        
        try {
        	
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, tabHeader);
            headerField.setAccessible(false);
            
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, tabFooter);
            footerField.setAccessible(false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}