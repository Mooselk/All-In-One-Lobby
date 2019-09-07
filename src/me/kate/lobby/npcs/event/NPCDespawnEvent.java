package me.kate.lobby.npcs.event;

import me.kate.lobby.npcs.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class NPCDespawnEvent extends NPCEvent {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public NPCDespawnEvent(Player player, NPC npc){
        super(player, npc);
    }

}