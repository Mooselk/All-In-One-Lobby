package me.kate.lobby.ping;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;

public class LocalServerPinger {
	
	private static String getVersion(final Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(46) + 1);
    }
	
	public static int getPing(final Player p) {
        final String version = getVersion(Main.getInstance().getServer());
        if (version.startsWith("v1_8_R1")) {
            return ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        if (version.startsWith("v1_8_R3")) {
            return ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        if (version.startsWith("v1_9")) {
            return ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        if (version.startsWith("v1_10")) {
            return ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        if (version.startsWith("v1_11")) {
            return ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        if (version.startsWith("v1_12")) {
            return ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
        }
        return ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)p).getHandle().playerConnection.player.ping;
    }
}
