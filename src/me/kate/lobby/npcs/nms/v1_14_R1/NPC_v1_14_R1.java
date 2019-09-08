package me.kate.lobby.npcs.nms.v1_14_R1;

import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.hologram.Hologram;
import me.kate.lobby.npcs.internal.MinecraftVersion;
import me.kate.lobby.npcs.internal.SimpleNPC;
import me.kate.lobby.npcs.nms.v1_14_R1.packets.PacketPlayOutEntityHeadRotationWrapper;
import me.kate.lobby.npcs.nms.v1_14_R1.packets.PacketPlayOutNamedEntitySpawnWrapper;
import me.kate.lobby.npcs.nms.v1_14_R1.packets.PacketPlayOutPlayerInfoWrapper;
import me.kate.lobby.npcs.nms.v1_14_R1.packets.PacketPlayOutScoreboardTeamWrapper;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Jitse Boonstra
 */
public class NPC_v1_14_R1 extends SimpleNPC {

    private Hologram hologram;
    private PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn;
    private PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeamRegister, packetPlayOutScoreboardTeamUnregister;
    private PacketPlayOutPlayerInfo packetPlayOutPlayerInfoAdd, packetPlayOutPlayerInfoRemove;
    private PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation;
    private PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;

    public NPC_v1_14_R1(NPCLib instance, List<String> lines) {
        super(instance, lines);
    }

    @Override
    public void createPackets() {
        this.hologram = new Hologram(location.clone().add(0, 0.5, 0), lines);
        hologram.generatePackets(MinecraftVersion.V1_14_R1);

        PacketPlayOutPlayerInfoWrapper packetPlayOutPlayerInfoWrapper = new PacketPlayOutPlayerInfoWrapper();

        // Packets for spawning the NPC:
        this.packetPlayOutScoreboardTeamRegister = new PacketPlayOutScoreboardTeamWrapper()
                .createRegisterTeam(name); // First packet to send.

        this.packetPlayOutPlayerInfoAdd = packetPlayOutPlayerInfoWrapper
                .create(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, gameProfile, name); // Second packet to send.

        this.packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawnWrapper()
                .create(uuid, location, entityId); // Third packet to send.

        this.packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotationWrapper()
                .create(location, entityId); // Fourth packet to send.

        this.packetPlayOutPlayerInfoRemove = packetPlayOutPlayerInfoWrapper
                .create(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, gameProfile, name); // Fifth packet to send (delayed).

        // Packet for destroying the NPC:
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityId); // First packet to send.

        // Second packet to send is "packetPlayOutPlayerInfoRemove".

        this.packetPlayOutScoreboardTeamUnregister = new PacketPlayOutScoreboardTeamWrapper()
                .createUnregisterTeam(name); // Third packet to send.
    }

    @Override
    public void sendShowPackets(Player player) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        playerConnection.sendPacket(packetPlayOutScoreboardTeamRegister);
        playerConnection.sendPacket(packetPlayOutPlayerInfoAdd);
        playerConnection.sendPacket(packetPlayOutNamedEntitySpawn);
        playerConnection.sendPacket(packetPlayOutEntityHeadRotation);

        hologram.spawn(player);


        Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () ->
                playerConnection.sendPacket(packetPlayOutPlayerInfoRemove), 50);
    }

    @Override
    public void sendHidePackets(Player player, boolean scheduler) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        playerConnection.sendPacket(packetPlayOutEntityDestroy);
        playerConnection.sendPacket(packetPlayOutPlayerInfoRemove);

        hologram.destroy(player);

        if (scheduler) {
            // Sending this a bit later so the player doesn't see the name (for that split second).
            Bukkit.getScheduler().runTaskLater(instance.getPlugin(), () ->
                    playerConnection.sendPacket(packetPlayOutScoreboardTeamUnregister), 5);
        } else {
            playerConnection.sendPacket(packetPlayOutScoreboardTeamUnregister);
        }
    }
}
