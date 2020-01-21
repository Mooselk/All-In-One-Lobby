package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
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
	
	@Override
	public Set<String> get(String key) {
		return getConfig().getConfigurationSection(key).getKeys(false);
	}

	@Override
	public ConfigurationSection getSection(String section) {
		return getConfig().getConfigurationSection(section);
	}
	
	public String getTopBlock() {
		return getConfig().getString("settings.top-block");
	}
	
	public String getBottomBlock() {
		return getConfig().getString("settings.top-block");
	}
	
	public String getSound() {
		return getConfig().getString("settings.sound");
	}
	
	public boolean isSoundEnabled() {
		return getConfig().getBoolean("jumppad.sound");
	}
	
	public double getPower() {
		return getConfig().getDouble("settings.power");
	}
	
	public double getHeight() {
		return getConfig().getDouble("settings.height");
	}
	
	public String getPitchOne() {
		return getConfig().getString("settings.pitch1");
	}
	
	public String getPitchTwo() {
		return getConfig().getString("settings.pitch2");
	}

}
