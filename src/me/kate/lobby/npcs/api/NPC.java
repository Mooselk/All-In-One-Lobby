/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.api;

import me.kate.lobby.npcs.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface NPC {

    /**
     * Set the NPC's location.
     * Use this method before using {@link NPC#create}.
     *
     * @param location The spawn location for the NPC.
     * @return object instance.
     */
    NPC setLocation(Location location);

    /**
     * Set the NPC's skin.
     * Use this method before using {@link NPC#create}.
     *
     * @param skin The skin(data) you'd like to apply.
     * @return object instance.
     */
    NPC setSkin(Skin skin);

    /**
     * Get the location of the NPC.
     *
     * @return The location of the NPC.
     */
    Location getLocation();

    /**
     * Get the world the NPC is located in.
     *
     * @return The world the NPC is located in.
     */
    World getWorld();

    /**
     * Create all necessary packets for the NPC so it can be shown to players.
     *
     * @return object instance.
     */
    NPC create();

    /**
     * Get the ID of the NPC.
     *
     * @return the ID of the NPC.
     */
    String getId();

    /**
     * Test if a player can see the NPC.
     * E.g. is the player is out of range, this method will return false as the NPC is automatically hidden by the library.
     *
     * @param player The player you'd like to check.
     * @return Value on whether the player can see the NPC.
     */
    boolean isShown(Player player);

    /**
     * Show the NPC to a player.
     * Requires {@link NPC#create} to be used first.
     *
     * @param player the player to show the NPC to.
     */
    void show(Player player);

    /**
     * Hide the NPC from a player.
     * Will not do anything if NPC isn't shown to the player.
     * Requires {@link NPC#create} to be used first.
     *
     * @param player The player to hide the NPC from.
     */
    void hide(Player player);

    /**
     * Destroy the NPC, i.e. remove it from the registry.
     * Requires {@link NPC#create} to be used first.
     */
    void destroy();
}