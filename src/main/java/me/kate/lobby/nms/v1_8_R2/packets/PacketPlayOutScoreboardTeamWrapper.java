/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.nms.v1_8_R2.packets;

import java.util.Collection;
import java.util.Collections;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import net.minecraft.server.v1_8_R2.PacketPlayOutScoreboardTeam;

/**
 * @author Jitse Boonstra
 */
public class PacketPlayOutScoreboardTeamWrapper {

    public PacketPlayOutScoreboardTeam createRegisterTeam(String name) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "h", int.class)
                .set(packetPlayOutScoreboardTeam, 0);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "b", String.class)
                .set(packetPlayOutScoreboardTeam, name);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "a", String.class)
                .set(packetPlayOutScoreboardTeam, name);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "e", String.class)
                .set(packetPlayOutScoreboardTeam, "never");
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "i", int.class)
                .set(packetPlayOutScoreboardTeam, 1);
        @SuppressWarnings("rawtypes")
		Reflection.FieldAccessor<Collection> collectionFieldAccessor = Reflection.getField(
                packetPlayOutScoreboardTeam.getClass(), "g", Collection.class);
        collectionFieldAccessor.set(packetPlayOutScoreboardTeam, Collections.singletonList(name));

        return packetPlayOutScoreboardTeam;
    }

    public PacketPlayOutScoreboardTeam createUnregisterTeam(String name) {
        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "h", int.class)
                .set(packetPlayOutScoreboardTeam, 1);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "a", String.class)
                .set(packetPlayOutScoreboardTeam, name);

        return packetPlayOutScoreboardTeam;
    }
}