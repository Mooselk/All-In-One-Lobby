/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.nms.v1_8_R2.packets;

import java.util.Collections;
import java.util.List;

import com.mojang.authlib.GameProfile;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R2.WorldSettings;

/**
 * @author Jitse Boonstra
 */
public class PacketPlayOutPlayerInfoWrapper {

    public PacketPlayOutPlayerInfo create(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, GameProfile gameProfile, String name) {
        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(packetPlayOutPlayerInfo.getClass(), "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class)
                .set(packetPlayOutPlayerInfo, action);

        PacketPlayOutPlayerInfo.PlayerInfoData playerInfoData = packetPlayOutPlayerInfo.new PlayerInfoData(gameProfile, 1,
                WorldSettings.EnumGamemode.NOT_SET, IChatBaseComponent.ChatSerializer.a("{\"text\":\"[NPC] " + name + "\",\"color\":\"dark_gray\"}"));

        @SuppressWarnings("rawtypes")
		Reflection.FieldAccessor<List> fieldAccessor = Reflection.getField(packetPlayOutPlayerInfo.getClass(), "b", List.class);
        fieldAccessor.set(packetPlayOutPlayerInfo, Collections.singletonList(playerInfoData));

        return packetPlayOutPlayerInfo;
    }
}