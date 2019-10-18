package me.kate.lobby.data.files.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface IPlayerSettings {
	
	void create();
	
	void save();
	
	void reload();
	
	void createSection(String section);
	
	boolean sectionExists(String section);
	
	FileConfiguration getPlayerSettings();
	
}