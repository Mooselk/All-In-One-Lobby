package me.kate.lobby.data;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Config {

	public abstract void create();
	
	public abstract void save();
	
	public abstract boolean reload();
	
	public abstract FileConfiguration getConfig();
	
	public abstract Set<String> get(String key);
	
	public abstract ConfigurationSection getSection(String section);
	
	public abstract String getName();
	
}
