package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.utils.Logger;

public class JumpPadConfig extends Config {
	
	private static File jumpPadFile;
	private static FileConfiguration jumpPadConf;

	@Override
	public void create() {
		jumpPadFile = new File(Main.getInstance().getDataFolder(), "pads.yml");
		if (!jumpPadFile.exists()) {
			jumpPadFile.getParentFile().mkdirs();
			Main.getInstance().saveResource("pads.yml", false);
			Logger.info("[Lobby] Creating pads.yml...");
		}
		jumpPadConf = new YamlConfiguration();
		try {
			jumpPadConf.load(jumpPadFile);
		} catch (IOException | InvalidConfigurationException e) {
			Logger.severe("[Lobby] Failed to create pads.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		try {
			jumpPadConf.save(jumpPadFile);
		} catch (IOException e) {
			Logger.severe("[Lobby] Failed to save pads.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean reload() {
		try {
			jumpPadConf.load(jumpPadFile);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			Logger.severe("[Lobby] Failed to reload pads.yml!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return jumpPadConf;
	}
	
}
