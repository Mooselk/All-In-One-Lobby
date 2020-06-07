package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.utils.Utils;

public class SelectorConfig extends Config {

	private static File selector;
	private static FileConfiguration selectorConfiguration;
	
	@Override
	public void create() {
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		if (!selector.exists()) {
			selector.getParentFile().mkdirs();
			Main.getInstance().saveResource("selector.yml", false);
			try {
				selector.createNewFile();
				Bukkit.getLogger().info("[Lobby] Creating selector.yml...");
			} catch (IOException e) {
				Bukkit.getLogger().severe("[Lobby] Failed to create selector.yml!");
				e.printStackTrace();
			}
		}
		selectorConfiguration = new YamlConfiguration();
		try {
			selectorConfiguration.load(selector);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			selectorConfiguration.save(selector);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to save selector.yml!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean reload() {
		selector = null;
		selectorConfiguration = null;
		selector = new File(Main.getInstance().getDataFolder(), "selector.yml");
		selectorConfiguration = new YamlConfiguration();
		try {
			selectorConfiguration.load(selector);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[Lobby] Failed to load selector.yml!");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public FileConfiguration getConfig() {
		return selectorConfiguration;
	}
	
	@Override
	public String getName() {
		return "ServerSelector";
	}
	
	public Set<String> get(String path) {
		return selectorConfiguration.getConfigurationSection(path).getKeys(false);
	}
	
	public ConfigurationSection getSection(String path) {
		return selectorConfiguration.getConfigurationSection(path);
	}
	
	public Material getSelectorMaterial() {
		return Material.getMaterial(getConfig().getString("selector.options.material"));
	}
	
	public boolean isEnabled() {
		return getConfig().getBoolean("selector.options.enabled");
	}
	
	public String getInvTitle() {
		return Utils.color(getConfig().getString("selector.options.title"));
	}
	
	public String getItemName() {
		return getConfig().getString("selector.options.item-name");
	}
	
	public List<String> getItemLore() {
		return getConfig().getStringList("selector.options.lore");
	}
	
	public List<String> getItemLore(String path) {
		return getConfig().getStringList("selector." + path + ".lore");
	}
	
	public int getInvSlot() {
		return getConfig().getInt("selector.options.slot");
	}
	
	public int getRows() {
		return getConfig().getInt("selector.options.rows");
	}
	
	public String getItemsPath() {
		return "selector.items.";
	}
}
