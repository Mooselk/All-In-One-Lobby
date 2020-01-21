package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;

public class ToggleConfig extends Config {

	private static File hideSettings;
	private static FileConfiguration hideSettingsConf;

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
	
	@Override
	public Set<String> get(String key) {
		return getConfig().getConfigurationSection(key).getKeys(false);
	}

	@Override
	public ConfigurationSection getSection(String section) {
		return getConfig().getConfigurationSection(section);
	}

	public String getHideMessage() {
		return getConfig().getString("messages.hide");
	}

	public String getUnhideMessage() {
		return getConfig().getString("messages.unhide");
	}

	public String getHideDisplayName() {
		return getConfig().getString("item.hide.name");
	}

	public String getUnhideDisplayName() {
		return getConfig().getString("item.unhide.name");
	}

	public List<String> getHideLore() {
		return getConfig().getStringList("item.hide.lore");
	}

	public List<String> getUnhideLore() {
		return getConfig().getStringList("item.unhide.lore");
	}
	
	public String getHideMaterial() {
		return getConfig().getString("item.hide.material");
	}

	public String getUnHideMaterial() {
		return getConfig().getString("item.unhide.material");
	}
	
	public int getSlot() {
		return getConfig().getInt("options.slot");
	}

	public int getCooldownLength() {
		return getConfig().getInt("options.cooldown");
	}
	
	public String getCooldownMessage(int timeleft) {
		String message = getConfig().getString("messages.cooldown");
		if (message.contains("%timeleft%")) {
			message = message.replaceAll("%timeleft%", String.valueOf(timeleft));
		}
		return message;
	}

}
