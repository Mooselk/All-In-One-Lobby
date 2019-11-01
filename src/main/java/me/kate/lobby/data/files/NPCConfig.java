package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;

public class NPCConfig {
	
	private static File npcfile;
	private static FileConfiguration npcconf;
	
	public static FileConfiguration getNPCConfig() {
		return npcconf;
	}

	public static void create() {
		npcfile = new File(Main.getInstance().getDataFolder(), "npcs.yml");
		if (!npcfile.exists()) {
			npcfile.getParentFile().mkdirs();
			Main.getInstance().saveResource("npcs.yml", false);
			Bukkit.getLogger().info("[Lobby] Creating npcs...");
		}
		npcconf = new YamlConfiguration();
		try {
			npcconf.load(npcfile);
			Bukkit.getLogger().info("[Lobby] Loading npcs file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to create npcs.yml!");
			e.printStackTrace();
		}
	}
	public static void save() {
		try {
			npcconf.save(npcfile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save npcs.yml!");
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		try {
			npcconf.load(npcfile);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to reload npcs.yml!");
			e.printStackTrace();
		}
	}
	
	public static void forceLoad() {
		npcfile = new File(Main.getInstance().getDataFolder(), "npcs.yml");
		npcconf = new YamlConfiguration();
		try {
			npcconf.load(npcfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
