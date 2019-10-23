package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;

public class HidePlayersFile implements IHidePlayerSettings {

	private static File hideSettings;
	private static FileConfiguration hideSettingsConf;
	
	@Override
	public void create() {
		hideSettings = new File(Main.getInstance().getDataFolder(), "hidesettings.yml");
		if (!hideSettings.exists()) {
			hideSettings.getParentFile().mkdirs();
			try {
				hideSettings.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating hidesettings...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create hidesettings.yml!");
				e.printStackTrace();
			}
		}
		hideSettingsConf = new YamlConfiguration();
		try {
			hideSettingsConf.load(hideSettings);
			Bukkit.getLogger().info("[Lobby] Loading playerdata file...");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load hidesettings.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void load() {
		hideSettings = null;
		hideSettingsConf = null;
		hideSettings = new File(Main.getInstance().getDataFolder(), "hidesettings.yml");
		hideSettingsConf = new YamlConfiguration();
		try {
			hideSettingsConf.load(hideSettings);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load hidesettings.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			hideSettingsConf.save(hideSettings);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save hidesettings.yml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void reload() {
		try {
			hideSettingsConf.load(hideSettings);
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("[Lobby] Error updating hidesettings.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public FileConfiguration getHideSettings() {
		this.load();
		return hideSettingsConf;
	}
	
	@Override
	public String getHideMessage() {
		this.load();
		return hideSettingsConf.getString("messages.hide");
	}

	@Override
	public String getUnhideMessage() {
		this.load();
		return hideSettingsConf.getString("messages.unhide");
	}

	@Override
	public String getHideDisplayName() {
		this.load();
		return hideSettingsConf.getString("item.hide.name");
	}

	@Override
	public String getUnhideDisplayName() {
		this.load();
		return hideSettingsConf.getString("item.unhide.name");
	}

	@Override
	public List<String> getHideLore() {
		this.load();
		return hideSettingsConf.getStringList("item.hide.lore");
	}

	@Override
	public List<String> getUnhideLore() {
		this.load();
		return hideSettingsConf.getStringList("item.unhide.lore");
	}

	@Override
	public String getHideMaterial() {
		this.load();
		return hideSettingsConf.getString("item.hide.material");
	}

	@Override
	public String getUnHideMaterial() {
		this.load();
		return hideSettingsConf.getString("item.unhide.material");
	}

	@Override
	public String getCooldownMessage(int timeleft) {
		this.load();
		String message = hideSettingsConf.getString("messages.cooldown");
		if (message.contains("%timeleft%")) {
			message = message.replaceAll("%timeleft%", String.valueOf(timeleft));
		}
		return message;
	}

	@Override
	public int getCooldownLength() {
		this.load();
		return hideSettingsConf.getInt("options.cooldown");
	}
	
}
