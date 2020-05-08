package me.kate.lobby.npcs.nms.v1_15_R1.packets;

import java.util.Collection;

import me.kate.lobby.npcs.api.state.NPCState;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;

public class PacketPlayOutEntityMetadataWrapper {

	public PacketPlayOutEntityMetadata create(Collection<NPCState> activateStates, int entityId) {
        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        byte masked = NPCState.getMasked(activateStates);
        // TODO: Find out why NPCState#CROUCHED doesn't work.
        dataWatcher.register(new DataWatcherObject<>(0, DataWatcherRegistry.a), masked);

        return new PacketPlayOutEntityMetadata(entityId, dataWatcher, true);
    }
}