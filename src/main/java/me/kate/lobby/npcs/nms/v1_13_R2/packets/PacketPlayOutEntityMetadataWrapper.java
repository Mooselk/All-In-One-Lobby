package me.kate.lobby.npcs.nms.v1_13_R2.packets;

import java.util.Collection;

import me.kate.lobby.npcs.api.state.NPCState;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;

public class PacketPlayOutEntityMetadataWrapper {

    public PacketPlayOutEntityMetadata create(Collection<NPCState> activateStates, int entityId) {
        DataWatcher dataWatcher = new DataWatcher(null);
        byte masked = NPCState.getMasked(activateStates);
        dataWatcher.register(new DataWatcherObject<>(0, DataWatcherRegistry.a), masked);

        return new PacketPlayOutEntityMetadata(entityId, dataWatcher, true);
    }
}