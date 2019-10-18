/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package me.kate.lobby.npcs.internal;

import org.bukkit.entity.Player;

/**
 * @author Jitse Boonstra
 */
interface PacketHandler {

    void createPackets();

    void sendShowPackets(Player player);

    void sendHidePackets(Player player);
}