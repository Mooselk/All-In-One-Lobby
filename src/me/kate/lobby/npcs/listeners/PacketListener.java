/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.listeners;

import me.kate.lobby.npcs.smallprotocol.Reflection;
import me.kate.lobby.npcs.smallprotocol.TinyProtocol;
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.api.events.NPCInteractEvent;
import me.kate.lobby.npcs.internal.NPCManager;
import me.kate.lobby.npcs.internal.SimpleNPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        if (packetPlayInUseEntityClazz.isInstance(packet)) {
            SimpleNPC npc = NPCManager.getAllNPCs().stream().filter(
                    check -> check.isShown(player) && check.getEntityId() == (int) entityIdField.get(packet))
                    .findFirst().orElse(null);

            if (npc == null) {
                // Default player, not doing magic with the packet.
                return true;
            }

            if (delay.contains(player.getUniqueId())) {
                return false;
            }

            NPCInteractEvent.ClickType clickType = actionField.get(packet).toString().equals("ATTACK")
                    ? NPCInteractEvent.ClickType.LEFT_CLICK : NPCInteractEvent.ClickType.RIGHT_CLICK;

            Bukkit.getScheduler().runTask(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, clickType, npc)));

            UUID uuid = player.getUniqueId();
            delay.add(uuid);
            Bukkit.getScheduler().runTask(plugin, () -> delay.remove(uuid));
            return false;
        }

        return true;
    }
}
