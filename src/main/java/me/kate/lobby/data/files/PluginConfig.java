package me.kate.lobby.data.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.data.Config;
import me.kate.lobby.listeners.SpawnMode;

public class PluginConfig extends Config {

	private static File conf;
	private static FileConfiguration configuration;
	
	@Override
	public void create() {
		conf = new File(Main.getInstance().getDataFolder(), "config.yml");
		if (!conf.exists()) {
			conf.getParentFile().mkdirs();
			Main.getInstance().saveResource("config.yml", false);
		}
		configuration = new YamlConfiguration();
		try {
			configuration.load(conf);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		Main.getInstance().saveConfig();
	}

	@Override
	public boolean reload() {
		Main.getInstance().reloadConfig();
		return true;
	}

	@Override
	public FileConfiguration getConfig() {
		return configuration;
	}
	
	@Override
	public String getName() {
		return "Config";
	}

	public Set<String> get(String key) {
		return this.getConfig().getConfigurationSection(key).getKeys(false);
	}
	
	public ConfigurationSection getSection(String section) {
		return this.getConfig().getConfigurationSection(section);
	}
	
	public boolean motdIsEnabled() {
		return this.getSection("join-motd").getBoolean("enabled");
	}
	
	public boolean tablistIsEnabled() {
		return getConfig().getBoolean("tablist.enabled");
	}
	
	public List<String> getJoinMOTD() {
		return this.getSection("join-motd").getStringList("motd");
	}
	
	public String getIp(String key) {
		return this.getSection("servers." + key).getString("ip");
	}
	
	public int getPort(String key) {
		return this.getSection("servers." + key).getInt("port");
	}
	
	public String getJoinMessage() {
		String msg = getConfig().getString("options.custom-joinmsg");
		
		if (msg.equalsIgnoreCase("none") 
				|| msg.equals("")) {
			return null;
		}
		
		return color(msg);
	}
	
	public String getLeaveMessage() {
		String msg = getConfig().getString("options.custom-leavemsg");
		
		if (msg.equalsIgnoreCase("none") 
				|| msg.equals("")) {
			return null;
		}
		
		return color(msg);
	}
	
	public boolean disableMobSpawning() {
		return getConfig().getBoolean("options.mob-spawning.disable-mobSpawning");
	}
	
	public SpawnMode getSpawnMode() {
		return SpawnMode.getSpawnMode(getConfig().getString("options.mob-spawning.mode"));
	}
	
	public boolean blockPlaceIsDisabled() {
		return getConfig().getBoolean("options.build.disable-block-place");
	}
	
	public boolean blockBreakIsDisabled() {
		return getConfig().getBoolean("options.build.disable-block-break");
	}
	
	public String getBreakDenyMessage() {
		return color(getConfig().getString("options.build.block-break-msg"));
	}
	
	public String getPlaceDenyMessage() {
		return color(getConfig().getString("options.build.block-place-msg"));
	}
}
