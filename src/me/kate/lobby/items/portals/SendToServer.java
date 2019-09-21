package me.kate.lobby.items.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;

public class SendToServer {

	public static void send(Player p, String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (IOException | NullPointerException e) {
			Bukkit.getLogger().severe("Error sending player to server, are you not running bungeecord?");
			e.printStackTrace();
		}
		Bukkit.getPlayer(p.getName()).sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
	}
}
