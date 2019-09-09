/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.nms.v1_13_R2.packets;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_13_R2.EnumGamemode;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * @author Jitse Boonstra
 */
public class PacketPlayOutPlayerInfoWrapper {

    private final Class<?> packetPlayOutPlayerInfoClazz = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo");
    private final Class<?> playerInfoDataClazz = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
    private final Reflection.ConstructorInvoker playerInfoDataConstructor = Reflection.getConstructor(playerInfoDataClazz,
            packetPlayOutPlayerInfoClazz, GameProfile.class, int.class, EnumGamemode.class, IChatBaseComponent.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public PacketPlayOutPlayerInfo create(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, GameProfile gameProfile, String name) {
        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(packetPlayOutPlayerInfo.getClass(), "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class)
                .set(packetPlayOutPlayerInfo, action);

        Object playerInfoData = playerInfoDataConstructor.invoke(packetPlayOutPlayerInfo,
                gameProfile, 1, EnumGamemode.NOT_SET,
                IChatBaseComponent.ChatSerializer.b("{\"text\":\"" + ChatColor.BLUE + "[NPC] " + name + "\"}")
        );

        Reflection.FieldAccessor<List> fieldAccessor = Reflection.getField(packetPlayOutPlayerInfo.getClass(), "b", List.class);
        List list = fieldAccessor.get(packetPlayOutPlayerInfo);
        list.add(playerInfoData);
        fieldAccessor.set(packetPlayOutPlayerInfo, list);

        return packetPlayOutPlayerInfo;
    }
}
