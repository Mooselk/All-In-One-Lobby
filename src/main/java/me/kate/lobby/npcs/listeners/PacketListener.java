/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.listeners;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.npcs.internal.NPCBase;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.npcs.smallprotocol.Reflection;
import me.kate.lobby.npcs.smallprotocol.TinyProtocol;

/**
 * @author Jitse Boonstra
 */
public class PacketListener {

    // Classes:
    private final Class<?> packetPlayInUseEntityClazz = Reflection.getMinecraftClass("PacketPlayInUseEntity");

    // Fields:
    @SuppressWarnings("rawtypes")
	private final Reflection.FieldAccessor entityIdField = Reflection.getField(packetPlayInUseEntityClazz, "a", int.class);
    @SuppressWarnings("rawtypes")
	private final Reflection.FieldAccessor actionField = Reflection.getField(packetPlayInUseEntityClazz, "action", Object.class);

    // Prevent players from clicking at very high speeds.
    private final Set<UUID> delay = new HashSet<>();

    private Plugin plugin;

    public void start(NPCLib instance) {
        this.plugin = instance.getPlugin();

        new TinyProtocol(instance) {

            @Override
            public Object onPacketInAsync(Player player, Object packet) {
                return handleInteractPacket(player, packet) ? super.onPacketInAsync(player, packet) : null;
            }
        };
    }

    private boolean handleInteractPacket(Player player, Object packet) {
        if (!packetPlayInUseEntityClazz.isInstance(packet))
            return true; // We aren't handling the packet.

        NPCBase npc = null;
        int packetEntityId = (int) entityIdField.get(packet);

        // Not using streams here is an intentional choice.
        // Packet listeners is one of the few places where it is important to write optimized code.
        // Lambdas (And the stream api) create a massive amount of objects, especially if it isn't a static lambda.
        // So, we're avoiding them here.
        // ~ Kneesnap, 9 / 20 / 2019.

        for (NPCBase testNPC : NPCManager.getAllNPCs()) {
            if (testNPC.isShown(player) && testNPC.getEntityId() == packetEntityId) {
                npc = testNPC;
                break;
            }
        }

        if (npc == null) {
            // Default player, not doing magic with the packet.
            return true;
        }

        if (delay.contains(player.getUniqueId())) { // There is an active delay.
            return false;
        }

        NPCInteractEvent.ClickType clickType = actionField.get(packet).toString().equals("ATTACK")
                ? NPCInteractEvent.ClickType.LEFT_CLICK : NPCInteractEvent.ClickType.RIGHT_CLICK;

        delay.add(player.getUniqueId());
        Bukkit.getScheduler().runTask(plugin, new TaskCallNpcInteractEvent(new NPCInteractEvent(player, clickType, npc), this));
        return false;
    }

    // This would be a non-static lambda, and its usage matters, so we'll make it a full class.
    private static final class TaskCallNpcInteractEvent implements Runnable {
        private NPCInteractEvent eventToCall;
        private PacketListener listener;

        private static Location playerLocation = new Location(null, 0, 0, 0);

        TaskCallNpcInteractEvent(NPCInteractEvent eventToCall, PacketListener listener) {
            this.eventToCall = eventToCall;
            this.listener = listener;
        }

        @Override
        public void run() {
            Player player = eventToCall.getWhoClicked();
            this.listener.delay.remove(player.getUniqueId()); // Remove the NPC from the interact cooldown.
            if (!Objects.equals(player.getWorld(), eventToCall.getNPC().getWorld()))
                return; // If the NPC and player are not in the same world, abort!

            double distance = player.getLocation(playerLocation).distanceSquared(eventToCall.getNPC().getLocation());
            if (distance <= 64) // Only handle the interaction if the player is within interaction range. This way, hacked clients can't interact with NPCs that they shouldn't be able to interact with.
                Bukkit.getPluginManager().callEvent(this.eventToCall);
        }
    }
}