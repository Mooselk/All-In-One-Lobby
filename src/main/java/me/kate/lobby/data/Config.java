package me.kate.lobby.data;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class Config {

	public abstract void create();
	
	public abstract void save();
	
	public abstract boolean reload();
	
	public abstract FileConfiguration getConfig();
}
