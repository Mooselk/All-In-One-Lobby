package me.kate.lobby.npcs;

import me.kate.lobby.npcs.skin.NPCSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NPC {

    void delete();
    Location getLocation();
    int getEntityID(Player p);
    boolean isDeleted();
    int getNpcID();
    NPCSkin getSkin();
    
}