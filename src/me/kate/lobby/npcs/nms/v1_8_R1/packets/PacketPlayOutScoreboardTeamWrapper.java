/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.nms.v1_8_R1.packets;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import net.minecraft.server.v1_8_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.ChatColor;

import java.util.Collection;

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
        // Could not get this working in the PacketPlayOutPlayerInfoWrapper class.
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "c", String.class)
                .set(packetPlayOutScoreboardTeam, ChatColor.BLUE + "[NPC] ");
        Reflection.FieldAccessor<Collection> collectionFieldAccessor = Reflection.getField(
                packetPlayOutScoreboardTeam.getClass(), "g", Collection.class);
        Collection collection = collectionFieldAccessor.get(packetPlayOutScoreboardTeam);
        collection.add(name);
        collectionFieldAccessor.set(packetPlayOutScoreboardTeam, collection);

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
