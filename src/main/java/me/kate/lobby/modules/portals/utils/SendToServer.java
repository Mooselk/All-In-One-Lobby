package me.kate.lobby.modules.portals.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;

public class SendToServer {

	public static void send(Player player, String server) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (IOException | NullPointerException e) {
			Bukkit.getLogger().severe("Error sending player to server, are you not running bungeecord?");
			e.printStackTrace();
		}
		Bukkit.getPlayer(player.getName()).sendPluginMessage(Main.getInstance(), "BungeeCord", byteStream.toByteArray());
	}
}
