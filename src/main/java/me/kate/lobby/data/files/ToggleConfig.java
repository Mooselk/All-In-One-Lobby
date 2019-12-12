package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class ToggleConfig extends Config {

	private static File hideSettings;
	public static FileConfiguration hideSettingsConf;

	@Override
	public void create() {
		hideSettings = new File(Main.getInstance().getDataFolder(), "hideplayers.yml");
		if (!hideSettings.exists()) {
			hideSettings.getParentFile().mkdirs();
			Main.getInstance().saveResource("hideplayers.yml", false);
			try {
				hideSettings.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating hideplayers.yml...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create hideplayers.yml!");
				e.printStackTrace();
			}
		}
		hideSettingsConf = new YamlConfiguration();
		try {
			hideSettingsConf.load(hideSettings);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load hideplayers.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			hideSettingsConf.save(hideSettings);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save hideplayers.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean reload() {
		hideSettings = null;
		hideSettingsConf = null;
		hideSettings = new File(Main.getInstance().getDataFolder(), "hideplayers.yml");
		hideSettingsConf = new YamlConfiguration();
		try {
			hideSettingsConf.load(hideSettings);
			return true;
		} catch (InvalidConfigurationException | IOException e) {
			Bukkit.getLogger().severe("[Lobby] Error updating hideplayers.yml!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return hideSettingsConf;
	}

	public String getHideMessage() {
		return hideSettingsConf.getString("messages.hide");
	}

	public String getUnhideMessage() {
		return hideSettingsConf.getString("messages.unhide");
	}

	public String getHideDisplayName() {
		return hideSettingsConf.getString("item.hide.name");
	}

	public String getUnhideDisplayName() {
		return hideSettingsConf.getString("item.unhide.name");
	}

	public List<String> getHideLore() {
		return hideSettingsConf.getStringList("item.hide.lore");
	}

	public List<String> getUnhideLore() {
		return hideSettingsConf.getStringList("item.unhide.lore");
	}
	
	public String getHideMaterial() {
		return hideSettingsConf.getString("item.hide.material");
	}

	public String getUnHideMaterial() {
		return hideSettingsConf.getString("item.unhide.material");
	}

	public String getCooldownMessage(int timeleft) {
		String message = hideSettingsConf.getString("messages.cooldown");
		if (message.contains("%timeleft%")) {
			message = message.replaceAll("%timeleft%", String.valueOf(timeleft));
		}
		return message;
	}

	public int getCooldownLength() {
		return hideSettingsConf.getInt("options.cooldown");
	}

}
