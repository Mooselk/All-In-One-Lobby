package me.kate.lobby.npcs.nms.v1_14_R1;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.modules.tablist.TabList;
import me.kate.lobby.utils.IUtils;
import me.kate.lobby.utils.Utils;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;

public class TabList_v1_14_R1 implements TabList {
	
	private final IUtils utils = new Utils();
	private Player player;
	
	@Override
	public void update() {
		this.sendHeaderFooter(player);
	}
	
	@Override
	public void sendHeaderFooter(Player player) {    
		this.player = player;
		IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\": \"" + utils
				.replaceTab(Main
				.getInstance()
				.getConfig()
				.getStringList("tablist.header"), player) + "\"}");
        IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\": \"" + utils
        		.replaceTab(Main
        		.getInstance()
        		.getConfig()
        		.getStringList("tablist.footer"), player) + "\"}");
        
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        
        try {
        	
            Field headerField = packet.getClass().getDeclaredField("header");
            headerField.setAccessible(true);
            headerField.set(packet, tabHeader);
            headerField.setAccessible(false);
            
            Field footerField = packet.getClass().getDeclaredField("footer");
            footerField.setAccessible(true);
            footerField.set(packet, tabFooter);
            footerField.setAccessible(false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}