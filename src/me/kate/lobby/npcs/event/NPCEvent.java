package me.kate.lobby.npcs.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.kate.lobby.npcs.NPC;

public abstract class NPCEvent extends Event{

    private Player player;
    private NPC npc;

    public NPCEvent(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
    }


    public Player getPlayer() {
        return this.player;
    }

    public NPC getNPC(){
        return npc;
    }
}