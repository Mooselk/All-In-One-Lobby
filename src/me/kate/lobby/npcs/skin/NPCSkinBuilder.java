package me.kate.lobby.npcs.skin;

import org.bukkit.plugin.Plugin;

public class NPCSkinBuilder {

    public static NPCSkin fromUsername(Plugin plugin, String username){
        return new NPCSkin(plugin, SkinType.IDENTIFIER, username);
    }

    public static NPCSkin fromUUID(Plugin plugin, String uuid){
        return new NPCSkin(plugin, SkinType.IDENTIFIER, uuid);
    }

    public static NPCSkin fromMineskin(Plugin plugin, int mineskinid){
        return new NPCSkin(plugin, SkinType.MINESKINID, String.valueOf(mineskinid));
    }

    public static NPCSkin fromPlayer(Plugin plugin){
        return new NPCSkin(plugin, SkinType.PLAYER);
    }

}