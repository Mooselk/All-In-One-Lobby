package me.kate.lobby.data.files.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface ISelectorSettings {

	void create();
	
	void load();
	
	void save();
	
	boolean reload();
	
	boolean reloadFailed();
	
	FileConfiguration getSelectorFile();
	
	
}
