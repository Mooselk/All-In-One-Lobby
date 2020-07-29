package me.kate.lobby.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import me.kate.lobby.Main;

public class Logger {
	
	private static final Server SERVER = Bukkit.getServer();
	
	public static void debug(String msg) {
		if (Main.DEBUG) { 
			SERVER.getLogger().info("[DEBUG] " + msg);
		}
	}
	
	public static void info(String msg) {
		SERVER.getLogger().info(msg);
	}
	
	public static void warn(String msg) {
		SERVER.getLogger().warning(msg);
	}
	
	public static void severe(String msg) {
		SERVER.getLogger().severe(msg);
	}
}
