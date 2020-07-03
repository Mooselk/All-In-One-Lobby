/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.nms.v1_15_R1.packets;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import org.bukkit.Location;

/**
 * @author Jitse Boonstra
 */
public class PacketPlayOutEntityHeadRotationWrapper {

	public PacketPlayOutEntityHeadRotation create(Location location, int entityId) {
        PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation();

        Reflection.getField(packetPlayOutEntityHeadRotation.getClass(), "a", int.class).
                set(packetPlayOutEntityHeadRotation, entityId);
        Reflection.getField(packetPlayOutEntityHeadRotation.getClass(), "b", byte.class)
                .set(packetPlayOutEntityHeadRotation, (byte) ((int) location.getYaw() * 256.0F / 360.0F));

        return packetPlayOutEntityHeadRotation;
    }
}
